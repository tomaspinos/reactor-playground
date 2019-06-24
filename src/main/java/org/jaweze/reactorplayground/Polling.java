package org.jaweze.reactorplayground;

import lombok.Value;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;

public class Polling {

  public static void main(String[] args) throws InterruptedException {
    Flux.interval(Duration.ofSeconds(1))
        .map(l -> LocalDateTime.now())
        .scan(Range.of(LocalDateTime.now(), LocalDateTime.now()),
            (range, date) -> Range.of(range.getTo(), date))
        .skip(1)
        .subscribe(System.out::println);

    Thread.sleep(10000);
  }

  @Value(staticConstructor = "of")
  static class Range {

    private final LocalDateTime from;
    private final LocalDateTime to;
  }
}
/*
Range(from=2019-06-24 15:07:44, to=2019-06-24 15:07:45)
Range(from=2019-06-24 15:07:45, to=2019-06-24 15:07:46)
Range(from=2019-06-24 15:07:46, to=2019-06-24 15:07:47)
Range(from=2019-06-24 15:07:47, to=2019-06-24 15:07:48)
Range(from=2019-06-24 15:07:48, to=2019-06-24 15:07:49)
 */
