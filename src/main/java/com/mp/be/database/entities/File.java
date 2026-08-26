package com.mp.be.database.entities;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Document(collection = "mp_file")
public class File extends BaseEntity{

    private String name;

    private Long sizeInBytes;

    private String privateUrl;

    private String publicUrl;

    private String downloadUrl;

    @Field("new")
    private boolean isnew = true;

}