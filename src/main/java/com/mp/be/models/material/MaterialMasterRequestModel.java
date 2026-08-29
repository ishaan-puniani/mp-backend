package com.mp.be.models.material;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Schema(name = "Material_RequestModel", description = "Material Master Query and Filter Request Model")
public class MaterialMasterRequestModel {
    private Map<String, Object> filter;
}
