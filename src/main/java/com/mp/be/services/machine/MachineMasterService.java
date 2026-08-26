package com.mp.be.services.machine;

import com.mp.be.database.entities.MachineMaster;
import com.mp.be.models.machine.MachineMasterModel;
import com.mp.be.models.machine.MachineMasterRequestModel;
import com.mp.be.services.ServiceOptions;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MachineMasterService {

    Page<MachineMasterModel> findAndCountAll(ServiceOptions serviceOptions,
                                             MachineMasterRequestModel requestModel,
                                             Optional<Integer> limit,
                                             Optional<Integer> offset,
                                             Optional<String> orderBy);

    List<MachineMasterModel> findAll(ServiceOptions serviceOptions);

    MachineMasterModel find(ServiceOptions serviceOptions, String id);

    MachineMasterModel create(ServiceOptions serviceOptions, MachineMaster data);

    MachineMasterModel update(ServiceOptions serviceOptions, String id, MachineMaster data);

    void delete(ServiceOptions serviceOptions, String id);

    MachineMaster importData(ServiceOptions serviceOptions, MachineMaster data, String importHash);
}
