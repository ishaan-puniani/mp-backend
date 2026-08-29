package com.mp.be.models.product;

import com.mp.be.database.entities.File;
import com.mp.be.database.enumerator.ItemStatus;
import com.mp.be.models.file.UserAddApproversByModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Schema(name = "Product_Model", description = "Commercial Finished Product / SKU Model")
public class ProductModel {

    @Schema(description = "Primary unique identifier", example = "64b0f9c2a1e45c001f3b8901")
    private String id;

    @Schema(description = "Product commercial name", example = "Vanilla Choco-Nut Ice Cream Tub")
    private String name;

    @Schema(description = "Unique product catalog code", example = "PROD-ICECREAM-01")
    private String code;

    @Schema(description = "Internal part/item code", example = "PART-IC-VN-500")
    private String partCode;

    @Schema(description = "Stock Keeping Unit", example = "SKU-IC-VAN-500ML")
    private String sku;

    @Schema(description = "Product description and packaging details")
    private String description;

    @Schema(description = "Product category", example = "Dairy & Ice Creams")
    private String category;

    @Schema(description = "Brand name", example = "Frosty Delights")
    private String brand;

    @Schema(description = "EAN/UPC Barcode", example = "8901234567890")
    private String barcode;

    @Schema(description = "Harmonized System Nomenclature (HSN) Tax Code", example = "21050000")
    private String hsnCode;

    @Schema(description = "GST tax rate percentage", example = "18.0")
    private Double gstRate;

    @Schema(description = "Base sales measurement unit", example = "pcs")
    private String baseUnit;

    @Schema(description = "Selling / Wholesale price", example = "120.0")
    private double pricing;

    @Schema(description = "Maximum Retail Price (MRP)", example = "150.0")
    private Double mrp;

    @Schema(description = "Standard BOM direct cost price", example = "78.5")
    private Double costPrice;

    @Schema(description = "Unit net weight", example = "250.0")
    private Double weight;

    @Schema(description = "Weight unit", example = "g")
    private String weightUnit;

    @Schema(description = "Shelf life in days", example = "180")
    private Integer shelfLifeDays;

    @Schema(description = "Recommended storage temperature", example = "-18°C to -22°C")
    private String storageTemperature;

    @Schema(description = "Reorder minimum stock level", example = "500.0")
    private Double minStockLevel;

    @Schema(description = "Maximum storage threshold stock level", example = "10000.0")
    private Double maxStockLevel;

    @Schema(description = "Lifecycle status (ACTIVE, INACTIVE, ARCHIVED, DRAFT)", example = "ACTIVE")
    private ItemStatus status;

    @Schema(description = "Associated SOP Manufacturing Process Configuration Code", example = "PROC-ICECREAM-01")
    private String processConfigCode;

    @Schema(description = "User who added this product")
    private UserAddApproversByModel addBy;

    @Schema(description = "Flexible custom specifications (Nutritional facts, allergen tags, etc.)")
    private Map<String, Object> specifications;

    @Schema(description = "Product preview images")
    private List<File> image;

    @Schema(description = "Supporting documentation and lab certificates")
    private List<File> uploadedFile;

    private Date createdAt;
    private Date updatedAt;
}