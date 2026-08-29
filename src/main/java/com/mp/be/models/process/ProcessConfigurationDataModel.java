package com.mp.be.models.process;

import com.mp.be.database.entities.process.ProcessConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(name = "Process_ConfigurationDataModel", description = "Process Configuration Single Data Wrapper")
public class ProcessConfigurationDataModel {
    public ProcessConfiguration data;
}
