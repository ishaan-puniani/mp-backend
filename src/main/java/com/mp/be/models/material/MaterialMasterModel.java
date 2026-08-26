package com.mp.be.models.material;

import com.mp.be.database.entities.File;
import com.mp.be.database.enumerator.ItemStatus;
import com.mp.be.database.enumerator.MaterialType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class MaterialMasterModel {
    private String id;
    private String name;
    private String code;
    private MaterialType type;
    private String baseUnit;
    private Double density;
    private Double standardCost;
    private String description;
    private ItemStatus status;
    private Map<String, Object> attributes;

    private List<File> image;
    private List<File> uploadedFile;
}
