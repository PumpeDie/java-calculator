package fr.tse.fise2.controller;

import fr.tse.fise2.model.CalculationResult;
import fr.tse.fise2.model.Calculator;
import fr.tse.fise2.model.CalculatorException;
import fr.tse.fise2.ui.CalculatorUI;
import fr.tse.fise2.ui.ScientificCalculatorUI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller {
    private final CalculatorUI view;
    private StringBuilder currentInput;

    private static final String OPERATORS = "+-x÷";

    public Controller(CalculatorUI view) {
        this.view = view;
        this.currentInput = new StringBuilder();
    }
    
    private void updateDisplay(String text) {
        view.setDisplay(text);
    }

    private void updateExpression(String text) {
        view.setExpressionDisplay(text);
    }

    // Méthode pour gérer les entrées numériques
    public void handleInput(String command) {
        if (Character.isDigit(command.charAt(0)) || ".".equals(command)) {
            handleNumberInput(command);
        } else if (OPERATORS.contains(command)) {
            handleOperatorInput(command);
        } else {
            switch (command) {
                case "AC": handleAC(); break;
                case "←": handleBackspace(); break;
                case "±": handlePlusMinus(); break;
                case "%": handlePercent(); break;
                case "=": handleEquals(); break;
                case "Sci": handleScientificMode(); break;
                default: break;
            }
        }
    }

    private void handleNumberInput(String command) {
        if (command.equals(".")) {
            String currentExp = currentInput.toString();
            Pattern pattern = Pattern.compile("[+\\-x÷]?([^+\\-x÷]*)$");
            Matcher matcher = pattern.matcher(currentExp);

            if (matcher.find()) {
                String lastOperand = matcher.group(1);
                if (!lastOperand.contains(".")) {
                    if (lastOperand.isEmpty() || currentInput.length() == 0) {
                        currentInput.append("0.");
                    } else {
                        currentInput.append(".");
                    }
                }
            } else if (currentInput.length() == 0) {
                currentInput.append("0.");
            }
        } else {
            currentInput.append(command);
        }
        updateDisplay(currentInput.toString());
        view.setACButtonToBackspace();
    }

    private void handleOperatorInput(String command) {
        if (currentInput.length() > 0) {
            String lastToken = getLastToken();

            if (isOperator(lastToken)) {
                currentInput.setLength(currentInput.length() - 1);
            }
            currentInput.append(command);
            updateDisplay(currentInput.toString());
        }
    }

    private void handleAC() {
        currentInput.setLength(0);
        updateDisplay("0");
        updateExpression("");
        view.setACButtonToAC();
    }

    private void handleBackspace() {
        if (currentInput.length() > 0) {
            String lastToken = getLastCompleteToken();
            int newLength = currentInput.length() - lastToken.length();

            if (isNumeric(lastToken) && lastToken.length() > 1) {
                currentInput.deleteCharAt(currentInput.length() - 1);
            } else {
                currentInput.setLength(newLength);
            }

            updateDisplay(currentInput.length() > 0 ? currentInput.toString() : "0");

            if (currentInput.length() == 0) {
                view.setACButtonToAC();
            }
        }
    }

    private void handlePlusMinus() {
        if (currentInput.length() > 0) {
            String expression = currentInput.toString();

            // Si l'expression commence par un signe moins
            if (expression.startsWith("-")) {
                // Enlever le signe moins
                currentInput.setLength(0);
                currentInput.append(expression.substring(1));
                updateDisplay(currentInput.toString());
                return;
            }
    
            // Trouver le dernier opérande, avec ou sans parenthèses
            Pattern pattern = Pattern.compile("(.*?[+\\-x÷])?(\\(-?\\d+\\.?\\d*\\)|-?\\d+\\.?\\d*)$");
            Matcher matcher = pattern.matcher(expression);
    
            if (matcher.find()) {
                String prefix = matcher.group(1) != null ? matcher.group(1) : "";
                String number = matcher.group(2);
    
                // Vérifier si le nombre est entre parenthèses
                boolean isParenthesized = number.startsWith("(") && number.endsWith(")");
    
                // Enlever les parenthèses pour faciliter le traitement
                if (isParenthesized) {
                    number = number.substring(1, number.length() - 1);
                }
    
                // Basculement du signe
                if (number.startsWith("-")) {
                    // Enlever le signe négatif
                    number = number.substring(1);
                } else {
                    // Ajouter le signe négatif
                    number = "-" + number;
                }
    
                // Réappliquer les parenthèses si nécessaire
                if (number.startsWith("-")) {
                    number = "(" + number + ")";
                }
    
                currentInput.setLength(0);
                currentInput.append(prefix).append(number);
                updateDisplay(currentInput.toString());
            }
        }
    }

    private void handlePercent() {
        if (currentInput.length() > 0) {
            currentInput.append("%");
            updateDisplay(currentInput.toString());
        }
    }

    private void handleEquals() {
        if (currentInput.length() > 0) {
            try {
                String expression = currentInput.toString();
                expression = addMissingParentheses(expression);
                String result = evaluateExpression(expression);
                updateExpression(expression);
                updateDisplay(result);
                currentInput.setLength(0);
                currentInput.append(result);
                view.setACButtonToAC();
            } catch (CalculatorException ex) {
                updateDisplay("Erreur: " + ex.getMessage());
            }
        }
    }

    // Méthode pour gérer les entrées scientifiques
    public void handleScientificInput(String command) {
        // Reset si affichage est "0"
        if (currentInput.toString().equals("0")) {
            view.clearCurrentInput();
        }
        
        switch (command) {
            case "sin": case "cos": case "tan": 
            case "asin": case "acos": case "atan":
            case "ln": case "exp":
                currentInput.append(command).append("(");
                break;
            case "√":
                currentInput.append("sqrt(");
                break;
            case "x²":
                currentInput.append("^2");
                break;
            case "xʸ":
                currentInput.append("^");
                break;
            case "n!":
                currentInput.append("!");
                break;
            case "(": case ")": case "π":
                currentInput.append(command);
                break;
        }
        
        updateDisplay(currentInput.toString());
        view.setACButtonToBackspace();
    }

    public void toggleScientificMode() {
        if (view instanceof ScientificCalculatorUI) {
            ((ScientificCalculatorUI) view).toggleScientificMode();
        }
    }

    // Méthodes pour gérer les entrées clavier
    public class CalculatorKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            handleKeyPress(e);
        }
    }

    public void handleKeyPress(KeyEvent e) {
        char keyChar = e.getKeyChar();
        
        if (Character.isDigit(keyChar) || "+-.%".indexOf(keyChar) != -1) {
            simulateButtonClick(String.valueOf(keyChar));
        } else if (e.getKeyCode() == KeyEvent.VK_DIVIDE) {
            simulateButtonClick("÷");
        } else if (e.getKeyCode() == KeyEvent.VK_MULTIPLY) {
            simulateButtonClick("x");
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            simulateButtonClick("=");
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            simulateButtonClick("←");
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            view.closeWindow();
        }
    }
    
    public KeyAdapter getKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        };
    }

    public void simulateButtonClick(String buttonText) {
        // Simuler directement l'action sans passer par le bouton
        handleInput(buttonText);
    }

    // Méthodes utilitaires
    private String evaluateExpression(String expression) throws CalculatorException {
        Calculator calculator = new Calculator();
        CalculationResult result = calculator.evaluateExpression(expression);
        return result.getFormattedResult();
    }

    private String addMissingParentheses(String expression) {
        int openCount = 0;
        int closeCount = 0;

        for (char c : expression.toCharArray()) {
            if (c == '(') openCount++;
            if (c == ')') closeCount++;
        }

        StringBuilder balanced = new StringBuilder(expression);
        for (int i = 0; i < openCount - closeCount; i++) {
            balanced.append(")");
        }

        return balanced.toString();
    }

    private String getLastToken() {
        if (currentInput.length() == 0) return "";
        return String.valueOf(currentInput.charAt(currentInput.length() - 1));
    }

    private String getLastCompleteToken() {
        if (currentInput.length() == 0) return "";

        String input = currentInput.toString();
        Pattern tokenPattern = Calculator.TOKEN_PATTERN;
        Matcher matcher = tokenPattern.matcher(input);
        String lastToken = "";
        int lastStart = -1;

        while (matcher.find()) {
            lastToken = matcher.group();
            lastStart = matcher.start();
        }

        if (lastStart == -1 || lastStart + lastToken.length() < input.length()) {
            return input.substring(input.length() - 1);
        }

        return lastToken;
    }

    private boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void handleScientificMode() {
        toggleScientificMode();
    }
}
