package com.mp.be.models.machine;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class MachineMasterRequestModel {
    private Map<String, Object> filter;
}
