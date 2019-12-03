package com.dleague.lakeshoreimporters.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LastOrderItemDTO {
    String title;
    String price;
    int quantity;
    String imagesUrl;
}
