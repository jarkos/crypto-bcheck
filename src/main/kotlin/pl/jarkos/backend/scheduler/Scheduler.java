package pl.jarkos.backend.scheduler;

import pl.jarkos.backend.file.FileRetention;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

import static pl.jarkos.backend.config.AppConfig.TWENTY_FOUR_HOURS_IN_MILLIS;

public class Scheduler {

    private Timer retentionTimer = new Timer("Retention scheduler", true);

    public void scheduleRetention() {
        retentionTimer.schedule(new FileRetention(), getNextMidnight(), TWENTY_FOUR_HOURS_IN_MILLIS);
    }

    private Date getNextMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().atStartOfDay().plusDays(1L);
        return Date.from(midnight.atZone(ZoneId.systemDefault()).toInstant());
    }

}
