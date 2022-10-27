package com.example.sportal.controller;

import com.example.sportal.dto.article.ArticleDTO;
import com.example.sportal.dto.category.CategoryDTO;

import com.example.sportal.dto.category.CreateCategoryDTO;
import com.example.sportal.model.entity.Category;
import com.example.sportal.model.exception.MethodNotAllowedException;
import com.example.sportal.model.exception.NotFoundException;
import com.example.sportal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController extends AbstractController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategories();
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryDTO createCategory(@RequestBody CreateCategoryDTO dto, HttpSession session) {
        if (isAdmin(session)) {
            return categoryService.createCategory(dto);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CategoryDTO deleteCategory(@PathVariable long id, HttpSession session) {
        if (isAdmin(session)) {
            return categoryService.deleteCategory(id);
        }
        throw new MethodNotAllowedException("You don`t have permission to do this action!");
    }

}
