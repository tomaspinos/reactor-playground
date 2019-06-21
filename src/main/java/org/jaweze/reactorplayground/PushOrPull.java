package org.jaweze.reactorplayground;

import reactor.core.publisher.Flux;

import java.util.stream.Stream;

public class PushOrPull {

  public static void main(String[] args) {
    String[] words = new String[]{"Kočka", "leze", "dírou", "pes", "oknem"};

    Stream.of(words)
        .skip(2)
        .limit(3)
        .map(w -> w + "_transformed")
        .forEach(System.out::println);

    Flux.just(words)
        .skip(2)
        .take(3)
        .map(w -> w + "_transformed")
        .subscribe(System.out::println);
  }
}
