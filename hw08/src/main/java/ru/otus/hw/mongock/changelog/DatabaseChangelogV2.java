package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.stream.IntStream;

@ChangeLog(order = "002")
public class DatabaseChangelogV2 {

    @ChangeSet(order = "001", id = "addComments", author = "aidproger")
    public void addComments(BookRepository bookRepository, CommentRepository commentRepository) {
        bookRepository.findAll().forEach(book -> {
            var comments = IntStream.range(1, 5).boxed()
                    .map(i -> {
                        return new Comment(null,
                                "comment_" + i + book.getId().substring(book.getId().length() - 2),
                                book);
                    })
                    .toList();
            commentRepository.saveAll(comments);
        });
    }

}
