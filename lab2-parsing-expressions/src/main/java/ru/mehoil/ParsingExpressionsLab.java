package ru.mehoil;

import java.util.Scanner;

import static ru.mehoil.parsers.ExpressionParser.parse;

/**
 * Application entry point.
 *
 * @author Mikhail Dorokhov
 * @version 1.0
 */
public class ParsingExpressionsLab {
    public static void main(final String[] args) {
        final var scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            final var expression = scanner.nextLine();
            if (expression.equalsIgnoreCase("exit")) {
                break;
            }
            final var result = parse(expression);
            if (!Double.isNaN(result)) {
                System.out.println("=> " + result);
            }
        }
    }
}