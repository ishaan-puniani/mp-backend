/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.entities;

import com.mp.be.database.enumerator.Categories;
import com.mp.be.database.enumerator.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
@Document(collection ="products")
public class Product extends BaseEntity {
    
  public String name;
  public double pricing;

  public Integer availableStock;

  @JsonFormat(pattern = "yyyy-MM-dd")
  public Date availableFrom;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  public LocalDateTime availableUpto;

  private Boolean isActive;

  private Status status;

  private List<Categories> categories;

  @Field(targetType = FieldType.OBJECT_ID)
  private String addBy;

  private List<File> image;

  private List<File> uploadedFile;

  @Field(targetType = FieldType.OBJECT_ID)
  private List<String> approvers;


}