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

    // Nouveau pattern incluant toutes les fonctions scientifiques
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
        "(?<=[^\\d\\)])-\\d+\\.?\\d*|" +    // Nombres négatifs
        "\\d+\\.?\\d*|" +                    // Nombres positifs
        "[+\\-x÷%()^!]|" +                   // Opérateurs et parenthèses
        "sin|cos|tan|" +                     // Fonctions trigo
        "arcsin|arccos|arctan|" +            // Fonctions trigo inverses
        "ln|exp|sqrt|π"                      // Autres fonctions
    );

    public Calculator() {
        engine = new CalculatorEngine();
    }
        
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

    private boolean isUnaryFunction(String token) {
        return token.matches("sin|cos|tan|arcsin|arccos|arctan|ln|exp|sqrt");
    }

    // Méthode pour gérer la priorité des opérateurs
    private int precedence(String operator) {
        switch (operator) {
            case "sin": case "cos": case "tan":
            case "arcsin": case "arccos": case "arctan":
            case "ln": case "exp": case "sqrt":
            case "!":
                return 5; // Priorité la plus haute pour les fonctions
            case "^":
                return 4; // Priorité haute pour les puissances
            case "(": case ")":
                return 3;
            case "x": case "÷": case "%":
                return 2;
            case "+": case "-":
                return 1;
            default:
                return 0;
        }
    }

    // Méthode pour exécuter les opérations
    private double performOperation(double a, double b, String operator) throws CalculatorException {
        switch (operator) {
            // Opérations de base
            case "+": return engine.add(a, b);
            case "-": return engine.subtract(a, b);
            case "x": return engine.multiply(a, b);
            case "÷": return engine.divide(a, b);
            case "%": return b == Double.NEGATIVE_INFINITY ? 
                     engine.percentOrModulo(a) : engine.percentOrModulo(a, b);
            
            // Opérations scientifiques
            case "^": return engine.pow(a, b);
            case "sin": return engine.sin(a);
            case "cos": return engine.cos(a);
            case "tan": return engine.tan(a);
            case "arcsin": return engine.arcsin(a);
            case "arccos": return engine.arccos(a);
            case "arctan": return engine.arctan(a);
            case "ln": return engine.ln(a);
            case "exp": return engine.exp(a);
            case "sqrt": return engine.sqrt(a);
            case "!": return engine.factorial(a);
            default:
                throw new CalculatorException("Opérateur non pris en charge: " + operator);
        }
    }

    /**
     * Évalue l'expression mathématique donnée sous forme de chaîne.
     * @param expression La chaîne d'expression à évaluer.
     * @return Un objet CalculationResult contenant le résultat et les informations pertinentes.
     */
    public CalculationResult evaluateExpression(String expression) throws CalculatorException {
        // Remplacer π par sa valeur
        expression = expression.replace("π", String.valueOf(Math.PI));
        
        List<String> tokens = tokenize(expression);
        Stack<Double> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                values.push(Double.parseDouble(token));
            } else if (isUnaryFunction(token) || token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                evaluateParentheses(values, operators);
            } else {
                evaluateOperators(token, values, operators);
            }
        }

        // Évaluer les opérations restantes
        while (!operators.isEmpty()) {
            evaluateTopOperator(values, operators);
        }

        if (values.isEmpty()) throw new CalculatorException("Expression vide");
        if (values.size() > 1) throw new CalculatorException("Expression invalide");

        return new CalculationResult(values.pop(), expression);
    }

    private void evaluateParentheses(Stack<Double> values, Stack<String> operators) 
            throws CalculatorException {
        while (!operators.isEmpty() && !operators.peek().equals("(")) {
            evaluateTopOperator(values, operators);
        }
        if (!operators.isEmpty()) {
            operators.pop(); // Retirer la parenthèse ouvrante
            // Si une fonction unaire précède la parenthèse, l'évaluer
            if (!operators.isEmpty() && isUnaryFunction(operators.peek())) {
                String func = operators.pop();
                double value = values.pop();
                values.push(performOperation(value, 0, func));
            }
        } else {
            throw new CalculatorException("Parenthèses mal équilibrées");
        }
    }

    private void evaluateOperators(String currentOp, Stack<Double> values, 
            Stack<String> operators) throws CalculatorException {
        while (!operators.isEmpty() && !operators.peek().equals("(") && 
               precedence(operators.peek()) >= precedence(currentOp)) {
            evaluateTopOperator(values, operators);
        }
        operators.push(currentOp);
    }

    private void evaluateTopOperator(Stack<Double> values, Stack<String> operators) 
            throws CalculatorException {
        if (operators.isEmpty()) return;
        
        String op = operators.pop();
        if (isUnaryFunction(op)) {
            if (values.isEmpty()) throw new CalculatorException("Expression invalide");
            double a = values.pop();
            values.push(performOperation(a, 0, op));
        } else if (op.equals("!")) {
            if (values.isEmpty()) throw new CalculatorException("Expression invalide");
            double a = values.pop();
            values.push(engine.factorial(a));
        } else {
            if (values.size() < 2) throw new CalculatorException("Expression invalide");
            double b = values.pop();
            double a = values.pop();
            values.push(performOperation(a, b, op));
        }
    }
}