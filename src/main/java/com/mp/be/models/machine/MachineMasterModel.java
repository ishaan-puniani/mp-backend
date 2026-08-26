package com.mp.be.models.machine;

import com.mp.be.database.entities.File;
import com.mp.be.database.enumerator.ItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class MachineMasterModel {
    private String id;
    private String name;
    private String code;
    private String shopName;
    private String machineType;
    private Double capacity;
    private String capacityUnit;
    private Double powerRating;
    private String location;
    private ItemStatus status;
    private Map<String, Object> parameters;

    private List<File> image;
    private List<File> uploadedFile;
}
