package com.mp.be.models.machine;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Schema(name = "Machine_RequestModel", description = "Machine Master Query and Filter Request Model")
public class MachineMasterRequestModel {
    private Map<String, Object> filter;
}
