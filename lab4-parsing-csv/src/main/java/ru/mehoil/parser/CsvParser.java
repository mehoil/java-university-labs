package ru.mehoil.parser;

import ru.mehoil.Division;
import ru.mehoil.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Consumes and parses .csv file, containing many {@link Person}.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class CsvParser {

    private static final int PARSED_LINE_PARTS = 6;

    public ParsingResult parseCsv(BufferedReader reader) throws IOException {
        final var persons = new ArrayList<Person>();
        final var errors = new ArrayList<String>();
        final var nameToDivision = new HashMap<String, Division>();
        final var divisionId = new AtomicLong(1);

        String line;
        boolean isFirstLine = true;
        while ((line = reader.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }

            final var parts = line.split(";");
            if (parts.length != PARSED_LINE_PARTS) {
                errors.add("Invalid line: " + line);
                continue;
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
            } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                errors.add("Error parsing line: " + line + " - " + e.getMessage());
            }
        }

        return new ParsingResult(persons, errors);
    }
}
