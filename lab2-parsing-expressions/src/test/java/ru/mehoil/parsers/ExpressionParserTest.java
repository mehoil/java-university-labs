package ru.mehoil.parsers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * {@link ExpressionParser} tests.
 *
 * @author Mikhail Dorokhov
 * @since 0.0.1
 */
class ExpressionParserTest {

    private static final double DELTA = 1e-10;

    @AfterEach
    void resetSystemIn() {
        System.setIn(System.in);
    }

    @Test
    void testSolveSimpleExpression() {
        final String expression = "2 + 3";
        final double expected = 5.0;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testSolveComplexExpression() {
        final String expression = "4 + (10 * (20 - 19 * (30 + 1))) + 7 * (2 - 1)";
        final double expected = -5679.0;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testOperationsOrder() {
        final String expression = "3 * (4 * (1 + 2 * 3 / 2) - 4) / 1 + 2";
        final double expected = 38.0;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testSolveNoOperators() {
        final String expression = "10";
        final double expected = 10.0;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testSolveWithWhitespaces() {
        final String expression = "1 0 0 +  1  2  3  .     4";
        final double expected = 223.4;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testDivideByOne() {
        final String expression = "99.9 / 1";
        final double expected = 99.9;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testDivideByZero() {
        final String expression = "99.9 / 0";

        final var result = ExpressionParser.parse(expression);
        assertEquals(Double.NaN, result);
    }

    @Test
    void testExtraOperator() {
        final String expression = "1 + 2 - 3 +";

        final var result = ExpressionParser.parse(expression);
        assertEquals(Double.NaN, result);
    }

    @Test
    void testInvalidBrackets() {
        final String expression = "1 + (2 - 3) + (4 * (1 + 3)))";

        final var result = ExpressionParser.parse(expression);
        assertEquals(Double.NaN, result);
    }

    @Test
    void testSinFunction() {
        final String expression = "sin(0)";
        final double expected = 0.0;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result, DELTA);
    }

    @Test
    void testCosFunction() {
        final String expression = "cos(0)";
        final double expected = 1.0;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result, DELTA);
    }

    @Test
    void testSqrtFunction() {
        final String expression = "sqrt(16)";
        final double expected = 4.0;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testNestedFunctions() {
        final String expression = "sin(cos(0))";
        final double expected = Math.sin(1.0);

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result, DELTA);
    }

    @Test
    void testMultipleFunctions() {
        final String expression = "sin(0) + cos(0) + sqrt(4)";
        final double expected = 3.0;

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result, DELTA);
    }

    @Test
    void testSimpleVariable() {
        final String expression = "x + 5";
        final double expected = 8.0;

        final String input = "3\n";
        final var bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testMultipleVariables() {
        final String expression = "a + b * c";
        final double expected = 7.0;

        final String input = "1\n2\n3\n";
        final var bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testLongVariableName() {
        final String expression = "variableName + 10";
        final double expected = 15.0;

        final String input = "5\n";
        final var bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

    @Test
    void testVariableInFunction() {
        final String expression = "sin(x)";
        final double expected = 0.0;

        final String input = "0\n";
        final var bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result, DELTA);
    }

    @Test
    void testInvalidFunctionName() {
        final String expression = "invalidFunc(5)";

        final var result = ExpressionParser.parse(expression);
        assertEquals(Double.NaN, result);
    }

    @Test
    void testFunctionWithComplexArgument() {
        final String expression = "sin(1 + 2 * 3)";
        final double expected = Math.sin(7.0);

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result, DELTA);
    }

    @Test
    void testVariableAndFunctionCombination() {
        final String expression = "x + sin(y)";
        final double expected = 1.0;

        final String input = "1\n0\n";
        final var bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result, DELTA);
    }

    @Test
    void testSqrtNegativeNumber() {
        final String expression = "sqrt(-4)";

        final var result = ExpressionParser.parse(expression);
        assertEquals(Double.NaN, result);
    }

    @Test
    void testCaseSensitivityInFunctions() {
        final String expression = "SIN(0)";
        final double expected = 0.0;

        // if the func is not recognized => assume it's a variable
        final String input = "1\n";
        final var bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        final var result = ExpressionParser.parse(expression);
        assertNotEquals(expected, result);
    }

    @Test
    void testVariableNameWithNumbers() {
        final String expression = "x1 + x2";
        final double expected = 3.0;

        final String input = "1\n2\n";
        final var bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);

        final var result = ExpressionParser.parse(expression);
        assertEquals(expected, result);
    }

}