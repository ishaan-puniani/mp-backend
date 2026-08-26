package com.mp.be.models.process;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ProcessConfigurationRequestModel {
    private Map<String, Object> filter;
}
