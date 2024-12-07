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
    public static final Pattern TOKEN_PATTERN = Pattern.compile(
        "(?<=[^\\d\\)])-\\d+\\.?\\d*|" +    // Nombres négatifs
        "\\d+\\.?\\d*|" +                    // Nombres positifs
        "[+\\-x÷%()^!]|" +                   // Opérateurs et parenthèses, y compris '%'
        "mod|" +                             // Opérateur modulo
        "sin|cos|tan|" +                      // Fonctions trigo
        "arcsin|arccos|arctan|" +             // Fonctions trigo inverses
        "ln|exp|sqrt|π"                        // Autres fonctions
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
        Matcher matcher = TOKEN_PATTERN.matcher(expression);
    
        // Étape 1 : Tokenisation initiale
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
    
        // Étape 2 : Remplacement conditionnel de '%' par 'mod'
        List<String> processedTokens = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
    
            if (token.equals("%")) {
                boolean isModulo = false;
                if (i > 0 && i < tokens.size() - 1) {
                    String prevToken = tokens.get(i - 1);
                    String nextToken = tokens.get(i + 1);
                    if (isNumeric(prevToken) && isNumeric(nextToken)) {
                        isModulo = true;
                    }
                }
    
                if (isModulo) {
                    processedTokens.add("mod");
                } else {
                    processedTokens.add("%");
                }
            } else {
                processedTokens.add(token);
            }
        }
    
        // Étape 3 : Gestion de la multiplication implicite et des parenthèses
        List<String> finalTokens = new ArrayList<>();
        String previousToken = null;
        for (String token : processedTokens) {
            // Multiplication implicite dans les cas suivants :
            if (previousToken != null) {
                // 1. Après un nombre
                if (previousToken.matches("-?\\d+\\.?\\d*")) {
                    if (token.equals("(") || token.equals("π") || isUnaryFunction(token)) {
                        finalTokens.add("x");
                    }
                }
                // 2. Après une parenthèse fermante
                else if (previousToken.equals(")")) {
                    if (token.matches("-?\\d+\\.?\\d*") || token.equals("π") || token.equals("(") || isUnaryFunction(token)) {
                        finalTokens.add("x");
                    }
                }
                // 3. Après π
                else if (previousToken.equals("π")) {
                    if (token.matches("-?\\d+\\.?\\d*") || token.equals("(") || isUnaryFunction(token)) {
                        finalTokens.add("x");
                    }
                }
                // 4. Après une fonction
                else if (isUnaryFunction(previousToken)) {
                    if (!token.equals("(")) {
                        finalTokens.add("x");
                    }
                }
            }
    
            // Gestion des nombres négatifs entre parenthèses
            if (token.startsWith("(-") && token.endsWith(")")) {
                finalTokens.add("(");
                finalTokens.add(token.substring(1, token.length() - 1));
                finalTokens.add(")");
                previousToken = ")";
            } else {
                finalTokens.add(token);
                previousToken = token;
            }
        }
    
        return finalTokens;
    }

    /** 
     * Méthode pour déterminer si une chaîne est un nombre
     * @param str La chaîne à vérifier
     * @return true si la chaîne est un nombre, false sinon
     */
    private boolean isNumeric(String str) {
        if (str.endsWith("%")) {
            str = str.substring(0, str.length() - 1);
        }
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
            case "x": case "÷": case "mod": case "%":
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
            case "%": return engine.percent(a);
            case "mod": return engine.modulo(a, b);
    
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
        List<String> tokens = tokenize(expression);
        System.out.println("Tokens: " + tokens);
        Stack<Double> values = new Stack<>();
        Stack<String> operators = new Stack<>();
    
        // Remplacer π par sa valeur
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("π")) {
                tokens.set(i, String.valueOf(Math.PI));
            }
        }
    
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
    
            if (isNumeric(token)) {
                double value = parseNumber(token);
                values.push(value);
            } else if (token.equals("%")) {
                if (values.isEmpty()) {
                    throw new CalculatorException("Pas de valeur pour le pourcentage ou le modulo");
                }
                // Traiter comme pourcentage unaire
                double value = values.pop();
                value = engine.percent(value);
                values.push(value);
            } else if (token.equals("mod")) {
                evaluateOperators("mod", values, operators);
            } else if (isUnaryFunction(token) || token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                evaluateParentheses(values, operators);
            } else {
                evaluateOperators(token, values, operators);
            }
        }
        System.out.println("Valeurs finales: " + values);
    
        // Évaluer les opérations restantes
        while (!operators.isEmpty()) {
            evaluateTopOperator(values, operators);
        }
    
        if (values.isEmpty()) throw new CalculatorException("Expression vide");
        if (values.size() > 1) throw new CalculatorException("Expression invalide");
    
        return new CalculationResult(values.pop(), expression);
    }

    private double parseNumber(String token) throws CalculatorException {
        boolean isPercentage = false;
        if (token.endsWith("%")) {
            isPercentage = true;
            token = token.substring(0, token.length() - 1);
        }
        double value;
        try {
            value = Double.parseDouble(token);
        } catch (NumberFormatException e) {
            throw new CalculatorException("Nombre invalide : " + token);
        }
        if (isPercentage) {
            value = engine.percent(value);
        }
        return value;
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

    private void evaluateOperators(String currentOp, Stack<Double> values, Stack<String> operators) throws CalculatorException {
        while (!operators.isEmpty() && !operators.peek().equals("(") &&
               precedence(operators.peek()) >= precedence(currentOp)) {
            evaluateTopOperator(values, operators);
        }
        operators.push(currentOp);
    }

    private void evaluateTopOperator(Stack<Double> values, Stack<String> operators) throws CalculatorException {
        if (operators.isEmpty())
            throw new CalculatorException("Expression invalide");
    
        String op = operators.pop();
    
        if (isUnaryFunction(op)) {
            if (values.isEmpty())
                throw new CalculatorException("Pas assez d'opérandes pour l'opérateur unaire " + op);
            double value = values.pop();
            double result = performOperation(value, 0, op);
            values.push(result);
        } else if (op.equals("!")) {
            if (values.isEmpty())
                throw new CalculatorException("Pas assez d'opérandes pour l'opérateur " + op);
            double value = values.pop();
            double result = performOperation(value, 0, op);
            values.push(result);
        } else { // op est un opérateur binaire (+, -, x, ÷, mod, etc.)
            if (values.size() < 2)
                throw new CalculatorException("Pas assez d'opérandes pour l'opérateur " + op);
            double b = values.pop();
            double a = values.pop();
            double result = performOperation(a, b, op);
            values.push(result);
        }
    }
}