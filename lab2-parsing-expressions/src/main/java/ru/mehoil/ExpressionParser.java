package ru.mehoil;

public class ExpressionParser {

    public static final String TEST_EXPR = "4 + (10 * (20 - 19 * (30 + 1))) + 7 * (2 - 1)";

    private static final String[] OPERANDS = {"+", "-", "*", "/"};

    public static long parse(final String expression) {
        int openBracketsCount = 0;
        int closeBracketsCount = 0;

        double result = 0;

        String[] tokens = expression.split("\\s+");
        expression.trim();
    }

    private static long calculateInner(final String innerExpression) {

    }

}

// + ( * ( - * ( + ) ) ) + * ( - )
// 4 10 20 19 30 1 7 2 1