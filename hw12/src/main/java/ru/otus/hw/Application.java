package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

//http://localhost:8080
//login_1/pass_1
//login_2/pass_2
//login_3/pass_3
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws SQLException {

        SpringApplication.run(Application.class, args);

    }

}
