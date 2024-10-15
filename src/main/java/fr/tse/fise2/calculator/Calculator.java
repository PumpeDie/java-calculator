package fr.tse.fise2.calculator;

import java.util.Stack;

/**
 * Classe Calculator qui gère les opérations de la calculatrice.
 */
public class Calculator {

    private CalculatorEngine engine;

    public Calculator() {
        engine = new CalculatorEngine();
    }

    /**
     * Évalue l'expression mathématique donnée sous forme de chaîne.
     * @param expression La chaîne d'expression à évaluer.
     * @return Un objet CalculationResult contenant le résultat et les informations pertinentes.
     */
    public CalculationResult evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        Stack<Double> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                values.push(Double.parseDouble(token));
            } else if ("±".equals(token)) {
                // Gérer l'opérateur "±"
                if (!values.isEmpty()) {
                    double value = values.pop();
                    values.push(engine.negate(value)); // Inverse le signe du dernier opérande
                } else {
                    throw new IllegalArgumentException("Aucun nombre avant le signe '±'.");
                }
            } else {
                // Traitement des opérateurs
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    double b = values.pop();
                    double a = values.pop();
                    String operator = operators.pop();
                    double result = performOperation(a, b, operator);
                    values.push(result);
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            double b = values.pop();
            double a = values.pop();
            String operator = operators.pop();
            double result = performOperation(a, b, operator);
            values.push(result);
        }

        double finalResult = values.pop();
        return new CalculationResult(finalResult, expression); // Crée un résultat avec l'expression
    }

    // Méthode pour déterminer si une chaîne est un nombre
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Méthode pour gérer la priorité des opérateurs
    private int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "x":
            case "÷":
            case "%":
                return 2;
            default:
                return 0;
        }
    }

    // Méthode pour exécuter les opérations
    private double performOperation(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return engine.add(a, b);
            case "-":
                return engine.subtract(a, b);
            case "x":
                return engine.multiply(a, b);
            case "÷":
                return engine.divide(a, b);
            case "±":
                return engine.negate(a);
            case "%":
                return engine.percentOrModulo(a, b);
            default:
                throw new UnsupportedOperationException("Opérateur non pris en charge : " + operator);
        }
    }
}