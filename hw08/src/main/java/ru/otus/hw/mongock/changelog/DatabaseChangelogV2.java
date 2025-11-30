package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeLog(order = "002")
public class DatabaseChangelogV2 {

    @ChangeSet(order = "001", id = "addComments", author = "aidproger")
    public void addComments(MongoDatabase db) throws JSONException {
        var comments = db.getCollection("comments");
        var books = db.getCollection("books");

        var jsonArray = "[{ \"text\": \"comment_1\" }, { \"text\": \"comment_2\" }, { \"text\": \"comment_3\" }," +
                "{ \"text\": \"comment_4\" }, { \"text\": \"comment_5\" }, { \"text\": \"comment_6\" }," +
                "{ \"text\": \"comment_7\" }, { \"text\": \"comment_8\" }, { \"text\": \"comment_9\" }]";
        var array = new JSONArray(jsonArray);
        int indexBegin = 0;
        int indexEnd = 3;
        for (Document book : books.find()) {
            var docs = IntStream.range(indexBegin, indexEnd)
                    .mapToObj(i -> {
                        try {
                            return Document.parse(array.getJSONObject(i).put("book_id", book.get("_id")).toString());
                        } catch (JSONException jsone) {
                            throw new RuntimeException(jsone.toString());
                        }
                    })
                    .collect(Collectors.toList());
            comments.insertMany(docs);
            indexBegin += 3;
            indexEnd += 3;
        }
    }

}
