package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.OrderFilterRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.OrderResponse;
import com.khahnm04.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders(
            @Valid @ModelAttribute OrderFilterRequest filter
    ) {
        log.info("Admin fetching all orders with filter: {}", filter);
        PageResponse<OrderResponse> pageResponse = orderService.findAll(filter);
        return ApiResponse.<List<OrderResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all orders successful")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrderDetail(
            @PathVariable Long id
    ) {
        log.info("Admin fetching order detail: {}", id);
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.getById(id))
                .message("Get order detail successful")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @Valid @RequestBody BulkActionRequest request
    ) {
        orderService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

    @GetMapping("/{id}/print")
    public ResponseEntity<byte[]> printOrder(
            @PathVariable Long id
    ) {
        log.info("Admin printing invoice for order: {}", id);
        byte[] pdf = orderService.generateOrderInvoice(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"invoice-" + id + ".pdf\"")
                .body(pdf);
    }

}
