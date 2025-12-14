package ru.otus.hw.common;

import ru.otus.hw.domain.Question;

public interface QuestionConverter {

    String convertQuestionToString(Question question, int numberOfQuestion);
}
