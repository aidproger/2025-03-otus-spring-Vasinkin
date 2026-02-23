package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.CommentService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component("recentcomments")
public class RecentCommentsHealthIndicator implements HealthIndicator {

    private static final int TIME_WINDOW_IN_MINUTES = 2;

    private final CommentService commentService;

    @Override
    public Health health() {
        var timeWindowInMinutes = LocalDateTime.now().minusMinutes(TIME_WINDOW_IN_MINUTES);
        var exists = commentService.existsByCreationDateAfter(timeWindowInMinutes);
        if (exists) {
            return Health.up()
                    .withDetail("message", "Recent comments available")
                    .build();
        } else {
            return Health.down()
                    .withDetail("message", "No recent comments")
                    .build();
        }
    }
}
