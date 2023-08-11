package com.PersonalProject.Jemo.controller.api;

import com.PersonalProject.Jemo.dto.CategoryDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static com.PersonalProject.Jemo.utils.Constants.CATEGORY_ENDPOINT;

public interface CategoryApi {

    @PostMapping(value = CATEGORY_ENDPOINT + "/create" , produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CategoryDto> save(@RequestBody CategoryDto categoryDto);

    @GetMapping(value = CATEGORY_ENDPOINT + "/{categoryName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CategoryDto> findCategoryByNameCategory(@PathVariable String categoryName);

    @GetMapping(value = CATEGORY_ENDPOINT + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CategoryDto> findById(@PathVariable Long id);

    @GetMapping(value = CATEGORY_ENDPOINT + "/All", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CategoryDto>> findAll();

    @DeleteMapping(value = CATEGORY_ENDPOINT + "/delete/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

}
