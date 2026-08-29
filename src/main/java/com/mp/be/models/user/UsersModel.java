/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "User_BatchListModel", description = "Batch Users Creation Model")
public class UsersModel {
    public List<String> emails;
    public List<String> roles;

}
