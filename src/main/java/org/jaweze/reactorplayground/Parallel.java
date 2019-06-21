package org.jaweze.reactorplayground;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class Parallel {

  public static void main(String[] args) {
    new Parallel().doSomething();
  }

  private void doSomething() {
    IntStream.range(1, 101).boxed()
        .filter(this::isPrime)
        .parallel()
        .forEach(System.out::println);

    new ForkJoinPool(4).submit(() -> {
      IntStream.range(1, 101).boxed()
          .filter(this::isPrime)
          .parallel()
          .forEach(System.out::println);
    });

    Flux.range(1, 100)
        .filter(this::isPrime)
        .parallel()
        .subscribe(System.out::println);

    Flux.range(1, 100)
        .filter(this::isPrime)
        .parallel()
        .runOn(Schedulers.newParallel("primes", 4))
        .subscribe(System.out::println);
  }

  private boolean isPrime(int i) {
    return BigInteger.valueOf(i).isProbablePrime(100);
  }
}
