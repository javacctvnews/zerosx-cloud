package com.zerosx.api.examples.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BusinessDTO {

    private String userId;

    private String commodityCode;

    private String name;

    private Integer count;

    private BigDecimal amount;

}
