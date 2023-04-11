package com.vnet.solution.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SalesData implements Serializable {

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate salesDate;
    private String storeName;
    private String productName;
    private int salesUnit;
    private BigDecimal salesRevenue;

    public SalesData() {
    }

    public SalesData(LocalDate salesDate, String storeName, String productName, int salesUnit, BigDecimal salesRevenue) {
        this.salesDate = salesDate;
        this.storeName = storeName;
        this.productName = productName;
        this.salesUnit = salesUnit;
        this.salesRevenue = salesRevenue;
    }

    public SalesData(String storeName, String productName, int salesUnit, BigDecimal salesRevenue) {
        this.storeName = storeName;
        this.productName = productName;
        this.salesUnit = salesUnit;
        this.salesRevenue = salesRevenue;
    }

    public LocalDate getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(LocalDate salesDate) {
        this.salesDate = salesDate;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSalesUnit() {
        return salesUnit;
    }

    public void setSalesUnit(int salesUnit) {
        this.salesUnit = salesUnit;
    }

    public BigDecimal getSalesRevenue() {
        return salesRevenue;
    }

    public void setSalesRevenue(BigDecimal salesRevenue) {
        this.salesRevenue = salesRevenue;
    }

    @Override
    public String toString() {
        return "SalesData{" +
                "salesDate=" + salesDate +
                ", storeName='" + storeName + '\'' +
                ", productName='" + productName + '\'' +
                ", salesUnit=" + salesUnit +
                ", salesRevenue=" + salesRevenue +
                '}';
    }
}
