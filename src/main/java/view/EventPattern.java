package view;

import java.time.LocalDateTime;

public interface EventPattern {

    LocalDateTime getTimeEnd();
    long getDuration();
    void setDuration(long duration);
}
