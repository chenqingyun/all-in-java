package com.chenqingyun.se;

import java.math.BigDecimal;

/**
 * @author chenqingyun
 * @date 2019-08-01 19:24.
 */
public class House {
    private String address;
    private BigDecimal price;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
