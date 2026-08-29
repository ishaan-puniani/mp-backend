package com.mp.be.models.machine;

import com.mp.be.database.entities.File;
import com.mp.be.database.enumerator.ItemStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Setter
@Getter
@Schema(name = "Machine_Model", description = "Machine & Workcenter Master Model")
public class MachineMasterModel {
    private String id;
    private String name;
    private String code;
    private String shopName;
    private String machineType;
    private Double capacity;
    private String capacityUnit;
    private ItemStatus status;
    private Map<String, Object> parameters;

    private List<File> image;
    private List<File> uploadedFile;
}
