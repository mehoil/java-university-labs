package ru.mehoil;

import ru.mehoil.parser.CsvParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Application entry point.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class ParsingCsvLab {

    private static final String FILENAME = "foreign_names.csv";

    public static void main(final String[] args) {
        final var parser = new CsvParser();
        try (
                final var fis = new FileInputStream(FILENAME);
                final var br = new BufferedReader(new InputStreamReader(fis))
        ) {
            final var result = parser.parseCsv(br);
            result.errors().forEach(System.err::println);
            System.out.printf("Total people read: %d%n", result.persons().size()); // 25898
        } catch (final IOException e) {
            System.err.printf("Exception while reading file: %s - %s%n", FILENAME, e.getMessage());
        }
    }

}