package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dao.CategoryDao;
import org.example.jobsearch.dto.CategoryDto;
import org.example.jobsearch.models.Category;
import org.example.jobsearch.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao;

    @Override
    public List<CategoryDto> getCategoriesList() {
        List<Category> categories = categoryDao.getCategoriesList();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category curCat : categories) {
            categoryDtos.add(CategoryDto.builder()
                    .id(curCat.getId())
                    .name(curCat.getName())
                    .build());
        }
        return categoryDtos;
    }
}
