package com.mp.be.services.process;

import com.mp.be.database.entities.process.ProcessConfiguration;
import com.mp.be.database.enumerator.ProcessStatus;
import com.mp.be.models.process.ProcessConfigurationModel;
import com.mp.be.models.process.ProcessConfigurationRequestModel;
import com.mp.be.services.ServiceOptions;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProcessConfigurationService {

    Page<ProcessConfigurationModel> findAndCountAll(ServiceOptions serviceOptions,
                                                    ProcessConfigurationRequestModel requestModel,
                                                    Optional<Integer> limit,
                                                    Optional<Integer> offset,
                                                    Optional<String> orderBy);

    List<ProcessConfigurationModel> findAll(ServiceOptions serviceOptions);

    ProcessConfigurationModel find(ServiceOptions serviceOptions, String id);

    ProcessConfigurationModel create(ServiceOptions serviceOptions, ProcessConfiguration data);

    ProcessConfigurationModel update(ServiceOptions serviceOptions, String id, ProcessConfiguration data);

    ProcessConfigurationModel updateStatus(ServiceOptions serviceOptions, String id, ProcessStatus status);

    void delete(ServiceOptions serviceOptions, String id);

    ProcessConfiguration importData(ServiceOptions serviceOptions, ProcessConfiguration data, String importHash);

    ProcessConfigurationModel seedIceCreamProcess(ServiceOptions serviceOptions);
}
