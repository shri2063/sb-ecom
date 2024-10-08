package com.ecommerce.sb_ecom.payload;

import lombok.Data;

import java.util.List;
@Data
public class ProductResponse
{
    private List<ProductDTO> content;
    private   int pageNum;
    private   int pageSize;
    private   long total;
    private   int totalPage;
    private  boolean lastPage;


}
