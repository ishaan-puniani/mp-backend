package com.mp.be.models.material;

import com.mp.be.database.entities.MaterialMaster;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(name = "Material_DataModel", description = "Material Master Single Data Wrapper")
public class MaterialMasterDataModel {
    public MaterialMaster data;
}
