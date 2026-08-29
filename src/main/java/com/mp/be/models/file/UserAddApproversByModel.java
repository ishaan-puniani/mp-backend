package com.mp.be.models.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "File_UserApproverModel", description = "File Document Approver Reference")
public class UserAddApproversByModel {
    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;
}
