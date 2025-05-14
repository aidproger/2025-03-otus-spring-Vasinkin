package ru.otus.hw.service;

public interface TestRenderer {

    String getQuestionRenderer();

    String getWithoutNumberQuestionRenderer();

    String getAnswerRenderer(int countAnswers);
}
