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
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                // Calculer tout ce qui est dans les parenthèses
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    if (values.size() < 2) {
                        throw new CalculatorException("Expression invalide");
                    }
                    double b = values.pop();
                    double a = values.pop();
                    values.push(performOperation(a, b, operators.pop()));
                }
                if (!operators.isEmpty()) {
                    operators.pop(); // Retirer la parenthèse ouvrante
                } else {
                    throw new CalculatorException("Parenthèses mal équilibrées");
                }
            } else {
                // Opérateurs standards
                while (!operators.isEmpty() && !operators.peek().equals("(") && 
                       precedence(operators.peek()) >= precedence(token)) {
                    if (values.size() < 2) {
                        throw new CalculatorException("Expression invalide");
                    }
                    double b = values.pop();
                    double a = values.pop();
                    values.push(performOperation(a, b, operators.pop()));
                }
                operators.push(token);
            }
        }

        // Calculer les opérations restantes
        while (!operators.isEmpty()) {
            if (operators.peek().equals("(") || operators.peek().equals(")")) {
                throw new CalculatorException("Parenthèses mal équilibrées");
            }
            if (values.size() < 2) throw new CalculatorException("Expression invalide");

            double b = values.pop();
            double a = values.pop();
            String operator = operators.pop();
            double result = performOperation(a, b, operator);
            values.push(result);
        }

        if (values.isEmpty()) throw new CalculatorException("Expression vide");
        if (values.size() > 1) throw new CalculatorException("Expression invalide");

        double finalResult = values.pop();
        return new CalculationResult(finalResult, expression); // Crée un résultat avec l'expression
    }

    // Pattern précompilé comme constante de classe
    private static final Pattern TOKEN_PATTERN = Pattern.compile("(?<!\\d)-\\d+\\.?\\d*|\\d+\\.?\\d*|[+\\-x÷%()]");
        
    /**
     * Méthode pour convertir une expression mathématique en notation postfixée.
     * @param expression L'expression mathématique à convertir.
     * @return Une chaîne de caractères représentant l'expression en notation postfixée.
     */
    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        // Utiliser le Pattern précompilé
        Matcher matcher = TOKEN_PATTERN.matcher(expression);
        
        String previousToken = null;
        while (matcher.find()) {
            String token = matcher.group();
            
            // Multiplication implicite après une parenthèse fermante
            if (previousToken != null && previousToken.equals(")") && 
                token.matches("\\d+\\.?\\d*")) {
                tokens.add("x");
            }
            
            // Gestion des nombres négatifs entre parenthèses
            if (token.startsWith("(-") && token.endsWith(")")) {
                tokens.add("(");
                tokens.add(token.substring(1, token.length() - 1));
                tokens.add(")");
                previousToken = ")";
            } else {
                tokens.add(token);
                previousToken = token;
            }
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
            case "(":
            case ")":
                return 4;
            case "±":
                return 3;
            case "x":
            case "÷":
            case "%":
                return 2;
            case "+":
            case "-":
                return 1;
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
                if (b == Double.NEGATIVE_INFINITY) { // Valeur spéciale pour indiquer un opérateur unaire
                    return engine.percentOrModulo(a);
                }
                return engine.percentOrModulo(a, b);
            default:
                throw new CalculatorException("Opérateur non pris en charge: " + operator);
        }
    }
}