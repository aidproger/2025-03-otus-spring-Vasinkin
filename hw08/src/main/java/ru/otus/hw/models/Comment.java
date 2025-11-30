package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String text;

    @Field(name = "book_id")
    private String bookId;
}
