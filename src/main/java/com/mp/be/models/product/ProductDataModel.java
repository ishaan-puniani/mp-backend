/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.product;
import com.mp.be.database.entities.Product;

import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Product_DataModel", description = "Product Single Data Wrapper")
public class ProductDataModel {

    public Product data;
}