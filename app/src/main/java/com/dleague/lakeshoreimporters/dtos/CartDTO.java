package com.dleague.lakeshoreimporters.dtos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity(tableName = "CartDTO")
public class CartDTO {

    @PrimaryKey(autoGenerate = true)
    Long id;

    @ColumnInfo(name = "PRODUCT_ID")
    public String productID;

    @ColumnInfo(name = "VARIANT_ID")
    public String variantsId;

    @ColumnInfo(name = "CURSOR")
    public String cursor;

    @ColumnInfo(name = "TITLE")
    public String title;

    @ColumnInfo(name = "DESCRIPTION")
    public String description;

    @ColumnInfo(name = "MIN_PRICE")
    public String minPirce;

    @ColumnInfo(name = "MAX_PRICE")
    public String maxPrice;

    @ColumnInfo(name = "IMAGE_URL")
    public String imageURL;

    @ColumnInfo(name = "CURRENCY")
    public String currenceyCode;

    @ColumnInfo(name = "QUANTITY")
    public int quantity;

    public CartDTO() {
    }

    @Ignore
    public CartDTO(String productID, String variantsId, String cursor, String title,
                   String description, String minPirce, String maxPrice, String imageURL, String currenceyCode, int quantity) {
        this.productID = productID;
        this.variantsId = variantsId;
        this.cursor = cursor;
        this.title = title;
        this.description = description;
        this.minPirce = minPirce;
        this.maxPrice = maxPrice;
        this.imageURL = imageURL;
        this.currenceyCode = currenceyCode;
        this.quantity = quantity;
    }


}
