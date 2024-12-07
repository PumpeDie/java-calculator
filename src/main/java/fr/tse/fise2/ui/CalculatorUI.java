package fr.tse.fise2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

import fr.tse.fise2.calculator.Calculator;
import fr.tse.fise2.calculator.CalculationResult;
import fr.tse.fise2.calculator.CalculatorException;

/**
 * Classe CalculatorUI qui gère l'interface utilisateur graphique pour la calculatrice.
 * Cette classe est responsable de la création des composants de l'interface utilisateur
 * et de la gestion des événements des boutons.
 * Cette classe est conçue pour être étendue par une classe ScientificCalculatorUI
 * qui ajoute des fonctionnalités supplémentaires à la calculatrice.
 */
public class CalculatorUI {

    // Constantes pour les labels des boutons et les opérateurs
    private static final String[] BUTTON_LABELS = {
        "AC", "±", "%", "÷",
        "7", "8", "9", "x",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "Sci", "0", ".", "="
    };
    private static final String OPERATORS = "+-x÷";

    // Composants de l'interface utilisateur
    private JTextField display;
    private JTextField expressionDisplay;
    private JPanel panel;
    private JButton acButton;

    // Stockage de l'entrée utilisateur
    private StringBuilder currentInput;

    // Gestion de l'affichage
    private DisplayManager displayManager;

    // Constructeur
    public CalculatorUI() {
        currentInput = new StringBuilder();
        initComponents();
    }

    /**
     * Initialiser les composants de l'interface utilisateur.
     * Créer les champs d'affichage, les boutons et les gestionnaires d'événements.
     */
    private void initComponents() {
        // Initialiser les champs d'affichage
        display = new JTextField();
        display.setName("display");
        display.setEditable(false);
        display.setText("0");

        expressionDisplay = new JTextField();
        expressionDisplay.setName("expressionDisplay");
        expressionDisplay.setEditable(false);

        // Appliquer les styles
        UIStyle.styleTextField(display, Color.BLACK, Color.WHITE, UIStyle.getUIFont(), 50);
        UIStyle.styleTextField(expressionDisplay, Color.BLACK, Color.LIGHT_GRAY, new Font(UIStyle.getUIFont().getName(), Font.BOLD, 14), 50);

        // Gestionnaire d'affichage
        displayManager = new DisplayManager(display, expressionDisplay);

        // Initialiser le panneau des boutons
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));
        UIStyle.stylePanel(panel, Color.BLACK);

        initializeButtons();
    }

    // Initialiser les boutons de l'interface utilisateur
    private void initializeButtons() {
        for (String text : BUTTON_LABELS) {
            JButton button = createButton(text);
            panel.add(button);
            if ("AC".equals(text)) {
                acButton = button;
            }
        }
    }

    // Créer un bouton avec le texte spécifié et ajouter un gestionnaire d'événements
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, text);
        button.addActionListener(new ButtonClickListener());
        return button;
    }

    // Appliquer le style approprié en fonction du texte du bouton
    private void styleButton(JButton button, String text) {
        if (OPERATORS.contains(text) || "=".equals(text)) {
            UIStyle.styleButton(button, Color.ORANGE, Color.WHITE, UIStyle.getUIFont());
        } else if ("AC±%".contains(text)) {
            UIStyle.styleButton(button, Color.LIGHT_GRAY, Color.WHITE, UIStyle.getUIFont());
        } else {
            UIStyle.styleButton(button, Color.DARK_GRAY, Color.WHITE, UIStyle.getUIFont());
        }
    }

    // Créer et afficher l'interface graphique de la calculatrice
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Calculatrice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setResizable(false);

        // Panneau d'affichage
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        UIStyle.stylePanel(displayPanel, Color.BLACK);
        displayPanel.add(expressionDisplay);
        displayPanel.add(display);

        frame.add(displayPanel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        // Gestion des événements clavier
        display.addKeyListener(new CalculatorKeyListener());

        frame.setVisible(true);
    }

    // Classe pour gérer l'affichage des champs de texte de la calculatrice 
    private class DisplayManager {
        private final JTextField display;
        private final JTextField expressionDisplay;

        public DisplayManager(JTextField display, JTextField expressionDisplay) {
            this.display = display;
            this.expressionDisplay = expressionDisplay;
        }

        void updateDisplay(String text) {
            display.setText(text);
        }

        void updateExpression(String expression) {
            expressionDisplay.setText(expression);
        }
    }

    // Gestion des entrées clavier
    private class CalculatorKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            handleKeyPress(e);
        }
    }

    // Méthode pour gérer les entrées clavier et simuler les clics de bouton
    private void handleKeyPress(KeyEvent e) {
        char keyChar = e.getKeyChar();
        String command = String.valueOf(keyChar);

        if (Character.isDigit(keyChar) || "+-.%".indexOf(keyChar) != -1 ||
            e.getKeyCode() == KeyEvent.VK_DIVIDE || e.getKeyCode() == KeyEvent.VK_MULTIPLY) {

            for (Component comp : panel.getComponents()) {
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    if (e.getKeyCode() == KeyEvent.VK_DIVIDE && button.getText().equals("÷")) {
                        button.doClick();
                        break;
                    } else if (e.getKeyCode() == KeyEvent.VK_MULTIPLY && button.getText().equals("x")) {
                        button.doClick();
                        break;
                    } else if (button.getText().equals(command)) {
                        button.doClick();
                        break;
                    }
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            simulateButtonClick("=");
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            simulateButtonClick("←");
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    // Méthode pour simuler un clic de bouton
    private void simulateButtonClick(String buttonText) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals(buttonText)) {
                    button.doClick();
                    break;
                }
            }
        }
    }

    /**
     * Classe interne pour gérer les événements des boutons de la calculatrice.
     * Cette classe implémente l'interface ActionListener pour écouter les événements de clic de bouton.
     * Elle implémente la méthode actionPerformed pour gérer les événements de clic de bouton.
     * Cette classe gère les événements pour les boutons numériques, les opérateurs et les fonctions.
     * Elle met à jour l'affichage de la calculatrice en fonction de l'entrée de l'utilisateur.
     * Elle gère également les événements pour les boutons spéciaux tels que AC, ←, ±, % et =.
     * Elle utilise les méthodes de la classe Calculator pour évaluer les expressions mathématiques.
     * Elle gère également les événements pour le mode scientifique de la calculatrice.
     * Cette classe est conçue pour être étendue par une classe ScientificButtonClickListener
     * qui ajoute des fonctionnalités supplémentaires à la calculatrice.
     */
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (Character.isDigit(command.charAt(0)) || ".".equals(command)) {
                handleNumberInput(command);
            } else if (OPERATORS.contains(command)) {
                handleOperatorInput(command);
            } else {
                switch (command) {
                    case "AC":
                        handleAC();
                        break;
                    case "←":
                        handleBackspace();
                        break;
                    case "±":
                        handlePlusMinus();
                        break;
                    case "%":
                        handlePercent();
                        break;
                    case "=":
                        handleEquals();
                        break;
                    case "Sci":
                        handleScientificMode();
                        break;
                    default:
                        break;
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
            displayManager.updateDisplay(currentInput.toString());
            acButton.setText("←");
        }

        private void handleOperatorInput(String command) {
            if (currentInput.length() > 0) {
                String lastToken = getLastToken();

                if (isOperator(lastToken)) {
                    currentInput.setLength(currentInput.length() - 1);
                }
                currentInput.append(command);
                displayManager.updateDisplay(currentInput.toString());
            }
        }

        private void handleAC() {
            currentInput.setLength(0);
            displayManager.updateDisplay("0");
            displayManager.updateExpression("");
            acButton.setText("AC");
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

                displayManager.updateDisplay(currentInput.length() > 0 ? currentInput.toString() : "0");

                if (currentInput.length() == 0) {
                    acButton.setText("AC");
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
                    displayManager.updateDisplay(currentInput.toString());
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
                    displayManager.updateDisplay(currentInput.toString());
                }
            }
        }

        private void handlePercent() {
            if (currentInput.length() > 0) {
                currentInput.append("%");
                displayManager.updateDisplay(currentInput.toString());
            }
        }

        private void handleEquals() {
            if (currentInput.length() > 0) {
                try {
                    String expression = currentInput.toString();
                    expression = addMissingParentheses(expression);
                    String result = evaluateExpression(expression);
                    displayManager.updateExpression(expression);
                    displayManager.updateDisplay(result);
                    currentInput.setLength(0);
                    currentInput.append(result);
                    acButton.setText("AC");
                } catch (CalculatorException ex) {
                    displayManager.updateDisplay("Erreur: " + ex.getMessage());
                }
            }
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
    }
    
    // Getters et setters
    protected JTextField getDisplay() {
        return display;
    }

    protected void setDisplay(String text) {
        display.setText(text);
    }

    protected StringBuilder getCurrentInput() {
        return currentInput;
    }

    protected void setCurrentInput(String text) {
        currentInput.setLength(0);
        currentInput.append(text);
    }

    protected void clearCurrentInput() {
        currentInput.setLength(0);
    }

    protected void appendToCurrentInput(String text) {
        currentInput.append(text);
    }

    protected JButton getAcButton() {
        return acButton;
    }

    protected JPanel getPanel() {
        return panel;
    }

    protected void handleScientificMode() {
        // Méthode à surcharger dans ScientificCalculatorUI
    }
}