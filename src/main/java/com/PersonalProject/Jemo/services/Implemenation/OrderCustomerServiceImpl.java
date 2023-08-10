package com.PersonalProject.Jemo.services.Implemenation;

import com.PersonalProject.Jemo.dto.ItemOrderCustomerDto;
import com.PersonalProject.Jemo.dto.OrderCustomerDto;
import com.PersonalProject.Jemo.exception.EntityNotFoundException;
import com.PersonalProject.Jemo.exception.EntityNotValidException;
import com.PersonalProject.Jemo.exception.ErrorCodes;
import com.PersonalProject.Jemo.exception.OperationNotValidException;
import com.PersonalProject.Jemo.model.Customer;
import com.PersonalProject.Jemo.model.ItemOrderCustomer;
import com.PersonalProject.Jemo.model.OrderCustomer;
import com.PersonalProject.Jemo.model.Product;
import com.PersonalProject.Jemo.repository.CustomerRepository;
import com.PersonalProject.Jemo.repository.ItemOrderCustomerRepository;
import com.PersonalProject.Jemo.repository.OrderCustomerRepository;
import com.PersonalProject.Jemo.repository.ProductRepository;
import com.PersonalProject.Jemo.services.OrderCustomerService;
import com.PersonalProject.Jemo.validator.OrderCustomerValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderCustomerServiceImpl implements OrderCustomerService {

    private OrderCustomerRepository orderCustomerRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private ItemOrderCustomerRepository itemOrderCustomerRepository;
    public OrderCustomerServiceImpl(OrderCustomerRepository orderCustomerRepository, ItemOrderCustomerRepository itemOrderCustomerRepository
            ,CustomerRepository customerRepository,ProductRepository productRepository) {
        super();
        this.orderCustomerRepository = orderCustomerRepository;
        this.itemOrderCustomerRepository = itemOrderCustomerRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderCustomerDto save(OrderCustomerDto orderCustomerDto) {
        List<String> errors = OrderCustomerValidator.validator(orderCustomerDto);
        if(!errors.isEmpty()){
            log.error("order invalid {}",orderCustomerDto);
            throw new EntityNotValidException("order invalid", ErrorCodes.ORDER_CUSTOMER_NOT_VALID, errors);
        }

        Optional<Customer> customer = customerRepository.findById(orderCustomerDto.getCustomerDto().getId());
        if(customer.isEmpty()){
            log.warn("Customer not found in BD");
            throw  new EntityNotFoundException("no user in database with this id" + orderCustomerDto.getCustomerDto().getId()
                    ,ErrorCodes.USER_NOT_FOUND);
        }
        List<String> productsErrors = new ArrayList<>();

        if(orderCustomerDto.getItemOrderCustomerDtos() != null){
            orderCustomerDto.getItemOrderCustomerDtos().forEach(itemOder->{
                if (itemOder.getProductDto() != null){
                    Optional<Product> product = productRepository.findById(itemOder.getProductDto().getId());
                    if (product.isEmpty()){
                        productsErrors.add("Product with id was not found " + itemOder.getProductDto().getId());
                    }
                } else {
                    productsErrors.add("Cant save a order with Product NULL ");
                }
            });
        }
        if (!productsErrors.isEmpty()){
            log.warn("Product not found in the database");
            throw new EntityNotValidException("Product not found in the database",ErrorCodes.PRODUCT_NOT_FOUND,productsErrors);
        }
        orderCustomerDto.setDateOrder(Instant.now());
        OrderCustomer savedOrderCustomer = orderCustomerRepository.save(OrderCustomerDto.toEntity(orderCustomerDto));

        if(orderCustomerDto.getItemOrderCustomerDtos() != null){
        orderCustomerDto.getItemOrderCustomerDtos().forEach(item -> {
            ItemOrderCustomer itemOrderCustomer = ItemOrderCustomerDto.toEntity(item);
            itemOrderCustomer.setOrderCustomer(savedOrderCustomer);
            itemOrderCustomerRepository.save(itemOrderCustomer);
        });}


        return OrderCustomerDto.fromEntity(savedOrderCustomer);
    }

    @Override
    public OrderCustomerDto findById(Long id) {
        if(id == null){
            log.error("order id is null");
            return null;
        }
        return orderCustomerRepository.findById(id)
                .map(OrderCustomerDto::fromEntity)
                .orElseThrow(()->
                        new EntityNotFoundException("order not found" + id, ErrorCodes.ORDER_CUSTOMER_NOT_FOUND));
    }

    @Override
    public List<OrderCustomerDto> findAll() {
        return orderCustomerRepository.findAll().stream()
                .map(OrderCustomerDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(id == null){
            log.error("order id is null");
            return;
        }
        List<ItemOrderCustomer> itemOrderCustomers = itemOrderCustomerRepository.findAllByOrderCustomerId(id);
        if (!itemOrderCustomers.isEmpty()){
            throw new OperationNotValidException("Can not delete a order in use ",ErrorCodes.ORDER_CUSTOMER_ALREADY_IN_USE);
        }
            orderCustomerRepository.deleteById(id);


    }
}
