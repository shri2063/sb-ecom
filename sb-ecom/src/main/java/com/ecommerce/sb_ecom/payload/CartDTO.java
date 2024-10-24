package com.ecommerce.sb_ecom.payload;

import java.util.List;

public class CartDTO
{
    private Long id;
    private Long userId;
    private List<CartLineItemDTO> cartLineItemDTOList;
}
