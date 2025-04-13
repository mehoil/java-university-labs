package ru.mehoil.parsers;

import ru.mehoil.data.Token;
import ru.mehoil.data.TokenType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/Shunting_yard_algorithm">
 * Shunting yard algorithm (SYA)
 * </a> that parses given arithmetical expression and solves it.
 *
 * @author Mikhail Dorokhov
 * @since 0.0.1
 */
public class ShuntingYardParser {

    private static final int MIN_OPERANDS_IN_STACK = 2;
    private static final Map<String, Integer> OPERATOR_PRIORITY = Map.of(
            "+", 1,
            "-", 1,
            "*", 2,
            "/", 2
    );

    public static List<Token> parseArithmeticalExpression(final List<Token> tokens) {
        final var output = new ArrayList<Token>();
        final var stack = new ArrayDeque<Token>();
        tokens.forEach(token -> {
            switch (token.type()) {
                case VARIABLE, NUMBER -> output.add(token);
                case FUNCTION, LEFT_PAREN -> stack.push(token);
                case RIGHT_PAREN -> {
                    while (!stack.isEmpty() && stack.peek().type() != TokenType.LEFT_PAREN) {
                        output.add(stack.pop());
                    }
                    if (stack.isEmpty()) {
                        throw new IllegalArgumentException("Error: Extra closing bracket");
                    }
                    stack.pop();
                    if (!stack.isEmpty() && stack.peek().type() == TokenType.FUNCTION) {
                        output.add(stack.pop());
                    }
                }
                case OPERATOR -> {
                    while (!stack.isEmpty() && stack.peek().type() == TokenType.OPERATOR &&
                            OPERATOR_PRIORITY.get(stack.peek().value()) >= OPERATOR_PRIORITY.get(token.value())) {
                        output.add(stack.pop());
                    }
                    stack.push(token);
                }
            }
        });
        while (!stack.isEmpty()) {
            final var top = stack.pop();
            if (top.type() == TokenType.LEFT_PAREN) {
                throw new IllegalArgumentException("Error: Extra opening bracket");
            }
            output.add(top);
        }
        return output;
    }

    public static double solveParsedExpression(final List<Token> parsedSYA, final Map<String, Double> variableValues) {
        final var stack = new ArrayDeque<Double>();
        parsedSYA.forEach(token -> {
            switch (token.type()) {
                case NUMBER -> stack.push(Double.parseDouble(token.value()));
                case VARIABLE -> stack.push(variableValues.get(token.value()));
                case OPERATOR -> {
                    if (stack.size() < MIN_OPERANDS_IN_STACK) {
                        throw new IllegalArgumentException("Error: not enough operands for the operator");
                    }
                    final double b = stack.pop();
                    final double a = stack.pop();
                    switch (token.value()) {
                        case "+" -> stack.push(a + b);
                        case "-" -> stack.push(a - b);
                        case "*" -> stack.push(a * b);
                        case "/" -> {
                            if (b == 0) {
                                throw new ArithmeticException("Error: division by zero");
                            }
                            stack.push(a / b);
                        }
                    }
                }
                case FUNCTION -> {
                    if (stack.isEmpty()) {
                        throw new IllegalArgumentException("Error: not enough arguments for the function");
                    }
                    final double arg = stack.pop();
                    switch (token.value()) {
                        case "sin" -> stack.push(Math.sin(arg));
                        case "cos" -> stack.push(Math.cos(arg));
                        case "sqrt" -> {
                            if (arg < 0) {
                                throw new ArithmeticException("Error: negative value under sqrt");
                            }
                            stack.push(Math.sqrt(arg));
                        }
                    }
                }
            }
        });
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Error: operands count mismatch");
        }
        return stack.pop();
    }
}
