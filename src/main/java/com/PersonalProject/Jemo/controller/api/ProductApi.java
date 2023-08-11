package com.PersonalProject.Jemo.controller.api;

import com.PersonalProject.Jemo.dto.ItemOrderCustomerDto;
import com.PersonalProject.Jemo.dto.ItemOrderSupplierDto;
import com.PersonalProject.Jemo.dto.ProductDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.PersonalProject.Jemo.utils.Constants.PRODUCT_ENDPOINT;
public interface ProductApi {

    @PostMapping(value =PRODUCT_ENDPOINT + "/create",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE )
    ResponseEntity<ProductDto> save(@RequestBody ProductDto productDto);

    @GetMapping(value =PRODUCT_ENDPOINT + "/{productName}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductDto> findByName(@PathVariable String productName);

    @GetMapping(value =PRODUCT_ENDPOINT + "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductDto> findById(@PathVariable Long id);

    @GetMapping(value =PRODUCT_ENDPOINT + "/All",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ProductDto>> findAll();

    @GetMapping(value =PRODUCT_ENDPOINT + "/filter/Category/{idCategory}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ProductDto>> findAllByCategoryId(@PathVariable Long idCategory);

    @GetMapping(value =PRODUCT_ENDPOINT + "/History/OrderCustomer/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ItemOrderCustomerDto>> findHistoryOrderClient(@PathVariable Long id);

    @GetMapping(value =PRODUCT_ENDPOINT + "/History/OrderSupplier/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ItemOrderSupplierDto>> findHistoryOrderSupplier(@PathVariable Long id);

    @DeleteMapping(value =PRODUCT_ENDPOINT + "/delete/{id}")
    ResponseEntity<Void> delete(@PathVariable  Long id);
}
