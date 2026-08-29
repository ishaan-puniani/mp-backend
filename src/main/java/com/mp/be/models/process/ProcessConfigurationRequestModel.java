package com.mp.be.models.process;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Schema(name = "Process_ConfigurationRequestModel", description = "Process Configuration Query and Filter Request Model")
public class ProcessConfigurationRequestModel {
    private Map<String, Object> filter;
}
