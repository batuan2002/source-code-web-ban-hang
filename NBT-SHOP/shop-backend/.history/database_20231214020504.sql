CREATE DATABASE Shop;
USE Shop;
-- khách hàng khi muốn mua hàng => phải đăng ký tài khoản =>  bảng users 
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0
);
ALTER TABLE users ADD COLUMN role_id INT;
CREATE TABLE roles(
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);
ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles(id);


CREATE TABLE tokens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


--hộ trợ đăng nhập từ facebook và google
-- tài khoản xã hội
 CREATE TABLE social_accounts(
 id INT AUTO_INCREMENT PRIMARY KEY,
 provider VARCHAR(20) NOT NULL COMMENT 'tên nhà mạng xh social network',
 provider_id VARCHAR(50) NOT NULL,
 email VARCHAR(150) NOT NULL COMMENT 'Email tk',
 name VARCHAR(100) NOT NULL COMMENT 'ten nguoi dung',
 user_id INT,
 FOREIGN KEY (user_id) REFERENCES users(id)
 );

 --bảng danh mục sản phẩm(category)
 CREATE TABLE categories(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'tên danh mục ví dụ quần áo'
 );

 --bảng chứa sản phẩm (Product)
 CREATE TABLE products(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(350) COMMENT 'tên sản phẩm',
    price FLOAT NOT NULL CHECK(price >=0),
    thumbnail VARCHAR(300) DEFAULT '',
    description LONGTEXT DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    category_id INT,
FOREIGN KEY (category_id) REFERENCES categories(id)
 );

 -- upload nhieu anh
 CREATE TABLE product_images(
    id INT PRIMARY KEY A
 )

 --đặt hàng
 CREATE TABLE orders(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    fullname VARCHAR(100) DEFAULT '',
    email VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    note VARCHAR(100) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    total_money FLOAT CHECK(total_money >= 0)
 );
ALTER TABLE orders ADD COLUMN shipping_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_address VARCHAR(200);
ALTER TABLE orders ADD COLUMN shipping_date DATE;
ALTER TABLE orders ADD COLUMN tracking_number VARCHAR(100);
ALTER TABLE orders ADD COLUMN payment_method VARCHAR(100);


--	xoá 1 đơn hàng => xoá mềm => thêm trường active
ALTER TABLE orders ADD COLUMN active TINYINT(1);

--trạng thái đơn hàng chỉ đc phép nhận "một số giá trị cụ thể"
ALTER TABLE orders
MODIFY COLUMN status ENUM('pending','processing','shipped','delivered','cancelled') COMMENT 'trạng thái đơn hàng';


 CREATE TABLE order_details(
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    price FLOAT CHECK (price >=0),
    number_of_products INT CHECK(number_of_products > 0),
    total_money FLOAT CHECK(total_money >= 0),
    color VARCHAR(20) DEFAULT ''
 );