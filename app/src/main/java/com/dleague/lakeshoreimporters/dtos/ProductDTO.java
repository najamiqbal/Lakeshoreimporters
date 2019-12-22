package com.dleague.lakeshoreimporters.dtos;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {
    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVariantsId() {
        return variantsId;
    }

    public void setVariantsId(String variantsId) {
        this.variantsId = variantsId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductPriceMin() {
        return productPriceMin;
    }

    public void setProductPriceMin(String productPriceMin) {
        this.productPriceMin = productPriceMin;
    }

    public String getProductPriceMax() {
        return productPriceMax;
    }

    public void setProductPriceMax(String productPriceMax) {
        this.productPriceMax = productPriceMax;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean isAvailableForSale() {
        return availableForSale;
    }

    public void setAvailableForSale(boolean availableForSale) {
        this.availableForSale = availableForSale;
    }

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    private String cursor;
    String handle;
    String productId;
    String variantsId;
    String productName;
    String description;
    String productPriceMin;
    String productPriceMax;
    String currencyCode;
    boolean  availableForSale;
    List<String> imagesUrl;
}
