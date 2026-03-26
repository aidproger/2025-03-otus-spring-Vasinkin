package ru.otus.hw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

//http://localhost:8081
//admin/admin

@SpringBootApplication
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws SQLException {

        loadPostgresPasswordFromDockerSecret();

        SpringApplication.run(Application.class, args);

        LOG.info("Service hw19-authentication started");

    }

    private static void loadPostgresPasswordFromDockerSecret() {
        try {
            String path = "/run/secrets/postgres_password";
            String pass = new String(Files.readAllBytes(Paths.get(path)));
            System.setProperty("POSTGRES_PASSWORD", pass);
            LOG.info("Read postgres connection info from path:{}", path);
        } catch (Exception e) {
            LOG.error("Error reading docker secret");
        }

    }

}
