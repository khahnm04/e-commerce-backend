package com.khahnm18.ecommercebackend.entity.product;

import java.util.List;

public record SpecificationItem(

    String name,
    List<String> values

) {}
