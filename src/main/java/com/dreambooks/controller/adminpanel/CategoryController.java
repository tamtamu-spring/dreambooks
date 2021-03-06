package com.dreambooks.controller.adminpanel;

import com.dreambooks.model.Category;
import com.dreambooks.service.BookService;
import com.dreambooks.service.CategoryService;
import com.dreambooks.utils.SearchObjects;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CategoryController {

    private CategoryService categoryService;
    private BookService bookService;

    public CategoryController(CategoryService categoryService, BookService bookService) {
        this.categoryService = categoryService;
        this.bookService = bookService;
    }

    @GetMapping(value = "/adminpanel/categories")
    public String getAllCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("searchObjects", new SearchObjects());
        return "/adminpanel/categories";
    }

    @GetMapping(value = "/adminpanel/category/{id}")
    public String getBookById(@PathVariable String id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(new Long(id)));
        model.addAttribute("books", bookService.getBooksByCategory(categoryService.getCategoryById(new Long(id)).getDescription()));
        return "/adminpanel/categorydetails";
    }

    @GetMapping(value = "/adminpanel/category/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        categoryService.deleteCategory(new Long(id));

        return "redirect:/adminpanel/categories";
    }

    @PostMapping(value = "/adminpanel/category/update")
    public String updateBook(@Valid Category category, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("books", bookService.getBooksByCategory(categoryService.getCategoryById(category.getId()).getDescription()));
            return "/adminpanel/categorydetails";
        }


        categoryService.saveCategory(category);
        return "redirect:/adminpanel/categories";
    }


    @PostMapping(value = "/adminpanel/category/save")
    public String saveBook(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", bindingResult);
            redirectAttributes.addFlashAttribute("category", category);

            return "redirect:/adminpanel/error";
        }

        categoryService.saveCategory(category);
        return "redirect:/adminpanel/categories";
    }

    @PostMapping(value = "/adminpanel/categories/search")
    public String getSearchObject(@ModelAttribute SearchObjects searchObjects, Model model) {
        model.addAttribute("categories", categoryService.getCategoriesByDescription(searchObjects.getSearchDescription()));

        return "/adminpanel/categories";
    }
}
