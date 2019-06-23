package org.jaweze.reactorplayground;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PublishOnSubscribeOn {

    private static final Map<String, AtomicInteger> threadInvocationCounts = new HashMap<>();

    private static final int COUNT = 100;
    private static final CountDownLatch latch = new CountDownLatch(COUNT);

    public static void main(String[] args) throws InterruptedException {
        new PublishOnSubscribeOn().doSomething();
    }

    private void doSomething() throws InterruptedException {
        Scheduler fromJsonScheduler = Schedulers.newParallel("fromJson", 4);
        Scheduler criticalSectionScheduler = Schedulers.newSingle("criticalSection");
        Scheduler toJsonScheduler = Schedulers.newParallel("toJson", 4);
        Scheduler dispatchScheduler = Schedulers.newSingle("dispatch");

        Flux<String> commands = Flux.range(0, COUNT).map(String::valueOf);

        commands.flatMap(json -> fromJson(json).subscribeOn(fromJsonScheduler))
                .publishOn(criticalSectionScheduler)
                .map(this::doSomethingInCriticalSection)
                .flatMap(result -> toJson(result).subscribeOn(toJsonScheduler))
                .publishOn(dispatchScheduler)
                .subscribe(item -> {
                    logThreadAndItem(null);
                    latch.countDown();
                });

        latch.await();

        System.out.println();
        System.out.println("Thread: invocation count");
        System.out.println(threadInvocationCounts.keySet().stream()
                .sorted()
                .map(thread -> thread + ": " + threadInvocationCounts.get(thread))
                .collect(Collectors.joining("\n")));

        System.exit(0);
    }

    private Object doSomethingInCriticalSection(Object o) {
        logThreadAndItem(o);
        return o;
    }

    private Mono<String> fromJson(String s) {
        return operation(s);
    }

    private <I, O> Mono<O> operation(I o) {
        return Mono.fromCallable(() -> {
            logThreadAndItem(o);
            // noinspection unchecked
            return (O) o;
        });
    }

    private Mono<String> toJson(Object o) {
        return operation(o);
    }

    private static void logThreadAndItem(Object o) {
        String threadName = Thread.currentThread().getName();
        threadInvocationCounts.putIfAbsent(threadName, new AtomicInteger(0));
        threadInvocationCounts.get(threadName).incrementAndGet();
        System.out.println(threadName + ": " + o);
    }
}
