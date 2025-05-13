package ru.mehoil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Application entry point.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class ParsingCsvLab {

    private static final int PARSED_LINE_PARTS = 6;
    private static final String FILENAME = "foreign_names.csv";

    public static void main(final String[] args) {

        final var persons = new ArrayList<Person>();
        final var nameToDivision = new HashMap<String, Division>();
        final var divisionId = new AtomicLong(1);

        try (
                final var fis = new FileInputStream(FILENAME);
                final var br = new BufferedReader(new InputStreamReader(fis))
        ) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                final var parts = line.split(";");
                if (parts.length != PARSED_LINE_PARTS) {
                    System.err.printf("Invalid line: %s%n", line);
                }

                try {
                    final var id = Long.parseLong(parts[0]);
                    final var name = parts[1];
                    final var gender = parts[2];
                    final var birthdate = LocalDate.parse(parts[3], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    final var divisionName = parts[4].trim();
                    final var salary = Long.parseLong(parts[5]);

                    final var division = nameToDivision.computeIfAbsent(divisionName, dname -> new Division(
                            divisionId.getAndIncrement(), dname
                    ));

                    final var person = new Person(id, name, gender, birthdate, division, salary);
                    persons.add(person);
                } catch (final NumberFormatException e) {
                    System.err.printf("Error parsing line: %s - %s%n", line, e.getMessage());
                }
            }
        } catch (final IOException e) {
            e.printStackTrace(); // handle exception somehow
        }

        System.out.printf("Total people read: %s%n", persons.size()); // 25898
    }

}