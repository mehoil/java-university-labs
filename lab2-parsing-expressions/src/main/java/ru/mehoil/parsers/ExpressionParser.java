package ru.mehoil.parsers;

import ru.mehoil.data.Token;
import ru.mehoil.data.TokenType;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Parses and solves complex string expressions into a list of {@link Token}
 * using {@link ShuntingYardParser}.
 *
 * @author Mikhail Dorokhov
 * @since 0.0.1
 */
public class ExpressionParser {

    private static final String EXPRESSION_PATTERN = "\\d+\\.\\d+|\\d+|[a-zA-Z][a-zA-Z0-9]*|[+\\-*/()]";
    private static final String NUMBER_PATTERN = "\\d+\\.\\d+|\\d+";
    private static final String VARIABLE_PATTERN = "[a-zA-Z][a-zA-Z0-9]*";

    private static final Set<String> FUNCTIONS = Set.of("sin", "cos", "sqrt");

    public static double parse(final String expression) {
        try {
            final var tokens = tokenize(expression);
            if (!areBracketsBalanced(tokens)) {
                throw new IllegalArgumentException("Error: unbalanced brackets");
            }

            final var variables = new HashSet<String>();
            tokens.forEach(token -> {
                if (token.type() == TokenType.VARIABLE) {
                    variables.add(token.value());
                }
            });

            final var variableValues = new HashMap<String, Double>();
            if (!variables.isEmpty()) {
                final var scanner = new Scanner(System.in);
                variables.forEach(variable -> {
                    System.out.print("Enter the value of '" + variable + "': ");
                    double value = scanner.nextDouble();
                    variableValues.put(variable, value);
                });
            }

            final var parsedSYA = ShuntingYardParser.parseArithmeticalExpression(tokens);
            return ShuntingYardParser.solveParsedExpression(parsedSYA, variableValues);
        } catch (final Exception e) {
            System.err.println(e.getMessage() != null ? e.getMessage() : "Error: invalid value");
            return Double.NaN;
        }
    }

    private static List<Token> tokenize(final String expression) {
        final var tokens = new ArrayList<Token>();
        final var pattern = Pattern.compile(EXPRESSION_PATTERN);
        final var matcher = pattern.matcher(expression.replaceAll("\\s+", ""));
        while (matcher.find()) {
            final var token = matcher.group();
            if (token.matches(NUMBER_PATTERN)) {
                tokens.add(new Token(TokenType.NUMBER, token));
            } else if (FUNCTIONS.contains(token)) {
                tokens.add(new Token(TokenType.FUNCTION, token));
            } else if (token.matches(VARIABLE_PATTERN)) {
                tokens.add(new Token(TokenType.VARIABLE, token));
            } else if (token.equals("(")) {
                tokens.add(new Token(TokenType.LEFT_PAREN, token));
            } else if (token.equals(")")) {
                tokens.add(new Token(TokenType.RIGHT_PAREN, token));
            } else {
                tokens.add(new Token(TokenType.OPERATOR, token));
            }
        }
        return tokens;
    }

    private static boolean areBracketsBalanced(final List<Token> tokens) {
        int balance = 0;
        for (final var token : tokens) {
            if (token.type() == TokenType.LEFT_PAREN) {
                balance++;
            } else if (token.type() == TokenType.RIGHT_PAREN) {
                balance--;
            }
            if (balance < 0) {
                return false;
            }
        }
        return balance == 0;
    }

}