package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;


@ChangeLog(order = "001")
public class DatabaseChangelogV1 {

    @ChangeSet(order = "001", id = "dropDb", author = "aidproger", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "addBooks", author = "aidproger")
    public void addBooks(MongoDatabase db) {
        var mongoCollection = db.getCollection("books");

        var docsBooks = List.of(
                new Document("_id", "1").append("title", "BookTitle_1")
                        .append("author", new Document("_id", "1").append("full_name", "Author_1"))
                        .append("genres", List.of(new Document("_id", "1").append("name", "Genre_1"),
                                new Document("_id", "2").append("name", "Genre_2"))),
                new Document("_id", "2").append("title", "BookTitle_2")
                        .append("author", new Document("_id", "2").append("full_name", "Author_2"))
                        .append("genres", List.of(new Document("_id", "3").append("name", "Genre_3"),
                                new Document("_id", "4").append("name", "Genre_4"))),
                new Document("_id", "3").append("title", "BookTitle_3")
                        .append("author", new Document("_id", "3").append("full_name", "Author_3"))
                        .append("genres", List.of(new Document("_id", "5").append("name", "Genre_5"),
                                new Document("_id", "6").append("name", "Genre_6"))),
                new Document("_id", "4").append("title", "BookTitle_4")
                        .append("author", new Document("_id", "2").append("full_name", "Author_2"))
                        .append("genres", List.of(new Document("_id", "3").append("name", "Genre_3"),
                                new Document("_id", "4").append("name", "Genre_4")))
        );

        mongoCollection.insertMany(docsBooks);

    }

}
