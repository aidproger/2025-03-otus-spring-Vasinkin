package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.QuestionReadException;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final LocalizedIOService ioService;

    @Override
    public void run() {
        try {
            var student = studentService.determineCurrentStudent();
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (QuestionReadException qre) {
            ioService.printLine("");
            ioService.printLineLocalized("TestRunnerService.error.reading.questions.from.resource.file");
            ioService.printLine("");
        } catch (IllegalArgumentException iae) {
            ioService.printLine("");
            ioService.printLineLocalized("TestRunnerService.error.choosing.number.of.answer");
            ioService.printLine("");
        } catch (Exception e) {
            ioService.printLine("");
            ioService.printLineLocalized("TestRunnerService.error.working.application");
            ioService.printLine("");
        }
    }

}
