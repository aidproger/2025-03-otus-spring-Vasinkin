package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//http://localhost:8080

//admin/admin    Author, Genre, Book, Comment - <all permissions>; User - <read>
//login_1/pass_1  Author, Genre - <read>; Book, Comment - <read, create>
//login_2/pass_2  Author, Genre, Book - <read>; Comment - <read, create>
//login_3/pass_3  Author, Genre, Book, Comment - <read>
//login_4/pass_4 non permissions

//Под логином login_1/pass_1 созданы BookTitle_1, BookTitle_2, BookTitle_3, comment_1 - comment_7
//Под логином login_2/pass_2 созданы comment_8, comment_9

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }

}
