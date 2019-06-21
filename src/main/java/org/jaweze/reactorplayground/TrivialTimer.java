package org.jaweze.reactorplayground;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;

public class TrivialTimer {

  public static void main(String[] args) throws InterruptedException {
    Flux.interval(Duration.ofSeconds(1))
        .subscribe(tick -> System.out.println(LocalDateTime.now() + ": " + tick));

    Thread.sleep(5000);
  }
}
