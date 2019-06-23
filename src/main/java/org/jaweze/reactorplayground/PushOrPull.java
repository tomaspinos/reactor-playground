package org.jaweze.reactorplayground;

import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PushOrPull {

  public static void main(String[] args) {
    String[] words = new String[]{"Kočka", "leze", "dírou", "pes", "oknem"};

    List<String> list = Stream.of(words)
        .skip(2)
        .limit(3)
        .map(w -> w + "_transformed")
        .collect(Collectors.toList());

    list.forEach(System.out::println);

    Flux<String> flux = Flux.just(words)
        .skip(2)
        .take(3)
        .map(w -> w + "_transformed");

    flux.subscribe(System.out::println);

    flux.subscribe(System.out::println);
  }
}
