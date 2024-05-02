package org.example.jobsearch.service;

import org.example.jobsearch.dto.CategoryDto;
import org.example.jobsearch.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryDto> getCategoriesList();

    Boolean isExistsById(Long categoryId);
}
