package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.common.QuestionConverter;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

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
        return ioService.readIntForRangeWithPromptLocalized(1, countAnswers, "TestService.choose.number.of.right.answer",
                "TestService.choose.number.of.right.answer.error");
    }

}
