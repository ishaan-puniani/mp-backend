/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.models.tenant;

import com.mp.be.database.entities.Tenant;

import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Tenant_DataModel", description = "Tenant Organization Single Data Wrapper")
public class TenantDataModel {

    public Tenant data;
}