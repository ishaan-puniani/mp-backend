/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.auth;

import com.mp.be.database.entities.Profile;
import com.mp.be.database.entities.User;
import com.mp.be.models.auth.AuthenticationRequest;
import com.mp.be.models.auth.PasswordModel;
import com.mp.be.models.auth.ResetPasswordModel;
import com.mp.be.services.ServiceOptions;

public interface AuthenticationService {

    public String signIn(AuthenticationRequest authenticationRequest);

	public String registerUser(User user);

	public User getUsers(String id);

	public Iterable<User> getAllUsers();

	public void deletUsers(String id);

	String changePassword(User currentUser, PasswordModel model);

    void sendPasswordResetEmail(String email) throws Exception;

	String resetPassword( ResetPasswordModel model);

	void verifyEmail(String token, ServiceOptions serviceOptions);

	void sendEmailAddressVerificationEmail( ServiceOptions serviceOptions);

    Profile updateProfile(String id, Profile profile);
}
