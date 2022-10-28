package com.example.sportal.service;

import com.example.sportal.dto.category.CategoryDTO;
import com.example.sportal.dto.category.CreateCategoryDTO;
import com.example.sportal.model.entity.Article;
import com.example.sportal.model.entity.Category;
import com.example.sportal.model.exception.*;
import com.example.sportal.repository.CategoryRepository;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService extends AbstractService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public CategoryDTO createCategory(CreateCategoryDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new DataAlreadyExistException("Category with this name already exists!");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new InvalidDataException("Invalid category name!");
        }
        Category category = modelMapper.map(dto, Category.class);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Transactional
    public CategoryDTO deleteCategory(long id) {
        Category category = getCategoryById(id);
        CategoryDTO dto = modelMapper.map(category, CategoryDTO.class);
        categoryRepository.delete(category);
        return dto;
    }
}

