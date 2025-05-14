package ru.mehoil;

import ru.mehoil.beans.AutoInjectable;
import ru.mehoil.beans.service.RegularService;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Represents an application run a separate thread.
 * Uses {@link BlockingQueue} to accept and process tasks at any time.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class Application implements Runnable {

    static {
        System.out.println("Initializing application...");
    }

    private final BlockingQueue<List<String>> inputQueue;
    @AutoInjectable
    private RegularService service;

    public Application(final BlockingQueue<List<String>> queue) {
        this.inputQueue = queue;
        System.out.println("Application initialized");
    }


    @Override
    public void run() {
        System.out.println("Running Application...");
        if (service == null) {
            System.err.println("Service is not properly injected");
            return;
        }

        while (true) {
            try {
                final var input = inputQueue.take();
                System.out.println("Processing input: " + input);

                final var processed = service.processStrings(input);
                System.out.printf("Processed input: %s%n", processed.toString());
            } catch (final InterruptedException e) {
                System.out.println("Application thread interrupted. Exiting...");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
