package com.khahnm04.ecommerce.modules.order.entity;

import com.khahnm04.ecommerce.modules.branch.entity.Branch;
import com.khahnm04.ecommerce.modules.inventory.entity.Warehouse;
import com.khahnm04.ecommerce.modules.user.entity.Customer;
import com.khahnm04.ecommerce.modules.user.entity.Employee;
import com.khahnm04.ecommerce.modules.order.enums.OrderStatus;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Order extends BaseEntity<Long> {

    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(name = "shipping_name", nullable = false)
    private String shippingName;

    @Column(name = "shipping_phone", nullable = false)
    private String shippingPhone;

    @Column(name = "shipping_province", nullable = false)
    private String shippingProvince;

    @Column(name = "shipping_district", nullable = false)
    private String shippingDistrict;

    @Column(name = "shipping_ward", nullable = false)
    private String shippingWard;

    @Column(name = "shipping_address_detail", nullable = false)
    private String shippingAddressDetail;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "required_date")
    private LocalDate requiredDate;

    @Column(name = "shipped_date")
    private LocalDate shippedDate;

}
