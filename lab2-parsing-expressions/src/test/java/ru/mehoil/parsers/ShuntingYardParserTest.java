package ru.mehoil.parsers;

import org.junit.jupiter.api.Test;
import ru.mehoil.data.Token;
import ru.mehoil.data.TokenType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link ShuntingYardParser} tests.
 *
 * @author Mikhail Dorokhov
 * @since 0.0.1
 */
class ShuntingYardParserTest {

    @Test
    public void testSimpleExpression() {
        final var tokens = List.of(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.OPERATOR, "+"),
                new Token(TokenType.NUMBER, "3")
        );
        final var expected = List.of(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.OPERATOR, "+")
        );
        assertEquals(expected, ShuntingYardParser.parseArithmeticalExpression(tokens));
    }

    @Test
    public void testExpressionWithBrackets() {
        final var tokens = List.of(
                new Token(TokenType.LEFT_PAREN, "("),
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.OPERATOR, "+"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.RIGHT_PAREN, ")"),
                new Token(TokenType.OPERATOR, "*"),
                new Token(TokenType.NUMBER, "4")
        );
        final var expected = List.of(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.OPERATOR, "+"),
                new Token(TokenType.NUMBER, "4"),
                new Token(TokenType.OPERATOR, "*")
        );
        assertEquals(expected, ShuntingYardParser.parseArithmeticalExpression(tokens));
    }

    @Test
    public void testExpressionWithFunction() {
        final var tokens = List.of(
                new Token(TokenType.FUNCTION, "sin"),
                new Token(TokenType.LEFT_PAREN, "("),
                new Token(TokenType.NUMBER, "0"),
                new Token(TokenType.RIGHT_PAREN, ")")
        );
        final var expected = List.of(
                new Token(TokenType.NUMBER, "0"),
                new Token(TokenType.FUNCTION, "sin")
        );
        assertEquals(expected, ShuntingYardParser.parseArithmeticalExpression(tokens));
    }

    @Test
    public void testExtraClosingBracket() {
        final var tokens = List.of(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.OPERATOR, "+"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.RIGHT_PAREN, ")")
        );
        assertThrows(IllegalArgumentException.class, () -> ShuntingYardParser.parseArithmeticalExpression(tokens));
    }

    @Test
    public void testExtraOpeningBracket() {
        final var tokens = List.of(
                new Token(TokenType.LEFT_PAREN, "("),
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.OPERATOR, "+"),
                new Token(TokenType.NUMBER, "3")
        );
        assertThrows(IllegalArgumentException.class, () -> ShuntingYardParser.parseArithmeticalExpression(tokens));
    }

    @Test
    public void testOperatorPriority() {
        final var tokens = List.of(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.OPERATOR, "+"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.OPERATOR, "*"),
                new Token(TokenType.NUMBER, "4")
        );
        final var expected = List.of(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.NUMBER, "4"),
                new Token(TokenType.OPERATOR, "*"),
                new Token(TokenType.OPERATOR, "+")
        );
        assertEquals(expected, ShuntingYardParser.parseArithmeticalExpression(tokens));
    }
}
