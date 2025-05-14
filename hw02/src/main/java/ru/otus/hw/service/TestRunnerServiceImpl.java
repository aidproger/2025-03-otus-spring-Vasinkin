package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
@Service
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final IOService ioService;

    @Override
    public void run() {
        try {
            var student = studentService.determineCurrentStudent();
            var testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (QuestionReadException qre) {
            ioService.printLine("Error reading questions from resource file");
        } catch (IllegalArgumentException iae) {
            ioService.printLine("Error choosing number of answer");
        } catch (Exception e) {
            ioService.printLine("Error working application");
        }
    }
}
