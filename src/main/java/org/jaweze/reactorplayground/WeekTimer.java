package org.jaweze.reactorplayground;

import lombok.val;
import reactor.core.publisher.Flux;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

public class WeekTimer {

  public static void main(String[] args) throws InterruptedException {
    // Friday 14.9., 23:59:56
    val fridayAlmostMidnight = LocalDateTime.of(2019, 6, 14, 23, 59, 56);
    val clock = new VirtualClock(fridayAlmostMidnight);

    Predicate<Long> isWeekend = tick -> {
      DayOfWeek dayOfWeek = clock.now().getDayOfWeek();
      return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    };

    Predicate<Long> isWorkday = tick -> !isWeekend.test(tick);

    Flux<Long> everySecond = Flux.interval(Duration.ofSeconds(1));
    Flux<Long> everyThirdSeconds = Flux.interval(Duration.ofSeconds(3));

    Flux<Long> timer = Flux.merge(
        everySecond.filter(isWorkday),
        everyThirdSeconds.filter(isWeekend));

    timer.subscribe(tick -> System.out.println(clock.now()));

    Thread.sleep(10000);
  }

  public static class VirtualClock {

    private final LocalDateTime virtualStartTime;
    private final long actualStartTime;

    VirtualClock(LocalDateTime virtualStartTime) {
      this.virtualStartTime = virtualStartTime;
      this.actualStartTime = System.currentTimeMillis();
    }

    LocalDateTime now() {
      return virtualStartTime.plus(System.currentTimeMillis() - actualStartTime, ChronoUnit.MILLIS);
    }
  }
}
