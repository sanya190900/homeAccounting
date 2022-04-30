package com.homeaccounting.services;

import com.homeaccounting.dao.Category;
import com.homeaccounting.dto.CategoryForTransfer;
import com.homeaccounting.dto.GroupForTransfer;
import com.homeaccounting.repositories.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for manipulations with categories.
 */
@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    /**
     * Dependency Injection constructor.
     *
     * @param categoriesRepository repository for categories
     */
    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }


    /**
     * Method for finding all categories.
     *
     * @return List of categories
     */
    public ResponseEntity<List<CategoryForTransfer>> getAllCategories() {
        List<Category> categories = categoriesRepository.findAll();             // Request to repository
        List<CategoryForTransfer> categoriesForTransfer = new ArrayList<>();

        // Preparation categories for transfer
        categories.forEach(
                category ->
                        categoriesForTransfer.add(
                                new CategoryForTransfer(
                                        category.getId(),
                                        category.getName(),
                                        category.getIcon(),
                                        new GroupForTransfer(
                                                category.getIdgroup().getId(),
                                                category.getIdgroup().getType()
                                        )
                                )
                        )
        );

        return new ResponseEntity<>(categoriesForTransfer, HttpStatus.OK);
    }
}
