package com.project.fashionshops.exceptions;

public class DataNotFoundException extends Exception{ // kiểm tra nó trả về Exception về loại nào để phân loại
    // thực thi lại lớp cha
    public DataNotFoundException(String message){
        super(message);
    }
}
// DataNotFoundException quản lý các exi phai
//