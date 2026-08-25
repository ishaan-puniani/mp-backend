/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection ="customers")
public class Customer extends BaseEntity {
    
  public String name;
  public String email;

}