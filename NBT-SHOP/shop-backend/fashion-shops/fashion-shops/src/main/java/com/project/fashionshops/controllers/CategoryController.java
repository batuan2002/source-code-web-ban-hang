package com.project.fashionshops.controllers;

import com.project.fashionshops.dtos.CategoryDTO;
import com.project.fashionshops.models.Category;
import com.project.fashionshops.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("")
    // nếu tham số truyền vào là 1 đối tượng => data transfer object
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {// category data transfer object đối tượng tuyền tải du liệu
        if (result.hasErrors()){

            List<FieldError> fieldErrorList = result.getFieldErrors();
            List<String> errorMessages = fieldErrorList.stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessages);

        }
        categoryService.createCategory(categoryDTO);
        // requestbody chuyển đổi nọi dung yêu cầu tới đối tuongej java
        return ResponseEntity.ok("insert Category Thành công");
    }


    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
    List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
}
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable Long id,
        @Valid   @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(id,categoryDTO);

        return ResponseEntity.ok("Update category Thành công");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
       categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category Thanh cong"+id);
    }
}
