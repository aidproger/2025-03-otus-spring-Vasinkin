package ru.otus.hw.models.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {

    private final MongoOperations mongoOperations;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        try {
            var deletedBookId = event.getDocument().getObjectId("_id").toString();

            var query = new Query(Criteria.where("book_id").is(deletedBookId));

            var result = mongoOperations.remove(query, Comment.class);

            System.out.println("Deleted comments count %d, for book id '%s'"
                    .formatted(result.getDeletedCount(), deletedBookId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onAfterDelete(event);
    }
}
