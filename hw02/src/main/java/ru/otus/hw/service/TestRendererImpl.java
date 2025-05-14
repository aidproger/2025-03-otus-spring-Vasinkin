package ru.otus.hw.service;

import org.springframework.stereotype.Component;

@Component
public class TestRendererImpl implements TestRenderer {

    private static final String QUESTION_TEMPLATE = "%d) %s";

    private static final String QUESTION_WITHOUT_NUMBER_TEMPLATE = "%s";

    private static final String ANSWER_TEMPLATE_BEGIN = "   ";

    private static final String ANSWER_TEMPLATE_END = ") %s%n";

    @Override
    public String getQuestionRenderer() {
        return QUESTION_TEMPLATE;
    }

    @Override
    public String getWithoutNumberQuestionRenderer() {
        return QUESTION_WITHOUT_NUMBER_TEMPLATE;
    }

    @Override
    public String getAnswerRenderer(int countAnswers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < countAnswers; i++) {
            sb.append(ANSWER_TEMPLATE_BEGIN)
                    .append(i + 1)
                    .append(ANSWER_TEMPLATE_END);
        }
        return sb.toString();
    }
}
