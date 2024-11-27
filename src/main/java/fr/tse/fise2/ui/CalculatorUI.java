package fr.tse.fise2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

import fr.tse.fise2.calculator.Calculator;
import fr.tse.fise2.calculator.CalculationResult;
import fr.tse.fise2.calculator.CalculatorException;

/**
 * Classe CalculatorUI qui gère l'interface utilisateur graphique (GUI) pour la calculatrice.
 * Utilise Swing pour afficher les boutons et le champ de texte.
 */
public class CalculatorUI {

    // Champ de texte utilisé pour afficher les résultats de la calculatrice.
    private JTextField display;

    // Champ de texte pour afficher l'expression validé de l'utilisateur.
    private JTextField expressionDisplay;

    // Panel contenant les boutons de la calculatrice.
    private JPanel panel;

    // StringBuilder pour stocker l'entrée actuelle de l'utilisateur.
    private StringBuilder currentInput;

    // Bouton pour effacer l'entrée utilisateur.
    private JButton acButton;

    // Constructeur de la classe CalculatorUI.
    public CalculatorUI() {
        display = new JTextField();                          // Créer un champ JTextField pour afficher les résultats
        display.setName("display");                     // Définir un nom pour le champ JTextField
        display.setText("0");                              // Afficher 0 par défaut
        expressionDisplay = new JTextField();                // Créer un champ JTextField pour afficher l'expression
        expressionDisplay.setName("expressionDisplay"); // Définir un nom pour le champ JTextField
        expressionDisplay.setEditable(false);              // Empêcher l'édition de l'expression
        currentInput = new StringBuilder();                  // Initialise le StringBuilder pour l'entrée utilisateur

        // Initialiser le panneau de boutons
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5)); // Grille 5x4 pour les boutons avec un espacement de 5px

        // Ajouter les boutons
        String[] buttons = {
            "AC", "±", "%", "÷",
            "7", "8", "9", "x",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "Sci", "0", ".", "="
        };

        // Boucle pour créer et ajouter les boutons au panel
        for (String text : buttons) {
            JButton button = new JButton(text);
            // Garder une référence au bouton "AC"
            if ("AC".equals(text)) {
                acButton = button;
            }
            // Définir les styles des boutons en utilisant UIStyle
            if ("÷".equals(text) || "x".equals(text) || "-".equals(text) || "+".equals(text) || "=".equals(text)) {
                UIStyle.styleButton(button, Color.ORANGE, Color.WHITE, UIStyle.getUIFont());
            } else if ("AC".equals(text) || "±".equals(text) || "%".equals(text)) {
                UIStyle.styleButton(button, Color.LIGHT_GRAY, Color.WHITE, UIStyle.getUIFont());
            } else if ("Sci".equals(text) || text.matches("[0-9]") || ".".equals(text)) {
                UIStyle.styleButton(button, Color.DARK_GRAY, Color.WHITE, UIStyle.getUIFont());
            }

            // Définir les noms des boutons pour les tests
            if ("←".equals(text)) {
                button.setName("backspace");
            } else if ("±".equals(text)) {
                button.setName("plusMinus");
            }

            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }
    }

    // Getters pour les champs de texte et le panneau de boutons
    public JTextField getDisplay() {
        return display;
    }

    public JPanel getPanel() {
        return panel;
    }

    /**
     * Crée l'interface graphique (GUI) et l'affiche.
     * Configure le JFrame principal, ajoute les boutons et configure la mise en page.
     */
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Calculatrice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);

        // Appliquer le style au champ d'affichage et au champ d'expression
        UIStyle.styleTextField(display, Color.BLACK, Color.WHITE, UIStyle.getUIFont(), 50);
        UIStyle.styleTextField(expressionDisplay, Color.BLACK, Color.LIGHT_GRAY, new Font(UIStyle.getUIFont().getName(), Font.BOLD, 14), 50);

        // Gérer les événements clavier pour les touches numériques et les opérateurs
        display.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                String command = String.valueOf(keyChar);
        
                // Vérifier si la touche est un chiffre ou un opérateur
                if (Character.isDigit(keyChar) || "+-.%".indexOf(keyChar) != -1 || e.getKeyCode() == KeyEvent.VK_DIVIDE || e.getKeyCode() == KeyEvent.VK_MULTIPLY) {
                    // Simuler un clic sur le bouton correspondant
                    for (Component comp : panel.getComponents()) {
                        if (comp instanceof JButton) {
                            JButton button = (JButton) comp;
                            // Gestion spéciale pour '*' et '/'
                            if (e.getKeyCode() == KeyEvent.VK_DIVIDE && button.getText().equals("÷")) {
                                button.doClick();
                                System.out.println("Key pressed: " + keyChar);
                                break;
                            } else if (e.getKeyCode() == KeyEvent.VK_MULTIPLY && button.getText().equals("x")) {
                                button.doClick();
                                System.out.println("Key pressed: " + keyChar);
                                break;
                            } else if (button.getText().equals(command)) {
                                button.doClick();
                                System.out.println("Key pressed: " + keyChar);
                                break;
                            }
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Simuler un clic sur le bouton "="
                    for (Component comp : panel.getComponents()) {
                        if (comp instanceof JButton) {
                            JButton button = (JButton) comp;
                            if (button.getText().equals("=")) {
                                button.doClick();
                                break;
                            }
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    // Simuler un clic sur le bouton "←" pour effacer
                    for (Component comp : panel.getComponents()) {
                        if (comp instanceof JButton) {
                            JButton button = (JButton) comp;
                            if (button.getText().equals("←")) {
                                button.doClick();
                                break;
                            }
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    // Fermer l'application avec la touche ÉCHAP
                    System.out.println("Fermeture de l'application");
                    System.exit(0);
                }
            }
        });

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        UIStyle.stylePanel(displayPanel, Color.BLACK);

        displayPanel.add(expressionDisplay);
        displayPanel.add(display);

        frame.add(displayPanel, BorderLayout.NORTH); // Affichage des résultats en haut
        frame.add(panel, BorderLayout.CENTER);  // Boutons au centre

        frame.setVisible(true);
    }

    /**
     * Classe interne ButtonClickListener qui gère les actions des boutons.
     * Implémente l'interface ActionListener pour écouter et traiter les clics sur les boutons.
     */
    private class ButtonClickListener implements ActionListener {

        // Méthode pour évaluer l'expression donnée et renvoyer le résultat formaté.
        private String evaluateExpression(String expression) throws CalculatorException {
            Calculator calculator = new Calculator();
            CalculationResult result = calculator.evaluateExpression(expression);
            return result.getFormattedResult();
        }

        // Méthode utilitaire pour vérifier si c'est un opérateur
        private boolean isOperator(String token) {
            return "+-x÷".contains(token);
        }

        // Méthode pour obtenir le dernier token de l'expression
        private String getLastToken() {
            if (currentInput.length() == 0) {
                return "";
            }
            return String.valueOf(currentInput.charAt(currentInput.length() - 1));
        }
        
        // Méthode pour gérer les actions des boutons
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            
            // Traite l'action en fonction du texte du bouton
            switch (command) {
                case "AC":
                    // Réinitialise l'entrée utilisateur
                    currentInput.setLength(0);
                    display.setText("0");
                    expressionDisplay.setText("");
                    acButton.setText("AC");
                    break;
    
                case "←":
                    // Supprime le dernier élément de currentInput
                    if (currentInput.length() > 0) {
                        currentInput.deleteCharAt(currentInput.length() - 1);
                        display.setText(currentInput.length() > 0 ? currentInput.toString() : "0");
                        // Si currentInput est vide, changer le bouton à "AC"
                        if (currentInput.length() == 0) {
                            acButton.setText("AC");
                        }
                    }
                    break;

                case "±":
                    // Change le signe du nombre actuel
                    if (currentInput.length() > 0) {
                        String expression = currentInput.toString();
                        
                        // Cas 1: Simple nombre (y compris résultat d'opération)
                        if (expression.matches("-?\\d+\\.?\\d*")) {
                            double value = Double.parseDouble(expression);
                            currentInput.setLength(0);
                            // Créer un CalculationResult temporaire pour le formatage
                            CalculationResult temp = new CalculationResult(value < 0 ? -value : value, expression);
                            String formattedValue = temp.getFormattedResult();
                            currentInput.append(value < 0 ? formattedValue : "(-" + formattedValue + ")");
                            display.setText(currentInput.toString());
                            return;
                        }
                        
                        // Cas 2: Expression avec opérateur à la fin
                        Pattern opPattern = Pattern.compile("(.*[+\\-x÷])-?\\d+\\.?\\d*$");
                        Matcher opMatcher = opPattern.matcher(expression);
                        if (opMatcher.matches()) {
                            String prefix = opMatcher.group(1);
                            String number = expression.substring(prefix.length());
                            currentInput.setLength(0);
                            if (number.startsWith("-")) {
                                currentInput.append(prefix).append(number.substring(1));
                            } else {
                                currentInput.append(prefix).append("(-").append(number).append(")");
                            }
                            display.setText(currentInput.toString());
                            return;
                        }
                        
                        // Cas 3: Expression avec parenthèses
                        if (expression.contains("(")) {
                            if (expression.startsWith("(-")) {
                                // Enlever les parenthèses
                                currentInput.setLength(0);
                                currentInput.append(expression.substring(2, expression.length() - 1));
                            } else {
                                // Ajouter les parenthèses
                                currentInput.setLength(0);
                                currentInput.append("(-").append(expression).append(")");
                            }
                            display.setText(currentInput.toString());
                        }
                    }
                    break;
    
                case "%":
                    // Ajoute le pourcentage à l'entrée utilisateur
                    if (currentInput.length() > 0) {
                        currentInput.append("%");
                        display.setText(currentInput.toString());
                    }
                    break;
    
                case "+": case "-": case "x": case "÷":
                    if (currentInput.length() > 0) {
                        String lastToken = getLastToken();
                        
                        // Si le dernier token est un opérateur, le remplacer
                        if (isOperator(lastToken)) {
                            currentInput.setLength(currentInput.length() - 1);
                            currentInput.append(command);
                        } 
                        // Sinon, ajouter le nouvel opérateur si ce n'est pas une parenthèse fermante
                        else if (!lastToken.equals(")")) {
                            currentInput.append(command);
                        }
                        display.setText(currentInput.toString());
                    }
                    break;
    
                case "Sci":
                    // Affiche la calculatrice scientifique
                    // À définir
                    break;
    
                case "=":
                    if (currentInput.length() > 0) {
                        try {
                            String expression = currentInput.toString();
                            String result = evaluateExpression(expression);
                            // Mettre à jour l'affichage de l'expression
                            expressionDisplay.setText(expression);
                            display.setText(result);
                            currentInput.setLength(0);
                            currentInput.append(result);
                            // Changer le bouton en AC après le calcul
                            acButton.setText("AC");
                        } catch (CalculatorException ex) {
                            display.setText("Erreur: " + ex.getMessage());
                        }
                    }
                    break;
    
                default:
                    // Ajoute le chiffre ou le point décimal à l'entrée utilisateur
                    if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
                        if (command.equals(".")) {
                            String currentExp = currentInput.toString();
                            // Extraire la dernière opérande (nombre après le dernier opérateur)
                            Pattern pattern = Pattern.compile("[+\\-x÷]?([^+\\-x÷]*)$");
                            Matcher matcher = pattern.matcher(currentExp);
                            
                            if (matcher.find()) {
                                String lastOperand = matcher.group(1);
                                // Vérifier si la dernière opérande contient déjà un point
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
                            currentInput.append(command); // Ajoute le chiffre
                        }
                        display.setText(currentInput.toString());
                        acButton.setText("←"); // Changer le texte du bouton "AC" en "←"
                    }
                    break;
            }
        }
    }
}
