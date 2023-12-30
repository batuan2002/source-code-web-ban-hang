package com.project.fashionshops.models;

public class OrderStatus {
    // tao ra class ko lien quan toi model
    // de no luu tru du lieu co dinh

    public static final String PENDING = "pending";
    public static final String PROCESSING = "processing";
    public  static  final String  SHIPPED = "shipped";
    public  static final String  DELIVERED = "delivered";
    public  static  final  String CANCELLED = "cancelled";
}
 //PENDING: Đơn hàng đã được đặt nhưng chưa được xử lý.
//PROCESSING: Đơn hàng đang được xử lý.
//SHIPPED: Đơn hàng đã được chuyển nhưng chưa giao.
//DELIVERED: Đơn hàng đã được giao tới tay khách hàng.
//CANCELLED: Đơn hàng đã bị hủy.