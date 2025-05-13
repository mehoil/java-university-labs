package ru.mehoil.parser;

import ru.mehoil.Person;

import java.util.List;

/**
 * Represents a result produced by {@link CsvParser}.
 *
 * @param persons a list of mapped persons from the csv file
 * @param errors  a list of errors occurred in the parsing process
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public record ParsingResult(List<Person> persons, List<String> errors) {

    public ParsingResult {
        persons = List.copyOf(persons);
        errors = List.copyOf(errors);
    }

}
