package Ecommerce.Application.project.modules.checkout.enums;

public enum OrderStatus {
    PENDING,        // Order created
    PROCESSING,     // Warehouse preparing items
    SHIPPED,        // Out for delivery
    COMPLETED,      // Delivered + confirmed
    CANCELLED       // Order canceled
}
