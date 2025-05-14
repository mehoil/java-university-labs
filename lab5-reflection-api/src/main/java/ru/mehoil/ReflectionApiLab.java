package ru.mehoil;

import ru.mehoil.beans.Injector;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Application entry point.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class ReflectionApiLab {

    public static void main(final String[] args) {
        final var inputQueue = new LinkedBlockingQueue<List<String>>();
        final var app = new Application(inputQueue);

        final var injector = new Injector();
        injector.inject(app);

        try (final var executor = Executors.newSingleThreadExecutor()) {
            executor.submit(app);
            loopRead(inputQueue);
            executor.shutdownNow();
        }
    }

    private static void loopRead(final BlockingQueue<List<String>> inputQueue) {
        try (final var scan = new Scanner(System.in)) {
            System.out.println("Enter data to process in application (or 'exit' to quit): ");

            while (true) {
                final String input = scan.nextLine().trim();
                if ("exit".equalsIgnoreCase(input)) {
                    System.err.println("Shutting down...");
                    break;
                }

                final var inputList = Arrays.asList(input.split("\\s+"));
                inputQueue.put(inputList);
            }
        } catch (final InterruptedException e) {
            System.err.println("Main thread interrupted. Shutting down...");
            Thread.currentThread().interrupt();
        }
    }

}