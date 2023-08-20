package com.mds.java.sample.code;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class EvenOddAlternateThread {

    private static final Object object = new Object();

    private static final IntPredicate evenPredicate = even -> even % 2 == 0;
    private static final IntPredicate oddPredicate = odd -> odd % 2 != 0;

    @SneakyThrows
    public static void main(String[] args) {
        CompletableFuture.runAsync(() -> printEvenOdd(evenPredicate));
        CompletableFuture.runAsync(() -> printEvenOdd(oddPredicate));
        Thread.sleep(1000);
    }

    private static void printEvenOdd(IntPredicate intPredicate) {
        IntStream.rangeClosed(1, 20)
                .filter(intPredicate)
                .forEach(EvenOddAlternateThread::consoleLog);
    }

    public static void consoleLog(int input) {
        synchronized (object) {
            try {
                System.out.println(Thread.currentThread().getName() + " " + input);
                object.notify();
                object.wait();
            } catch (InterruptedException iEx) {
                System.out.println("Error occurred while printing even odd numbers");
            }
        }
    }

}
