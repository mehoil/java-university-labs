package ru.mehoil;

import java.util.Objects;

/**
 *
 */
public record Division(long id, String name) {

    public Division {
        Objects.requireNonNull(name);
    }
}
