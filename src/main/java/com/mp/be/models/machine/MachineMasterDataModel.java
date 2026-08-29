package com.mp.be.models.machine;

import com.mp.be.database.entities.MachineMaster;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(name = "Machine_DataModel", description = "Machine Master Single Data Wrapper")
public class MachineMasterDataModel {
    public MachineMaster data;
}
