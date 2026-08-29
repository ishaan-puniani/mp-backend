/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.generic;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "Common_ListResponseModel", description = "Generic Paginated List Response Model")
public class ListResponseModel<T> {
   public List<T> rows;
   public long count;
}
