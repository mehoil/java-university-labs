package ru.mehoil;

import java.time.LocalDate;
import java.util.Objects;

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
