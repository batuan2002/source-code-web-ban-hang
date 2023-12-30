package com.project.fashionshops.controllers;

import com.github.javafaker.Faker;
import com.project.fashionshops.dtos.CategoryDTO;
import com.project.fashionshops.dtos.ProductDTO;
import com.project.fashionshops.dtos.ProductImageDTO;
import com.project.fashionshops.models.Product;
import com.project.fashionshops.models.ProductImage;
import com.project.fashionshops.responses.ProductListResponse;
import com.project.fashionshops.responses.ProductResponse;
import com.project.fashionshops.services.IProductService;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @PostMapping("")
// consums up load file len no chuyen doi tung phan doi tuong
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<FieldError> fieldErrorList = result.getFieldErrors();
                List<String> errorMessages = fieldErrorList.stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            // phai luu truoc
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files

    ) {
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files; //nếu như file bằng null khỏi tạo 1 mảng rỗng nếu ko thì ta lấy cái cũ
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("Bạn chỉ đc upload 5 ảnh thôi nha");
            }
            List<ProductImage> productImages = new ArrayList<>();
            // dung hàm duyệt danh sach
            for (MultipartFile file : files) {

                if (file.getSize() == 0) {
                    continue;
                }

                // kiểm tra kích thước file và định dạng
                if (file.getSize() > 10 * 1024 * 1024) {// kich thuoc file 10mb

                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too ! Maximum size is 10mb");
                }
                // định dạng file
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) { // kiem tra cai file phai phai anh ko trong image
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image"); // neu ma dinh lỗi
                }
                // lưu file và cập nhật thumbnail trong DTO
                String filename = storeFile(file); //Thay thế hàm này với code của bạn để lưu file
                // lưu vào đối tượng product trong DB
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }


    // ham save file
    private String storeFile(MultipartFile file) throws IOException {
       // kiểm tra ở đoạn này ổn thì mới nhảy xuống
        if (!isImageFile(file) || file.getOriginalFilename() == null){
           throw  new IOException("Invalid image format");
       }
        // lấy ra tên file
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())); // vi sao lai requi chắc chăn nó khác null
        // thêm UUID (Universally Unique Identifier) vào trước file để đảm bảo file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // đường dẫn tới thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        //kiểm tra và tạo thư mực  nếu nó ko tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        //dđường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // sao chép file vào thư mục
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    // kiểm tra xem có phải files ảnh ko qua đoạn mã sau
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        // tạo pageable từ thông tintrang v giới hạn
        PageRequest pageRequest = PageRequest.of(page,limit,
                Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
       // lấy tổng số trang
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById
            (@PathVariable("id") Long productId) {

        try {
         Product existingProduct = productService.getProductById(productId);
         return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));

        } catch (Exception e) {
          return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        try {
          productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Product with id = %d delete", id));
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    //Fake nayf dung để giã mạo Fake data
    // giúp tạo ra nhiều dữ liệu ngẫu nhiên như địa chỉ , sdt , email
    // cài pom trước khi sử dụng
    // dung song com,emt lai khi nao can lai bat len doi public -> prv
  //  @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts(){
        // tạo ra 1 đối tượng fake
        Faker faker = new Faker();
        // vong lặp
        for ( int i =0 ; i < 1_000_000 ; i++){
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)){
                continue; //nếu name đã tồn tại , bỏ qua và tạo sản phẩm mới
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10,90_000_000))
                    .thumbnail("")
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(2,3)) // cái này là số id trong database
                    .build();
            // delete from de xoa bang
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return  ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake Product created thanh cong");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable long id,
            @RequestBody ProductDTO productDTO
    ){
        try {
            Product updatedProduct = productService.updateProduct(id,productDTO);
            return ResponseEntity.ok(updatedProduct);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

// tesst cos anh vaf ko co anh