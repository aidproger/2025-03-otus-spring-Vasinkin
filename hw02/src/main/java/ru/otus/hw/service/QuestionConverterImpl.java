package ru.otus.hw.service;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@Component
public class QuestionConverterImpl implements QuestionConverter {

    private static final String QUESTION_TEMPLATE = "%d) %s%n%s";

    private static final String ANSWER_TEMPLATE = "   %d) %s%n";


    public String convertQuestionToString(@NonNull Question question, int numberOfQuestion) {
        return QUESTION_TEMPLATE.formatted(numberOfQuestion, question.text(),
                convertAnswersToString(question.answers()));
    }

    private String convertAnswersToString(List<Answer> answers) {
        var sb = new StringBuilder();
        for (int i = 0; i < answers.size(); i++) {
            sb.append(ANSWER_TEMPLATE.formatted(i + 1, answers.get(i)));
        }
        return sb.toString();
    }
}
