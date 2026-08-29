/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.generic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(name = "Common_ImportRequestModel", description = "Generic Bulk Data Import Request Model")
public class ImportRequestModel<T> {
    private T data;
    private String importHash;
}