package com.khahnm04.ecommerce.dto.projection;

import com.khahnm04.ecommerce.common.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderProjection {

    Long getId();
    String getCode();
    BigDecimal getSubTotal();
    BigDecimal getDiscount();
    BigDecimal getTotal();
    OrderStatus getStatus();
    String getShippingName();
    String getShippingPhone();
    String getShippingProvince();
    String getShippingDistrict();
    String getShippingWard();
    String getShippingAddressDetail();
    String getNote();

    @Value("#{target.customer.fullName}")
    String getCustomerFullName();

    LocalDateTime getExpectedDeliveryDate();
    LocalDateTime getCreatedAt();

}
