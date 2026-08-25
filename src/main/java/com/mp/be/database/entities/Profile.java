package com.mp.be.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection ="profiles")
public class Profile extends BaseEntity{

    private String tenantId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @DBRef
    private List<File> avatars;


}
