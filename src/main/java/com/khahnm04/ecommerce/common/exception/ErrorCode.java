package com.khahnm04.ecommerce.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // =================================================================
    // COMMON (Range: 900 - 999)
    // =================================================================
    INTERNAL_SERVER_ERROR(900, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_EXCEPTION(901, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_EXCEPTION(902, "Validation error", HttpStatus.BAD_REQUEST),
    DUPLICATE_ENTRY(903, "Duplicate entry", HttpStatus.BAD_REQUEST),
    LIST_PAGE_MIN(950, "Page must be >= {value}", HttpStatus.BAD_REQUEST),
    LIST_SIZE_MIN(951, "Size must be >= {value}", HttpStatus.BAD_REQUEST),
    LIST_SIZE_MAX(952, "Size must be <= {value}", HttpStatus.BAD_REQUEST),

    // =================================================================
    // MODULE: AUTH (Range: 1000 - 1999)
    // =================================================================
    UNAUTHENTICATED(1000, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1001, "You do not have permission", HttpStatus.FORBIDDEN),
    REFRESH_TOKEN_FAILED(1002, "Refresh token failed", HttpStatus.UNAUTHORIZED),
    TOKEN_CREATION_FAILED(1003, "Token creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_IDENTIFIER_REQUIRED(1004, "Missing login identifier", HttpStatus.BAD_REQUEST),

    ROLE_CODE_REQUIRED(1100, "Role code cannot be blank", HttpStatus.BAD_REQUEST),
    ROLE_NAME_REQUIRED(1101, "Role name cannot be blank", HttpStatus.BAD_REQUEST),
    ROLE_DESCRIPTION_REQUIRED(1102, "Role description cannot be blank", HttpStatus.BAD_REQUEST),
    ROLE_PERMISSIONS_REQUIRED(1103, "Role permissions cannot be empty", HttpStatus.BAD_REQUEST),
    ROLE_NAME_INVALID(1104, "Name must be at least {min} characters", HttpStatus.BAD_REQUEST),
    ROLE_SELECTION_REQUIRED(1105, "At least one role must be selected", HttpStatus.BAD_REQUEST),
    ROLE_ACTION_REQUIRED(1106, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1150, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(1151, "Role already exists", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1152, "Permission not found", HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND(1200, "User not found", HttpStatus.NOT_FOUND),
    USER_EXISTED(1201, "User already existed", HttpStatus.CONFLICT),
    PHONE_NUMBER_REQUIRED(1210, "Phone number cannot be blank", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(1211, "Phone number already exists", HttpStatus.CONFLICT),
    EMAIL_REQUIRED(1220, "Email cannot be blank", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1221, "Email already exists", HttpStatus.CONFLICT),
    PASSWORD_REQUIRED(1230, "Password cannot be blank", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1231, "Password does not match", HttpStatus.BAD_REQUEST),
    PASSWORD_CONFIRMATION_MISMATCH(1232, "Password and confirm password do not match", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_SAME_AS_OLD(1233, "New password cannot be the same as the old password", HttpStatus.BAD_REQUEST),
    PASSWORD_MIN_LENGTH(1234, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),

    // =================================================================
    // MODULE: USER (Range: 2000 - 2999)
    // =================================================================
    EMPLOYEE_STATUS_REQUIRED(2000, "Status cannot be blank", HttpStatus.BAD_REQUEST),
    EMPLOYEE_SELECTION_REQUIRED(2001, "At least one branch must be selected", HttpStatus.BAD_REQUEST),
    EMPLOYEE_ACTION_REQUIRED(2002, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    EMPLOYEE_PHONE_NUMBER_REQUIRED(2003, "Phone number cannot be blank", HttpStatus.BAD_REQUEST),
    EMPLOYEE_CODE_REQUIRED(2004, "Code cannot be blank", HttpStatus.BAD_REQUEST),
    EMPLOYEE_FULL_NAME_REQUIRED(2005, "Full name cannot be blank", HttpStatus.BAD_REQUEST),
    EMPLOYEE_BRANCH_ID_REQUIRED(2006, "Branch ID cannot be blank", HttpStatus.BAD_REQUEST),
    EMPLOYEE_AVATAR_URL_REQUIRED(2007, "Avatar URL cannot be blank", HttpStatus.BAD_REQUEST),
    EMPLOYEE_ROLES_REQUIRED(2008, "roles cannot be empty", HttpStatus.BAD_REQUEST),
    EMPLOYEE_BULK_ACTION_REQUIRED_IDS(2050, "At least one branch must be selected", HttpStatus.BAD_REQUEST),
    EMPLOYEE_INVALID_BULK_ACTION(2051, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),
    EMPLOYEE_NOT_FOUND(2052, "Employee not found", HttpStatus.NOT_FOUND),
    EMPLOYEE_EXISTED(2053, "Employee already existed", HttpStatus.CONFLICT),
    EMPLOYEE_CODE_EXISTED(2054, "Employee code already existed", HttpStatus.CONFLICT),

    CUSTOMER_STATUS_REQUIRED(2100, "Status cannot be blank", HttpStatus.BAD_REQUEST),
    CUSTOMER_SELECTION_REQUIRED(2101, "At least one branch must be selected", HttpStatus.BAD_REQUEST),
    CUSTOMER_ACTION_REQUIRED(2102, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    CUSTOMER_PHONE_NUMBER_REQUIRED(2103, "Phone number cannot be blank", HttpStatus.BAD_REQUEST),
    CUSTOMER_FULL_NAME_REQUIRED(2105, "Full name cannot be blank", HttpStatus.BAD_REQUEST),
    CUSTOMER_AVATAR_URL_REQUIRED(2107, "Avatar URL cannot be blank", HttpStatus.BAD_REQUEST),
    CUSTOMER_BULK_ACTION_REQUIRED_IDS(2150, "At least one branch must be selected", HttpStatus.BAD_REQUEST),
    CUSTOMER_INVALID_BULK_ACTION(2151, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),
    CUSTOMER_NOT_FOUND(2152, "Customer not found", HttpStatus.NOT_FOUND),
    CUSTOMER_EXISTED(2153, "Customer already existed", HttpStatus.CONFLICT),

    ADDRESS_RECEIVER_NAME_REQUIRED(2200, "Receiver name cannot be blank", HttpStatus.BAD_REQUEST),
    ADDRESS_RECEIVER_PHONE_REQUIRED(2201, "Receiver phone cannot be blank", HttpStatus.BAD_REQUEST),
    ADDRESS_RECEIVER_PHONE_INVALID(2202, "Receiver phone is invalid", HttpStatus.BAD_REQUEST),
    ADDRESS_PROVINCE_NAME_REQUIRED(2203, "Province name cannot be blank", HttpStatus.BAD_REQUEST),
    ADDRESS_DISTRICT_NAME_REQUIRED(2204, "District name cannot be blank", HttpStatus.BAD_REQUEST),
    ADDRESS_WARD_NAME_REQUIRED(2205, "Ward name cannot be blank", HttpStatus.BAD_REQUEST),
    ADDRESS_DETAIL_REQUIRED(2206, "Detail address cannot be blank", HttpStatus.BAD_REQUEST),
    ADDRESS_TYPE_INVALID(2207, "Address type is invalid", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(2250, "Address not found", HttpStatus.NOT_FOUND),

    // =================================================================
    // MODULE: BRANCH (Range: 3000 - 3999)
    // =================================================================
    BRANCH_SELECTION_REQUIRED(3001, "At least one branch must be selected", HttpStatus.BAD_REQUEST),
    BRANCH_ACTION_REQUIRED(3002, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    BRANCH_CODE_REQUIRED(3003, "Branch code cannot be blank", HttpStatus.BAD_REQUEST),
    BRANCH_NAME_REQUIRED(3004, "Branch name cannot be blank", HttpStatus.BAD_REQUEST),
    BRANCH_ADDRESS_DETAIL_REQUIRED(3005, "Address detail cannot be blank", HttpStatus.BAD_REQUEST),
    BRANCH_DISTRICT_REQUIRED(3006, "District cannot be blank", HttpStatus.BAD_REQUEST),
    BRANCH_CITY_REQUIRED(3007, "City cannot be blank", HttpStatus.BAD_REQUEST),
    BRANCH_PHONE_REQUIRED(3008, "Phone number cannot be blank", HttpStatus.BAD_REQUEST),
    BRANCH_PHONE_INVALID(3009, "Phone number is invalid", HttpStatus.BAD_REQUEST),
    BRANCH_NOT_FOUND(3050, "Branch not found", HttpStatus.NOT_FOUND),
    BRANCH_EXISTED(3051, "Branch already existed", HttpStatus.CONFLICT),
    BRANCH_BULK_ACTION_REQUIRED_IDS(3052, "At least one branch must be selected", HttpStatus.BAD_REQUEST),
    BRANCH_INVALID_BULK_ACTION(3053, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    // =================================================================
    // MODULE: CATALOG (Range: 4000 - 4999)
    // =================================================================
    CATEGORY_SELECTION_REQUIRED(4000, "At least one category must be selected", HttpStatus.BAD_REQUEST),
    CATEGORY_ACTION_REQUIRED(4001, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_REQUIRED(4003, "Category name cannot be blank", HttpStatus.BAD_REQUEST),
    CATEGORY_SLUG_REQUIRED(4004, "Category slug cannot be blank", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(4050, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(4051, "Category already existed", HttpStatus.CONFLICT),
    CATEGORY_NAME_EXISTED(4052, "Category name existed", HttpStatus.CONFLICT),
    CATEGORY_PARENT_NOT_EXISTED(4053, "Category parent not existed", HttpStatus.CONFLICT),
    CATEGORY_HAS_CHILDREN(4054, "Cannot delete category because it has child categories", HttpStatus.BAD_REQUEST),
    CANNOT_MOVE_PARENT_TO_CHILD(4055, "Cannot move parent to child", HttpStatus.BAD_REQUEST),
    DUPLICATE_CATEGORY_PRODUCT(4056, "Duplicate category product", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_PRODUCTS(4057, "Cannot delete category that has products", HttpStatus.BAD_REQUEST),
    CATEGORY_BULK_ACTION_REQUIRED_IDS(4058, "At least one category must be selected", HttpStatus.BAD_REQUEST),
    CATEGORY_INVALID_BULK_ACTION(4059, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    BRAND_SELECTION_REQUIRED(4100, "At least one brand must be selected", HttpStatus.BAD_REQUEST),
    BRAND_ACTION_REQUIRED(4101, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    BRAND_STATUS_REQUIRED(4102, "Brand status cannot be blank", HttpStatus.BAD_REQUEST),
    BRAND_NAME_REQUIRED(4103, "Brand name cannot be blank", HttpStatus.BAD_REQUEST),
    BRAND_SLUG_REQUIRED(4104, "Brand slug cannot be blank", HttpStatus.BAD_REQUEST),
    BRAND_NOT_FOUND(4150, "Brand not found", HttpStatus.NOT_FOUND),
    BRAND_EXISTED(4151, "Brand already existed", HttpStatus.CONFLICT),
    BRAND_BULK_ACTION_REQUIRED_IDS(4158, "At least one brand must be selected", HttpStatus.BAD_REQUEST),
    BRAND_INVALID_BULK_ACTION(4159, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    ATTRIBUTE_SELECTION_REQUIRED(4200, "At least one category must be selected", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_ACTION_REQUIRED(4201, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_NAME_REQUIRED(4202, "Name cannot be blank", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_CODE_REQUIRED(4203, "Code cannot be blank", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_EXISTED(4251, "Attribute already existed", HttpStatus.CONFLICT),
    ATTRIBUTE_NOT_FOUND(4250, "Attribute not found", HttpStatus.NOT_FOUND),
    ATTRIBUTE_BULK_ACTION_REQUIRED_IDS(4258, "At least one attribute must be selected", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_INVALID_BULK_ACTION(4259, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    PRODUCT_SELECTION_REQUIRED(4300, "At least one product must be selected", HttpStatus.BAD_REQUEST),
    PRODUCT_ACTION_REQUIRED(4301, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_STATUS_REQUIRED(4302, "Product status cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_REQUIRED(4303, "Product name cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_SLUG_REQUIRED(4304, "Product slug cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_BRAND_REQUIRED(4305, "Product brand cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_REQUIRED(4306, "Product price cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_THUMBNAIL_REQUIRED(4307, "Product thumbnail cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_ATTRIBUTE_ID_REQUIRED(4308, "Attribute ID cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_ATTRIBUTE_VALUE_REQUIRED(4309, "Attribute value cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_URL_REQUIRED(4310, "Image URL cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_CATEGORIES_REQUIRED(4311, "Categories cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_ATTRIBUTES_REQUIRED(4311, "Attributes cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGES_REQUIRED(4311, "Images cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(4350, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(4351, "Product already existed", HttpStatus.CONFLICT),
    PRODUCT_CODE_EXISTED(4352, "Product code already existed", HttpStatus.CONFLICT),
    PRODUCT_BULK_ACTION_REQUIRED_IDS(4358, "At least one product must be selected", HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID_BULK_ACTION(4359, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    VARIANT_SELECTION_REQUIRED(4400, "At least one variant must be selected", HttpStatus.BAD_REQUEST),
    VARIANT_ACTION_REQUIRED(4401, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    VARIANT_NAME_REQUIRED(4402, "Variant name cannot be blank", HttpStatus.BAD_REQUEST),
    VARIANT_CODE_REQUIRED(4403, "Variant code cannot be blank", HttpStatus.BAD_REQUEST),
    VARIANT_NOT_FOUND(4450, "Variant not found", HttpStatus.NOT_FOUND),
    VARIANT_EXISTED(4451, "Variant already existed", HttpStatus.CONFLICT),
    VARIANT_BULK_ACTION_REQUIRED_IDS(4458, "At least one variant must be selected", HttpStatus.BAD_REQUEST),
    VARIANT_INVALID_BULK_ACTION(4459, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    VARIANT_VALUE_SELECTION_REQUIRED(4500, "At least one variant value must be selected", HttpStatus.BAD_REQUEST),
    VARIANT_VALUE_ACTION_REQUIRED(4501, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    VARIANT_VALUE_REQUIRED(4502, "Variant value cannot be blank", HttpStatus.BAD_REQUEST),
    VARIANT_VALUE_VARIANT_ID_REQUIRED(4503, "Variant ID cannot be blank", HttpStatus.BAD_REQUEST),
    VARIANT_VALUE_NOT_FOUND(4550, "Variant value not found", HttpStatus.NOT_FOUND),
    VARIANT_VALUE_EXISTED(4551, "Variant value already existed", HttpStatus.CONFLICT),
    VARIANT_VALUE_BULK_ACTION_REQUIRED_IDS(4558, "At least one variant value must be selected", HttpStatus.BAD_REQUEST),
    VARIANT_VALUE_INVALID_BULK_ACTION(4559, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    PRODUCT_VARIANT_SELECTION_REQUIRED(4600, "At least one product variant must be selected", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_ACTION_REQUIRED(4601, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_NAME_REQUIRED(4602, "Product variant name cannot be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_PRICE_REQUIRED(4603, "Product variant price is required", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_PRODUCT_ID_REQUIRED(4604, "Product ID is required", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_STATUS_REQUIRED(4605, "Product variant status is required", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_NOT_FOUND(4650, "Product variant not found", HttpStatus.NOT_FOUND),
    PRODUCT_VARIANT_EXISTED(4651, "Product variant SKU already existed", HttpStatus.CONFLICT),
    PRODUCT_VARIANT_BULK_ACTION_REQUIRED_IDS(4658, "At least one product variant must be selected",
            HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_INVALID_BULK_ACTION(4659, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    // =================================================================
    // MODULE: INVENTORY (Range: 5000 - 5999)
    // =================================================================
    WAREHOUSE_SELECTION_REQUIRED(5000, "At least one warehouse must be selected", HttpStatus.BAD_REQUEST),
    WAREHOUSE_ACTION_REQUIRED(5001, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    WAREHOUSE_STATUS_REQUIRED(5002, "Warehouse status cannot be blank", HttpStatus.BAD_REQUEST),
    WAREHOUSE_CODE_REQUIRED(5003, "Warehouse code cannot be blank", HttpStatus.BAD_REQUEST),
    WAREHOUSE_NAME_REQUIRED(5004, "Warehouse name cannot be blank", HttpStatus.BAD_REQUEST),
    WAREHOUSE_BRANCH_ID_REQUIRED(5005, "Branch ID is required", HttpStatus.BAD_REQUEST),
    WAREHOUSE_NOT_FOUND(5050, "Warehouse not found", HttpStatus.NOT_FOUND),
    WAREHOUSE_EXISTED(5051, "Warehouse already existed", HttpStatus.CONFLICT),
    WAREHOUSE_BULK_ACTION_REQUIRED_IDS(5058, "At least one warehouse must be selected", HttpStatus.BAD_REQUEST),
    WAREHOUSE_INVALID_BULK_ACTION(5059, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    // Supplier (5100 - 5159)
    SUPPLIER_SELECTION_REQUIRED(5100, "At least one supplier must be selected", HttpStatus.BAD_REQUEST),
    SUPPLIER_ACTION_REQUIRED(5101, "Bulk action type cannot be blank", HttpStatus.BAD_REQUEST),
    SUPPLIER_STATUS_REQUIRED(5102, "Supplier status cannot be blank", HttpStatus.BAD_REQUEST),
    SUPPLIER_CODE_REQUIRED(5103, "Supplier code cannot be blank", HttpStatus.BAD_REQUEST),
    SUPPLIER_NAME_REQUIRED(5104, "Supplier name cannot be blank", HttpStatus.BAD_REQUEST),
    SUPPLIER_NOT_FOUND(5150, "Supplier not found", HttpStatus.NOT_FOUND),
    SUPPLIER_EXISTED(5151, "Supplier already existed", HttpStatus.CONFLICT),
    SUPPLIER_BULK_ACTION_REQUIRED_IDS(5158, "At least one supplier must be selected", HttpStatus.BAD_REQUEST),
    SUPPLIER_INVALID_BULK_ACTION(5159, "The requested bulk action is invalid", HttpStatus.BAD_REQUEST),

    // Serial (5200 - 5259)
    SERIAL_STATUS_REQUIRED(5200, "Serial status cannot be blank", HttpStatus.BAD_REQUEST),
    SERIAL_NOTES_REQUIRED(5201, "Serial notes cannot be blank", HttpStatus.BAD_REQUEST),
    SERIAL_NOT_FOUND(5250, "Serial number not found", HttpStatus.NOT_FOUND),
    SERIAL_ALREADY_ALLOCATED(5251, "Serial number already allocated", HttpStatus.CONFLICT),

    // Import Order (5300 - 5359)
    IMPORT_ORDER_WAREHOUSE_REQUIRED(5300, "Warehouse is required", HttpStatus.BAD_REQUEST),
    IMPORT_ORDER_SUPPLIER_REQUIRED(5301, "Supplier is required", HttpStatus.BAD_REQUEST),
    IMPORT_ORDER_CODE_REQUIRED(5302, "Import order code cannot be blank", HttpStatus.BAD_REQUEST),
    IMPORT_DETAIL_VARIANT_REQUIRED(5303, "Product variant is required", HttpStatus.BAD_REQUEST),
    IMPORT_DETAIL_SERIAL_CODE_REQUIRED(5304, "Serial code cannot be blank", HttpStatus.BAD_REQUEST),
    IMPORT_DETAIL_UNIT_PRICE_REQUIRED(5305, "Unit price is required", HttpStatus.BAD_REQUEST),
    IMPORT_ORDER_NOT_FOUND(5350, "Import order not found", HttpStatus.NOT_FOUND),
    IMPORT_ORDER_EXISTED(5351, "Import order code already existed", HttpStatus.CONFLICT),
    IMPORT_ORDER_NOT_PENDING(5352, "Import order is not in PENDING status", HttpStatus.BAD_REQUEST),
    IMPORT_ORDER_EMPTY(5353, "Import order has no items", HttpStatus.BAD_REQUEST),
    IMPORT_DETAIL_NOT_FOUND(5354, "Import detail not found", HttpStatus.NOT_FOUND),

    // Transfer Order (5400 - 5459)
    TRANSFER_ORDER_FROM_WAREHOUSE_REQUIRED(5400, "Source warehouse is required", HttpStatus.BAD_REQUEST),
    TRANSFER_ORDER_TO_WAREHOUSE_REQUIRED(5401, "Destination warehouse is required", HttpStatus.BAD_REQUEST),
    TRANSFER_ORDER_CODE_REQUIRED(5402, "Transfer order code cannot be blank", HttpStatus.BAD_REQUEST),
    TRANSFER_DETAIL_SERIAL_REQUIRED(5403, "Serial number is required", HttpStatus.BAD_REQUEST),
    TRANSFER_ORDER_NOT_FOUND(5450, "Transfer order not found", HttpStatus.NOT_FOUND),
    TRANSFER_ORDER_EXISTED(5451, "Transfer order code already existed", HttpStatus.CONFLICT),
    TRANSFER_ORDER_NOT_PENDING(5452, "Transfer order is not in PENDING status", HttpStatus.BAD_REQUEST),
    TRANSFER_ORDER_NOT_IN_TRANSIT(5453, "Transfer order is not in IN_TRANSIT status", HttpStatus.BAD_REQUEST),
    TRANSFER_ORDER_EMPTY(5454, "Transfer order has no items", HttpStatus.BAD_REQUEST),
    TRANSFER_ORDER_SAME_WAREHOUSE(5455, "Source and destination warehouses cannot be the same", HttpStatus.BAD_REQUEST),
    TRANSFER_ORDER_CANNOT_CANCEL(5456, "Transfer order cannot be cancelled in current status", HttpStatus.BAD_REQUEST),
    TRANSFER_SERIAL_NOT_IN_STOCK(5457, "Serial is not in stock", HttpStatus.BAD_REQUEST),
    TRANSFER_SERIAL_WRONG_WAREHOUSE(5458, "Serial is not in the source warehouse", HttpStatus.BAD_REQUEST),
    TRANSFER_DETAIL_NOT_FOUND(5459, "Transfer detail not found", HttpStatus.NOT_FOUND),

    // Inventory Check (5500 - 5559)
    INVENTORY_CHECK_WAREHOUSE_REQUIRED(5500, "Warehouse is required", HttpStatus.BAD_REQUEST),
    INVENTORY_CHECK_CODE_REQUIRED(5501, "Inventory check code cannot be blank", HttpStatus.BAD_REQUEST),
    INVENTORY_CHECK_TYPE_REQUIRED(5502, "Check type cannot be blank", HttpStatus.BAD_REQUEST),
    INVENTORY_CHECK_ACTUAL_QTY_REQUIRED(5503, "Actual quantity is required", HttpStatus.BAD_REQUEST),
    INVENTORY_CHECK_ACTUAL_QTY_MIN(5504, "Actual quantity cannot be negative", HttpStatus.BAD_REQUEST),
    INVENTORY_CHECK_BATCH_EMPTY(5505, "Batch update items cannot be empty", HttpStatus.BAD_REQUEST),
    INVENTORY_CHECK_DETAIL_ID_REQUIRED(5506, "Detail ID is required", HttpStatus.BAD_REQUEST),
    INVENTORY_CHECK_NOT_FOUND(5550, "Inventory check not found", HttpStatus.NOT_FOUND),
    INVENTORY_CHECK_EXISTED(5551, "Inventory check code already existed", HttpStatus.CONFLICT),
    INVENTORY_CHECK_NOT_IN_PROGRESS(5552, "Inventory check is not in IN_PROGRESS status", HttpStatus.BAD_REQUEST),
    INVENTORY_CHECK_DETAIL_NOT_FOUND(5553, "Inventory check detail not found", HttpStatus.NOT_FOUND),

    // Cart (6000 - 6059)
    CART_ITEM_VARIANT_REQUIRED(6000, "Product variant is required", HttpStatus.BAD_REQUEST),
    CART_ITEM_QUANTITY_REQUIRED(6001, "Quantity is required", HttpStatus.BAD_REQUEST),
    CART_ITEM_QUANTITY_MIN(6002, "Quantity must be at least 1", HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND(6050, "Cart not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(6051, "Cart item not found", HttpStatus.NOT_FOUND),
    CART_EMPTY(6052, "Cart is empty", HttpStatus.BAD_REQUEST),

    // =================================================================
    // MODULE: ORDER (Range: 7000 - 7099)
    // =================================================================
    ORDER_SHIPPING_NAME_REQUIRED(7000, "Shipping name cannot be blank", HttpStatus.BAD_REQUEST),
    ORDER_SHIPPING_PHONE_REQUIRED(7001, "Shipping phone cannot be blank", HttpStatus.BAD_REQUEST),
    ORDER_SHIPPING_PROVINCE_REQUIRED(7002, "Shipping province cannot be blank", HttpStatus.BAD_REQUEST),
    ORDER_SHIPPING_DISTRICT_REQUIRED(7003, "Shipping district cannot be blank", HttpStatus.BAD_REQUEST),
    ORDER_SHIPPING_WARD_REQUIRED(7004, "Shipping ward cannot be blank", HttpStatus.BAD_REQUEST),
    ORDER_SHIPPING_ADDRESS_DETAIL_REQUIRED(7005, "Shipping address detail cannot be blank", HttpStatus.BAD_REQUEST),
    ORDER_PAYMENT_METHOD_REQUIRED(7006, "Payment method is required", HttpStatus.BAD_REQUEST),
    ORDER_CART_ITEM_IDS_REQUIRED(7007, "Cart item IDs cannot be empty", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_REQUIRED(7008, "Order status is required", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(7050, "Order not found", HttpStatus.NOT_FOUND),
    ORDER_INVALID_STATUS_TRANSITION(7051, "Invalid order status transition", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_CANCEL(7052, "Order cannot be cancelled in current status", HttpStatus.BAD_REQUEST),
    ORDER_INSUFFICIENT_STOCK(7053, "Insufficient stock for product variant", HttpStatus.BAD_REQUEST),

    // =================================================================
    // MODULE: NEWS (Range: 8000 - 8099)
    // =================================================================
    NEWS_TITLE_REQUIRED(8000, "News title cannot be blank", HttpStatus.BAD_REQUEST),
    NEWS_SLUG_REQUIRED(8001, "News slug cannot be blank", HttpStatus.BAD_REQUEST),
    NEWS_CONTENT_REQUIRED(8002, "News content cannot be blank", HttpStatus.BAD_REQUEST),
    NEWS_STATUS_REQUIRED(8003, "News status is required", HttpStatus.BAD_REQUEST),
    NEWS_NOT_FOUND(8050, "News not found", HttpStatus.NOT_FOUND),
    NEWS_SLUG_EXISTED(8051, "News slug already existed", HttpStatus.CONFLICT),
    NEWS_INVALID_STATUS_TRANSITION(8052, "Invalid news status transition", HttpStatus.BAD_REQUEST),
    NEWS_SELECTION_REQUIRED(8053, "At least one news must be selected", HttpStatus.BAD_REQUEST),
    NEWS_ACTION_REQUIRED(8054, "Bulk action is required", HttpStatus.BAD_REQUEST),
    NEWS_INVALID_BULK_ACTION(8055, "Invalid bulk action", HttpStatus.BAD_REQUEST),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatusCode statusCode;

}
