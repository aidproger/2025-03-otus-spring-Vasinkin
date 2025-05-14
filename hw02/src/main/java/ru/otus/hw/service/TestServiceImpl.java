package ru.otus.hw.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final TestRenderer testRenderer;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (int i = 0; i < questions.size(); i++) {
            var question = questions.get(i);
            displayQuestionAndAnswers(question, i + 1);
            var number = chooseNumberOfAnswer(question.answers().size());
            var isAnswerValid = question.answers().get(number).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private void displayQuestionAndAnswers(@NonNull Question question, int numberOfQuestion) {
        ioService.printFormattedLine(
                testRenderer.getQuestionRenderer(),
                numberOfQuestion,
                question.text());
        ioService.printFormattedLine(
                testRenderer.getAnswerRenderer(question.answers().size()),
                question.answers().toArray(new Answer[0]));
    }

    private int chooseNumberOfAnswer(int countAnswers) {
        var number = ioService.readIntForRangeWithPrompt(1, countAnswers, "Choose number of right answer",
                "Number of answer not found! Choose number of right answer again");
        number--;
        return number;
    }
}
