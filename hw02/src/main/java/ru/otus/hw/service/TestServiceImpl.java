package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.common.QuestionConverter;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (int i = 0; i < questions.size(); i++) {
            var question = questions.get(i);
            var strQuestionForOutput = questionConverter.convertQuestionToString(question, i + 1);
            ioService.printLine(strQuestionForOutput);
            var number = chooseNumberOfAnswer(question.answers().size());
            var isAnswerValid = question.answers().get(number - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private int chooseNumberOfAnswer(int countAnswers) {
        return ioService.readIntForRangeWithPrompt(1, countAnswers, "Choose number of right answer",
                "Number of answer not found! Choose number of right answer again");
    }
}
