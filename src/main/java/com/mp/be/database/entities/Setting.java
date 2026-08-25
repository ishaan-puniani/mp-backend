/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection ="settings")
public class Setting extends BaseEntity {
    
  public String theme;
  private List<File> logos;

  private List<File> backgroundImages;
}