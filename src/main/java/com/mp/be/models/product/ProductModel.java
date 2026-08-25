/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.product;

import com.mp.be.database.entities.File;
import com.mp.be.database.enumerator.Categories;
import com.mp.be.database.enumerator.Status;
import com.mp.be.models.file.UserAddApproversByModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ProductModel {

      private String id;
      private String name;
      private double pricing;
      private Integer availableStock;
      @JsonFormat(pattern = "yyyy-MM-dd")
      private Date availableFrom;
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      private LocalDateTime availableUpto;
      private Boolean isActive;
      private Status status;
      private List<Categories> categories;
      private UserAddApproversByModel addBy;
      private List<File> image;
      private List<File> uploadedFile;
      private List<UserAddApproversByModel> approvers;
}