package ru.mehoil;

import static ru.mehoil.parsers.ExpressionParser.parse;

/**
 * Application entry point.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class ParsingExpressionsLab {
    public static void main(final String[] args) {
        final String[] expressions = {
                "4 + (10 * (20 - 19 * (30 + 1))) + 7 * (2 - 1)",
                "sin(3.14) + x * 2",
                "a + b * 3",
                "2 + )3("
        };
        for (String expr : expressions) {
            System.out.println("Given expression: " + expr);
            double result = parse(expr);
            if (!Double.isNaN(result)) {
                System.out.println("Result: " + result);
            }
            System.out.println();
        }
    }
}