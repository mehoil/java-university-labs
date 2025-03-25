package ru.mehoil;

/**
 * Application entry point.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class ContainerClassLab {

    public static void main(final String[] args) {
        final long[] numbers = new long[]{11, 22, 33, 44, 55};
        final var nc = new NumbersContainer(numbers);
        System.out.println(nc);

    }
}