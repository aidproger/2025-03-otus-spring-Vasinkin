package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Интеграционный тест DAO, читающий вопросы ")
public class CsvQuestionDaoTest {

    private CsvQuestionDao questionDao;

    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    void setUp() {
        fileNameProvider = mock(TestFileNameProvider.class);
        questionDao = new CsvQuestionDao(fileNameProvider);
    }

    @DisplayName("должен зачитывать из CSV-файла и парсить вопросы и ответы ")
    @Test
    void shouldReadAndParseCSVFileWithQuestionsAndAnswers() {

        var fileName = "questions.csv";

        var expectedQuestions = getExpectedQuestionsForTest();

        given(fileNameProvider.getTestFileName()).willReturn(fileName);

        var actualQuestions = questionDao.findAll();

        verify(fileNameProvider, times(1)).getTestFileName();

        assertThat(actualQuestions.size()).isEqualTo(expectedQuestions.size());
        assertThat(actualQuestions).containsExactlyElementsOf(expectedQuestions);

    }

    private List<Question> getExpectedQuestionsForTest() {
        return new ArrayList<>(Arrays.asList(
                new Question("Is there life on Mars?",
                        new ArrayList<>(Arrays.asList(
                                new Answer("Science doesn't know this yet", true),
                                new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                                new Answer("Absolutely not", false)))),
                new Question("How should resources be loaded form jar in Java?",
                        new ArrayList<>(Arrays.asList(
                                new Answer("ClassLoader#geResourceAsStream or ClassPathResource#getInputStream", true),
                                new Answer("ClassLoader#geResource#getFile + FileReader", false),
                                new Answer("Wingardium Leviosa", false)))),
                new Question("Which option is a good way to handle the exception?",
                        new ArrayList<>(Arrays.asList(
                                new Answer("@SneakyThrow", false),
                                new Answer("e.printStackTrace()", false),
                                new Answer("Rethrow with wrapping in business exception (for example, QuestionReadException)", true),
                                new Answer("Ignoring exception", false)))),
                new Question("When did human fly into space in the first time?",
                        new ArrayList<>(Arrays.asList(
                                new Answer("In 1922 year", false),
                                new Answer("In 1954 year", false),
                                new Answer("In 1961 year", true),
                                new Answer("Human never flew into space", false)))),
                new Question("What pattern does AOP use?",
                        new ArrayList<>(Arrays.asList(
                                new Answer("Proxy", true),
                                new Answer("Decorator", false),
                                new Answer("Strategy", false)))),
                new Question("When founded St. Petersburg?",
                        new ArrayList<>(Arrays.asList(
                                new Answer("April 12, 1242", false),
                                new Answer("January 1, 1380", false),
                                new Answer("May 27, 1703", true),
                                new Answer("June 12, 1991", false))))
        ));
    }

}
