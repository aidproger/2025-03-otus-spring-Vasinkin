package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface TestRendererService {

    String createQuestionRenderer(Question question, int numberOfQuestion);

    String createAnswersRenderer(Question question);
}
