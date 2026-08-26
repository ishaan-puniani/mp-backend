package com.mp.be.services.material;

import com.mp.be.database.entities.MaterialMaster;
import com.mp.be.models.material.MaterialMasterModel;
import com.mp.be.models.material.MaterialMasterRequestModel;
import com.mp.be.services.ServiceOptions;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MaterialMasterService {

    Page<MaterialMasterModel> findAndCountAll(ServiceOptions serviceOptions,
                                              MaterialMasterRequestModel requestModel,
                                              Optional<Integer> limit,
                                              Optional<Integer> offset,
                                              Optional<String> orderBy);

    List<MaterialMasterModel> findAll(ServiceOptions serviceOptions);

    MaterialMasterModel find(ServiceOptions serviceOptions, String id);

    MaterialMasterModel create(ServiceOptions serviceOptions, MaterialMaster data);

    MaterialMasterModel update(ServiceOptions serviceOptions, String id, MaterialMaster data);

    void delete(ServiceOptions serviceOptions, String id);

    MaterialMaster importData(ServiceOptions serviceOptions, MaterialMaster data, String importHash);
}
