package com.mp.be.models.auth;

import com.mp.be.database.entities.Profile;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Auth_ProfileDataModel", description = "Current User Profile Data Wrapper")
public class ProfileDataModel {

    public Profile data;
}
