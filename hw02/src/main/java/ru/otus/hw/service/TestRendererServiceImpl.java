package ru.otus.hw.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@Service
public class TestRendererServiceImpl implements TestRendererService {

    private static final String QUESTION_TEMPLATE = "%d) %s";

    private static final String ANSWER_TEMPLATE_BEGIN = "   ";

    private static final String ANSWER_TEMPLATE_END = ") %s%n";


    public String createQuestionRenderer(@NonNull Question question, int numberOfQuestion) {
        return String.format(QUESTION_TEMPLATE, numberOfQuestion, question.text());
    }

    public String createAnswersRenderer(@NonNull Question question) {
        return String.format(getAnswerRenderer(question.answers().size()), question.answers().toArray(new Answer[0]));
    }

    private String getAnswerRenderer(int countAnswers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < countAnswers; i++) {
            sb.append(ANSWER_TEMPLATE_BEGIN)
                    .append(i + 1)
                    .append(ANSWER_TEMPLATE_END);
        }
        return sb.toString();
    }
}
