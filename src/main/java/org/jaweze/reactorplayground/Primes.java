package org.jaweze.reactorplayground;

import reactor.core.publisher.Flux;

import java.math.BigInteger;
import java.time.Duration;
import java.util.stream.IntStream;

public class Primes {

  public static void main(String[] args) {
    IntStream.iterate(1, i -> i + 1)
        .filter(i -> BigInteger.valueOf(i).isProbablePrime(100))
        .limit(100)
        .forEach(System.out::println);

    Flux<Integer> primes = Flux.<Integer, Integer>generate(
        () -> 1,
        (state, sink) -> {
          sink.next(state + 1);
          return state + 1;
        })
        .filter(i -> BigInteger.valueOf(i).isProbablePrime(100));

    primes.take(100)
        .subscribe(System.out::println);

    primes.take(Duration.ofMillis(10))
        .subscribe(System.out::println);
  }
}
