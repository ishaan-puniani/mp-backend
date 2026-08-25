package com.mp.be.api.user;

import com.mp.be.database.entities.User;
import com.mp.be.models.AutoComplete;
import com.mp.be.models.user.*;
import com.mp.be.models.generic.ListResponseModel;
import com.mp.be.services.user.UserService;
import com.mp.be.services.ServiceOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAndCountAll() {
        UserModel userModel = new UserModel();
        Page<UserModel> page = new PageImpl<>(Collections.singletonList(userModel));

        // Test success scenario
        when(userService.findAndCountAll(any(ServiceOptions.class), any(UserRequestModel.class), any(), any(), any()))
                .thenReturn(page);
        ResponseEntity<ListResponseModel<UserModel>> response = userController.findAndCountAll(request, new UserRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ListResponseModel<UserModel> responseBody = response.getBody();
        assertEquals(1, responseBody.rows.size());

        // Test failure scenario
        when(userService.findAndCountAll(any(ServiceOptions.class), any(UserRequestModel.class), any(), any(), any()))
                .thenThrow(new RuntimeException("Service error"));
        try {
            response = userController.findAndCountAll(request, new UserRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        } catch (RuntimeException e) {
            assertEquals("Service error", e.getMessage());
        }
    }

    @Test
    void testFindAndCountAllWithEmptyResult() {
        Page<UserModel> emptyPage = new PageImpl<>(Collections.emptyList());

        // Test empty result scenario
        when(userService.findAndCountAll(any(ServiceOptions.class), any(UserRequestModel.class), any(), any(), any()))
                .thenReturn(emptyPage);
        ResponseEntity<ListResponseModel<UserModel>> response = userController.findAndCountAll(request, new UserRequestModel(), Optional.of(0), Optional.of(10), Optional.of("id"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ListResponseModel<UserModel> responseBody = response.getBody();
        assertEquals(0, responseBody.rows.size());
    }

    @Test
    void testFind() {
        UserModel userModel = new UserModel();

        // Test success scenario
        when(userService.find(any(ServiceOptions.class), any(String.class))).thenReturn(userModel);
        UserModel response = userController.find(request, "1");
        assertEquals(userModel, response);

        // Test failure scenario
        when(userService.find(any(ServiceOptions.class), any(String.class))).thenThrow(new RuntimeException("User not found"));
        try {
            response = userController.find(request, "1");
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    void testCreate() {
        UsersDataModel usersDataModel = new UsersDataModel();
        UsersModel usersModel = new UsersModel();
        usersModel.emails = Collections.singletonList("test@example.com");
        usersDataModel.data = usersModel;

        // Test success scenario
        when(userService.create(any(ServiceOptions.class), any(UserModel.class))).thenReturn(new UserModel());
        ResponseEntity<String> response = userController.create(request, usersDataModel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User created successfully", response.getBody());

        // Test failure scenario
        when(userService.create(any(ServiceOptions.class), any(UserModel.class))).thenThrow(new RuntimeException("Invalid data"));
        try {
            response = userController.create(request, usersDataModel);
        } catch (RuntimeException e) {
            assertEquals("Invalid data", e.getMessage());
        }
    }

    @Test
    void testUpdate() {
        UserDataModel userDataModel = new UserDataModel();
        UserModel userModel = new UserModel();
        userModel.setId("1");
        userDataModel.data = userModel;

        // Test success scenario
        when(userService.update(any(ServiceOptions.class), eq("1"), any(UserModel.class))).thenReturn(userModel);
        ResponseEntity<UserModel> response = userController.update(request, userDataModel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userModel, response.getBody());

        // Test failure scenario
        when(userService.update(any(ServiceOptions.class), eq("1"), any(UserModel.class))).thenThrow(new RuntimeException("Invalid data"));
        try {
            response = userController.update(request, userDataModel);
        } catch (RuntimeException e) {
            assertEquals("Invalid data", e.getMessage());
        }
    }

    @Test
    void testDelete() {
        List<String> ids = Collections.singletonList("1");

        // Test success scenario
        doNothing().when(userService).delete(any(String.class));
        userController.delete(ids);

        // Test failure scenario
        doThrow(new RuntimeException("Invalid ID")).when(userService).delete(any(String.class));
        try {
            userController.delete(ids);
        } catch (RuntimeException e) {
            assertEquals("Invalid ID", e.getMessage());
        }
    }

    @Test
    void testAutocomplete() {
        List<User> users = Collections.singletonList(new User());

        // Test success scenario
        when(userService.findAll(any(ServiceOptions.class))).thenReturn(users);
        List<AutoComplete> response = userController.autocomplete(request, "query", 10);
        assertEquals(1, response.size());

        // Test failure scenario
        when(userService.findAll(any(ServiceOptions.class))).thenThrow(new RuntimeException("Service error"));
        try {
            response = userController.autocomplete(request, "query", 10);
        } catch (RuntimeException e) {
            assertEquals("Service error", e.getMessage());
        }
    }
} 