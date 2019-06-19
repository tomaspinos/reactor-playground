package org.jaweze.reactorplayground;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fibonacci {

    /**
     * (0, 1)
     * (1, 1)
     * (1, 2)
     * (2, 3)
     * (3, 5)
     * (5, 8)
     */
    public static void main(String[] args) {
        List<Integer> list = Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(20)
                .map(a -> a[1])
                .collect(Collectors.toList());

        System.out.println(list);

        Flux<Integer> fibonacciGenerate = Flux.generate(() -> new int[]{0, 1},
                (state, sink) -> {
                    sink.next(state[1]);
                    return new int[]{state[1], state[0] + state[1]};
                });

        fibonacciGenerate.take(20)
                .subscribe(System.out::println);

        Flux<Integer> fibonacciRepeatScan = Mono.just(0)
                .repeat()
                .scan(new int[]{0, 1}, (a, dummy) -> new int[]{a[1], a[0] + a[1]})
                .map(a -> a[1]);

        fibonacciRepeatScan.take(20)
                .subscribe(System.out::println);
    }
}
