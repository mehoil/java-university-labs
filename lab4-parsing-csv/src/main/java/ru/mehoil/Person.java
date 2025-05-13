package ru.mehoil;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Person model.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public record Person(
        long id,
        String name,
        String gender,
        LocalDate birthdate,
        Division division,
        long salary
) {

    public Person {
        Objects.requireNonNull(name);
        Objects.requireNonNull(gender);
        Objects.requireNonNull(birthdate);
        Objects.requireNonNull(division);
    }
}
