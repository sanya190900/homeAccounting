package com.homeaccounting.controllers;

import com.homeaccounting.dto.CategoryForTransfer;
import com.homeaccounting.services.CategoriesService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for receiving categories requests.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    /**
     * Dependency Injection constructor.
     *
     * @param categoriesService categories service
     */
    @Autowired
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    /**
     * Method for receiving get request on path '/categories'.
     *
     * @return List of all categories in database
     */
    @GetMapping
    public ResponseEntity<List<CategoryForTransfer>> getAllCategories() {
        return categoriesService.getAllCategories();
    }
}
