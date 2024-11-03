package fr.tse.fise2.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public CalculationResult evaluateExpression(String expression) throws CalculatorException {
        List<String> tokens = tokenize(expression);
        Stack<Double> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                values.push(Double.parseDouble(token));
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

    /**
     * Méthode pour convertir une expression mathématique en notation postfixée.
     * @param expression L'expression mathématique à convertir.
     * @return Une chaîne de caractères représentant l'expression en notation postfixée.
     */
    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\d+\\.\\d+|\\d+|[+\\-x÷%()]").matcher(expression);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    /** 
     * Méthode pour déterminer si une chaîne est un nombre
     * @param str La chaîne à vérifier
     * @return true si la chaîne est un nombre, false sinon
     */
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
            case "±":
                return 3;
            default:
                return 0;
        }
    }

    // Méthode pour exécuter les opérations
    private double performOperation(double a, double b, String operator) throws CalculatorException {
        switch (operator) {
            case "+":
                return engine.add(a, b);
            case "-":
                return engine.subtract(a, b);
            case "x":
                return engine.multiply(a, b);
            case "÷":
                return engine.divide(a, b);
            case "%":
                return engine.percentOrModulo(a, b);
            default:
                throw new CalculatorException("Opérateur non pris en charge: " + operator);
        }
    }
}