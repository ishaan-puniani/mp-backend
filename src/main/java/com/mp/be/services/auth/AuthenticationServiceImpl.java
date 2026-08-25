/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.auth;

import com.mp.be.database.entities.File;
import com.mp.be.database.entities.Profile;
import com.mp.be.database.repositories.FileRepository;
import com.mp.be.database.repositories.ProfileRepository;
import com.mp.be.database.repositories.TenantRepository;
import com.mp.be.models.auth.*;
import com.mp.be.database.entities.User;
import com.mp.be.database.repositories.UserRepository;
import com.mp.be.models.auth.AuthenticationRequest;
import com.mp.be.services.BrevoEmailService;
import com.mp.be.services.JwtService;
import com.mp.be.services.ServiceOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

	@Autowired
	private BrevoEmailService emailService;

	@Autowired
	private ProfileRepository userprofileRepository;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TenantRepository tenantRepository;

	@Override
	public String signIn(AuthenticationRequest authenticationRequest) {
		User user = userRepository.findByEmail(authenticationRequest.getEmail())
				.orElseThrow(() -> new BadCredentialsException("User not found"));

		if (user.getPassword() == null || !passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		UserDetails userDetails = new AuthUser(user);
		return jwtService.generateToken(userDetails);
	}

	@Override
	public String registerUser(User user) {
		User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
		if (existingUser != null) {
			if (existingUser.getPassword() != null) {
				throw new BadCredentialsException("User is already registered");
			}
			existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
			user = existingUser;
		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		User newUser = userRepository.save(user);
		return jwtService.generateToken(newUser);
	}

	@Override
	public User getUsers(String id) {
		return userRepository.findByEmail(id).orElse(null);
	}

	@Override
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public void deletUsers(String id) {
		userRepository.deleteById(id);
	}

	@Override
	public String changePassword(User currentUser, PasswordModel model) {
		User user = userRepository.findByEmail(currentUser.getEmail())
				.orElseThrow(() -> new BadCredentialsException("User not found"));
		if (passwordEncoder.matches(model.getOldPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(model.getNewPassword()));
			userRepository.save(user);
			return "Password Changed Successfully";
		} else {
			return "Failed to change password. Please check your credentials.";
		}
	}

	@Override
	public void sendPasswordResetEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new Exception("User not found"));

		String token = generateResetToken();
		user.setPasswordResetToken(token);
		user.setPasswordResetTokenExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));
		userRepository.save(user);

		String link = "http://localhost:3000/auth/password-reset?token=" + token;
		Map<String, Object> variables = new HashMap<>();
		variables.put("userName", user.getEmail());
		variables.put("link", link);
		emailService.sendEmail(user.getEmail(), "22", variables);

	}

	@Override
	public String resetPassword(ResetPasswordModel model) {
		User user = userRepository.findByPasswordResetToken(model.getToken())
				.orElseThrow(() -> new BadCredentialsException("User not found"));
		if (user != null) {
			user.setPassword(passwordEncoder.encode(model.getPassword()));
			userRepository.save(user);
			return "Password Reset Successfully";
		} else {
			return "Failed to change password. Please check your credentials.";
		}

	}

	@Override
	public void verifyEmail(String token, ServiceOptions options) {
		User currentUser = options.getCurrentUser();

		User user = userRepository.findByEmailVerificationToken(token)
				.orElseThrow(() -> new BadCredentialsException("Invalid email verification token"));

		if (currentUser != null && !currentUser.getId().equals(user.getId())) {
			throw new BadCredentialsException("Signed in as wrong user: " + currentUser.getEmail() + " instead of " + user.getEmail());
		}

		user.setEmailVerified(true);
		userRepository.save(user);
	}

	@Override
	public void sendEmailAddressVerificationEmail( ServiceOptions options) {

		try {
			User user = userRepository.findByEmail(options.getCurrentUser().getEmail())
					.orElseThrow(() -> new BadCredentialsException("User not found"));

			String token = generateVerificationToken(user);
			String link = "http://localhost:3000/auth/verify-email?token=" + token;

			Map<String, Object> variables = new HashMap<>();
			variables.put("link", link);

			emailService.sendEmail(user.getEmail(), "17", variables);
		} catch (Exception e) {
			throw new BadCredentialsException("Failed to send verification email");
		}
	}

	@Override
	public Profile updateProfile(String id, Profile data) {
		Profile record = null;
		saveFileModels(data.getAvatars());
		if(id!=null){
			record = userprofileRepository.findById(id).orElse(null);
			if (record != null) {
				record.setAvatars(new ArrayList<>(data.getAvatars()));
				// Copy properties from 'data' to 'record', ignoring null values
				BeanUtils.copyProperties(data, record, "id","avatars");

				// Save the updated record
				userprofileRepository.save(record);}
		}
		else {
			record= data;
			userprofileRepository.save(record);
		}
		return record;
	}

	private String generateResetToken() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] tokenBytes = new byte[20];
		secureRandom.nextBytes(tokenBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
	}

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		user.setEmailVerificationToken(token);
		user.setEmailVerificationTokenExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));
		userRepository.save(user);
		return token;
	}

	private void saveFileModels(List<File> fileModels) {
		if (fileModels != null) {
			for (File fileModel : fileModels) {
				if (fileRepository.findById(fileModel.id)== null) {
					fileRepository.save(fileModel);
				}
			}
		}
	}

}
