package ru.mehoil;

import java.util.Objects;

/**
 * Division model.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public record Division(long id, String name) {

    public Division {
        Objects.requireNonNull(name);
    }
}
