package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Import(AppProperties.class)
@RequiredArgsConstructor
@Repository
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        return readCSVWithQuestions().stream()
                .map(QuestionDto::toDomainObject)
                .toList();

    }

    private List<QuestionDto> readCSVWithQuestions() {
        try (InputStream questionsResourceInputStream =
                     CsvQuestionDao.class.getResourceAsStream("/" + fileNameProvider.getTestFileName())) {

            return new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(questionsResourceInputStream))
                    .withSkipLines(1)
                    .withSeparator(';')
                    .withType(QuestionDto.class)
                    .build()
                    .parse();

        } catch (Exception e) {
            throw new QuestionReadException("Error reading csv resource file '"
                    + fileNameProvider.getTestFileName() + "'...", e);
        }
    }
}
