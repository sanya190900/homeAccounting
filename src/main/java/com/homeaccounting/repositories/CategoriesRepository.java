package com.homeaccounting.repositories;

import com.homeaccounting.dao.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for working with categories and database.
 */
public interface CategoriesRepository extends JpaRepository<Category, Long> {

    /**
     * Get all categories from database.
     *
     * @return List of all categories
     */
    @Query("SELECT c FROM Category c ORDER BY c.id")
    List<Category> findAll();
}
