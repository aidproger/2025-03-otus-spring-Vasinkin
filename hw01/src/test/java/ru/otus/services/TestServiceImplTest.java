package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestRenderer;
import ru.otus.hw.service.TestServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Сервис тестирования пользователя ")
public class TestServiceImplTest {

    private IOService ioService;

    private QuestionDao questionDao;

    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao, mock(TestRenderer.class));
    }

    @DisplayName("должен корректно отображать вопросы и ответы ")
    @Test
    void shouldCorrectRenderingQuestionsAndAnswers() {

        var questions = new ArrayList<>(Arrays.asList(
                new Question("Question1",
                        new ArrayList<>(Arrays.asList(
                            new Answer("Answer1", false),
                            new Answer("Answer2", true)))),
                new Question("Question2",
                        new ArrayList<>(Arrays.asList(
                            new Answer("Answer3", true),
                            new Answer("Answer4", false))))
        ));

        var expectedPrintFormattedLineInStreamsIO = new ArrayList<>(Arrays.asList(
                new Object[]{},
                new Object[]{1, "Question1"},
                new Object[]{new Answer("Answer1", false),
                             new Answer("Answer2", true)},
                new Object[]{2, "Question2"},
                new Object[]{new Answer("Answer3", true),
                             new Answer("Answer4", false)}
        ));

        given(questionDao.findAll()).willReturn(questions);
        var stringCaptor = ArgumentCaptor.forClass(String.class);
        var varargsCaptor = ArgumentCaptor.forClass(Object[].class);

        testService.executeTest();

        verify(questionDao, times(1)).findAll();
        verify(ioService, times(5)).printFormattedLine(stringCaptor.capture(), varargsCaptor.capture());
        var actualPrintFormattedLineInStreamsIO = varargsCaptor.getAllValues();
        assertThat(actualPrintFormattedLineInStreamsIO).containsExactlyElementsOf(expectedPrintFormattedLineInStreamsIO);
    }
}
