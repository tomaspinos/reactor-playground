package org.jaweze.reactorplayground;

import reactor.core.publisher.Flux;

import java.util.stream.Stream;

public class PushOrPull {

    public static void main(String[] args) {
        String[] words = new String[]{"Kočka", "leze", "dírou", "pes", "oknem", "Nebude-li", "pršet", "nezmoknem"};

        Stream.of(words)
                .skip(5)
                .limit(5)
                .map(w -> w + "_transformed")
                .forEach(System.out::println);

        Flux.just(words)
                .skip(5)
                .take(5)
                .map(w -> w + "_transformed")
                .subscribe(System.out::println);
    }
}
