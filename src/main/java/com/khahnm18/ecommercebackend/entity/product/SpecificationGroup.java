package com.khahnm18.ecommercebackend.entity.product;

import java.util.List;

public record SpecificationGroup(

    String name,
    List<SpecificationItem> items

) {}
