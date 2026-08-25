/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@SecurityScheme(name = "mp-backend api", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@SpringBootApplication
@EnableMongoAuditing
public class MpJavaMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpJavaMongoApplication.class, args);
	}


}
