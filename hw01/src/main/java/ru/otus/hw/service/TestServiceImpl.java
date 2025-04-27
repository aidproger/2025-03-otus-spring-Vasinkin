package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final TestRenderer testRenderer;

    @Override
    public void executeTest() {
        try {
            List<Question> questions = questionDao.findAll();

            ioService.printLine("");
            ioService.printFormattedLine("Please answer the questions below%n");

            var numberQuestion = new AtomicInteger(1);
            questions.forEach(question -> {
                ioService.printFormattedLine(
                        testRenderer.getQuestionRenderer(),
                        numberQuestion.getAndIncrement(),
                        question.text());
                ioService.printFormattedLine(
                        testRenderer.getAnswerRenderer(question.answers().size()),
                        question.answers().toArray(new Answer[0]));
            });

        } catch (QuestionReadException qre) {
            ioService.printLine("Error reading questions from resource file");//ошибка чтения вопросов
        } catch (Exception e) {
            ioService.printLine("Error working application");//общая ошибка приложения
        }
    }

}
