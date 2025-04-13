package ru.mehoil.data;

import java.util.Objects;

/**
 * Represents a parsed token within a string expression.
 *
 * @param type  - type of the token
 * @param value - token value
 * @author Mikhail Dorokhov
 * @since 0.0.1
 */
public record Token(
        TokenType type,
        String value
) {
    public Token {
        Objects.requireNonNull(type);
        Objects.requireNonNull(value);
    }
}
