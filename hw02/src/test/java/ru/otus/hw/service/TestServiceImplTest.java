package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.otus.hw.common.QuestionConverter;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Сервис тестирования пользователя ")
public class TestServiceImplTest {

    private IOService ioService;

    private QuestionDao questionDao;

    private QuestionConverter questionConverter;

    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        questionConverter = mock(QuestionConverter.class);
        testService = new TestServiceImpl(ioService, questionDao, questionConverter);
    }

    @DisplayName("должен возвращать правильный результат при тестирование студента по списку вопросов")
    @Test
    void shouldCorrectTestingStudentByQuestions() {

        var student = new Student("Ivan", "Ivanov");

        var expectedQuestions = TestDataGenerator.generateExpectedQuestionsForCSVTest();
        var expectedNumbersOfQuestions = List.of(1, 2, 3, 4, 5, 6);

        given(questionDao.findAll()).willReturn(expectedQuestions);
        given(ioService.readIntForRangeWithPrompt(1, 3, "Choose number of right answer",
                "Number of answer not found! Choose number of right answer again")).willReturn(1);
        given(ioService.readIntForRangeWithPrompt(1, 4, "Choose number of right answer",
                "Number of answer not found! Choose number of right answer again")).willReturn(1);
        var stringCaptor = ArgumentCaptor.forClass(String.class);
        var varargsCaptor = ArgumentCaptor.forClass(Object[].class);
        var questionCaptor = ArgumentCaptor.forClass(Question.class);
        var integerCaptor = ArgumentCaptor.forClass(Integer.class);

        var testResult = testService.executeTestFor(student);

        verify(questionDao, times(1)).findAll();
        verify(ioService, times(1)).printFormattedLine(stringCaptor.capture(), varargsCaptor.capture());
        verify(ioService, times(7)).printLine(stringCaptor.capture());
        verify(questionConverter, times(6)).convertQuestionToString(questionCaptor.capture(), integerCaptor.capture());

        var actualCreateQuestionRenderer = questionCaptor.getAllValues();
        assertThat(actualCreateQuestionRenderer).containsExactlyElementsOf(expectedQuestions);
        var actualNumbersOfQuestions = integerCaptor.getAllValues();
        assertThat(actualNumbersOfQuestions).containsExactlyElementsOf(expectedNumbersOfQuestions);

        assertThat(testResult.getAnsweredQuestions()).containsExactlyElementsOf(expectedQuestions);
        assertThat(testResult.getRightAnswersCount()).isEqualTo(3);
    }
}
