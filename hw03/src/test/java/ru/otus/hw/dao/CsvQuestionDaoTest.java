package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.common.TestDataGenerator;
import ru.otus.hw.config.TestFileNameProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

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

        var expectedQuestions = TestDataGenerator.generateExpectedQuestionsForCSVTest();

        given(fileNameProvider.getTestFileName()).willReturn(fileName);

        var actualQuestions = questionDao.findAll();

        verify(fileNameProvider, times(1)).getTestFileName();

        assertThat(actualQuestions.size()).isEqualTo(expectedQuestions.size());
        assertThat(actualQuestions).containsExactlyElementsOf(expectedQuestions);

    }

}
