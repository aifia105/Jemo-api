package com.PersonalProject.Jemo.services.Implemenation;

import com.PersonalProject.Jemo.dto.CartDto;
import com.PersonalProject.Jemo.exception.EntityNotFoundException;
import com.PersonalProject.Jemo.exception.EntityNotValidException;
import com.PersonalProject.Jemo.exception.ErrorCodes;
import com.PersonalProject.Jemo.model.Customer;
import com.PersonalProject.Jemo.repository.CartRepository;
import com.PersonalProject.Jemo.repository.CustomerRepository;
import com.PersonalProject.Jemo.services.CartService;
import com.PersonalProject.Jemo.validator.CartValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CustomerRepository customerRepository) {
        super();
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public CartDto save(CartDto cartDto) {
        List<String> errors = CartValidator.validator(cartDto);
        if (!errors.isEmpty()){
            log.warn("Cart object not valid");
            throw new EntityNotValidException("Cart object not valid", ErrorCodes.CART_NOT_VALID,errors);
        }
        Optional<Customer> customer = customerRepository.findById(cartDto.getCustomer().getId());
        if (customer.isEmpty()){
            log.warn("user not found to associate with this cart");
            throw new EntityNotFoundException("user not found to associate with this cart",ErrorCodes.USER_NOT_FOUND);
        }
        return CartDto.fromEntity(cartRepository.save(CartDto.toEntity(cartDto)));
    }

    @Override
    public CartDto findById(Long id) {
        if(id == null){
            log.warn("Id cart is null");
            return null;
        }
        return cartRepository.findById(id)
                .map(CartDto::fromEntity)
                .orElseThrow(()->
                    new EntityNotFoundException("Cart with this ID not found id = " + id, ErrorCodes.CART_NOT_FOUND));
    }

    @Override
    public CartDto findByUserId(Long id) {
        if (id == null) {
            log.warn("Id User is null");
            return null;
        }
        Optional<CartDto> cart = cartRepository.findByCustomerId(id);
        if (cart.isEmpty()){
         throw new EntityNotFoundException("Cart with this user ID not found UserId = " + id, ErrorCodes.CART_NOT_FOUND);
        }
        return cart.get();
    }

    @Override
    public List<CartDto> findAll() {
        return cartRepository.findAll().stream()
                .map(CartDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (id == null){
            log.warn("Id  cart is null");
            return;
        }
        cartRepository.deleteById(id);

    }
}