package com.mp.be.models.material;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class MaterialMasterRequestModel {
    private Map<String, Object> filter;
}
