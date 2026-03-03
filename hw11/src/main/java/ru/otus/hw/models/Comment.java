package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@AllArgsConstructor
@TypeAlias("Comment")
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String text;

    @DocumentReference(lazy = true)
    private Book book;
}
