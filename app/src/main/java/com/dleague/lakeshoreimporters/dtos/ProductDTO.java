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
    String cursor;
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
