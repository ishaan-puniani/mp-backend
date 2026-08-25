/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.generic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImportRequestModel<T> {
    private T data;
    private String importHash;
}