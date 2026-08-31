/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.user;

import com.mp.be.database.entities.User;
import com.mp.be.models.user.UserModel;
import com.mp.be.models.user.UserMeModel;
import com.mp.be.services.ServiceOptions;
import com.mp.be.models.user.UserRequestModel;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface UserService {

    public Page<UserModel> findAndCountAll(ServiceOptions serviceOptions,
                                      UserRequestModel requestModel,
                                      Optional<Integer> limit,
                                      Optional<Integer> offset,
                                      Optional <String> orderBy);

    public UserModel find(ServiceOptions serviceOptions, String id);

    public UserMeModel findMe(ServiceOptions serviceOptions, String id);

    public UserModel findByEmail(ServiceOptions serviceOptions, String email);

    public UserModel create(ServiceOptions serviceOptions, UserModel data);

    public void delete(String id);

    public UserModel update(ServiceOptions serviceOptions, String id, UserModel data);

    List<User> findAll(ServiceOptions serviceOptions);
}
