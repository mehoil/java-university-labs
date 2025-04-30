package ru.mehoil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * Application entry point.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class CollectionsAnalysisLab {

    private static final String RESULTS_TEMPLATE = """
            \tMethod Iterations Time (ns) for %s
            \s
            ADD\t\tx%s\t=>\t%s ns (%s ms),\s
            GET\t\tx%s\t=>\t%s ns (%s ms),\s
            REMOVE\tx%s\t=>\t%s ns (%s ms),\s
            \s""";

    public static void main(final String[] args) {
        final int iterations = 10_000;
        final int size = 100_000;

        System.out.println("Testing array list...");
        testCollection(new ArrayList<>(), iterations, size);

        System.out.println("\nTesting linked list...");
        testCollection(new LinkedList<>(), iterations, size);
    }

    private static void testCollection(final List<Integer> list, final int iterations, final int size) {
        IntStream.range(0, size).forEach(list::add);

        final long timeToAdd = measureTime(iterations, i -> list.add(size + i));
        final long timeToGet = measureTime(iterations, i -> list.get(size / 2));
        final long timeToRemove = measureTime(iterations, i -> list.remove(size / 2));

        final String results = formatResults(list.getClass().getName(), iterations, timeToAdd, timeToGet, timeToRemove);
        System.out.println(results);
    }

    private static long measureTime(final int iterations, final IntConsumer action) {
        final long startedAt = System.nanoTime();
        IntStream.range(0, iterations).forEach(action);
        return System.nanoTime() - startedAt;
    }

    private static String formatResults(
            final String className,
            final int iterations,
            final long timeToAdd,
            final long timeToGet,
            final long timeToRemove
    ) {
        return RESULTS_TEMPLATE.formatted(
                className,
                iterations, timeToAdd, timeToAdd / 1_000_000f,
                iterations, timeToGet, timeToGet / 1_000_000f,
                iterations, timeToRemove, timeToRemove / 1_000_000f
        );
    }

}