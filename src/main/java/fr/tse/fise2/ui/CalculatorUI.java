package fr.tse.fise2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import fr.tse.fise2.calculator.CalculationResult;
import fr.tse.fise2.calculator.Calculator;

/**
 * Classe CalculatorUI qui gère l'interface utilisateur graphique (GUI) pour la calculatrice.
 * Utilise Swing pour afficher les boutons et le champ de texte.
 */
public class CalculatorUI {

    /** 
     * Champ de texte utilisé pour afficher les résultats de la calculatrice. 
     */
    private JTextField display;

    /** 
     * Panel contenant les boutons de la calculatrice.
     */
    private JPanel panel;

    /**
     * StringBuilder pour stocker l'entrée actuelle de l'utilisateur.
     */
    private StringBuilder currentInput;

    /**
     * Constructeur de la classe CalculatorUI.
     * Initialise le champ d'affichage et empêche sa modification directe par l'utilisateur.
     */
    public CalculatorUI() {
        display = new JTextField();
        display.setName("display");      // Définir un nom pour le champ JTextField
        display.setText("0");               // Afficher 0 par défaut
        currentInput = new StringBuilder();   // Initialise le StringBuilder pour l'entrée utilisateur
    }

    /**
    * Retourne le champ d'affichage de la calculatrice.
    * @return JTextField utilisé pour afficher les résultats.
    */
    public JTextField getDisplay() {
        return display;
    }

    /**
     * Crée l'interface graphique (GUI) et l'affiche.
     * Configure le JFrame principal, ajoute les boutons et configure la mise en page.
     */
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Calculatrice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);

        // Appliquer le style au champ d'affichage
        UIStyle.styleTextField(display, Color.BLACK, Color.WHITE, UIStyle.getUIFont(), 200);

        // Gérer les événements clavier pour les touches numériques et les opérateurs
        display.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                String command = String.valueOf(keyChar);
        
                // Vérifier si la touche est un chiffre ou un opérateur
                if (Character.isDigit(keyChar) || "+-*/.%".indexOf(keyChar) != -1 || e.getKeyCode() == KeyEvent.VK_DIVIDE || e.getKeyCode() == KeyEvent.VK_MULTIPLY) {
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
                    // Simuler un clic sur le bouton "AC" pour effacer
                    for (Component comp : panel.getComponents()) {
                        if (comp instanceof JButton) {
                            JButton button = (JButton) comp;
                            if (button.getText().equals("AC")) {
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
         
        // Créer un panel pour les boutons
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

            // Définir les styles des boutons en utilisant UIStyle
            if ("÷".equals(text) || "x".equals(text) || "-".equals(text) || "+".equals(text) || "=".equals(text)) {
                UIStyle.styleButton(button, Color.ORANGE, Color.WHITE, UIStyle.getUIFont());
            } else if ("AC".equals(text) || "±".equals(text) || "%".equals(text)) {
                UIStyle.styleButton(button, Color.LIGHT_GRAY, Color.WHITE, UIStyle.getUIFont());
            } else if ("Sci".equals(text) || text.matches("[0-9]") || ".".equals(text)) {
                UIStyle.styleButton(button, Color.DARK_GRAY, Color.WHITE, UIStyle.getUIFont());
            }

            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.add(display, BorderLayout.NORTH); // Affichage des résultats en haut
        frame.add(panel, BorderLayout.CENTER);  // Boutons au centre

        frame.setVisible(true);
    }

    /**
     * Classe interne ButtonClickListener qui gère les actions des boutons.
     * Implémente l'interface ActionListener pour écouter et traiter les clics sur les boutons.
     */
    private class ButtonClickListener implements ActionListener {
        /**
         * Méthode appelée lorsque l'utilisateur clique sur un bouton.
         * @param e L'événement de clic sur un bouton.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            
            // Traite l'action en fonction du texte du bouton
            switch (command) {
                case "AC":
                    // Réinitialise l'entrée utilisateur
                    currentInput.setLength(0);
                    display.setText("0");
                    break;
    
                case "±":
                    // Change le signe du nombre actuel
                    if (currentInput.length() > 0) {
                        double value = Double.parseDouble(currentInput.toString());
                        value = -value; // Inverse le signe
                        currentInput.setLength(0);
                        currentInput.append(value);
                        display.setText(currentInput.toString());
                    }
                    break;
    
                case "%":
                    // Ajoute le pourcentage à l'entrée utilisateur
                    if (currentInput.length() > 0) {
                        currentInput.append(" %");
                        display.setText(currentInput.toString());
                    }
                    break;
    
                case "÷":
                    // Ajoute l'opérateur à l'entrée utilisateur
                    if (currentInput.length() > 0) {
                        currentInput.append(" " + command + " "); // Ajoute l'opérateur avec des espaces pour la lisibilité
                        display.setText(currentInput.toString());
                    }
                    break;

                case "x":
                    // Ajoute l'opérateur à l'entrée utilisateur
                    if (currentInput.length() > 0) {
                        currentInput.append(" " + command + " "); // Ajoute l'opérateur avec des espaces pour la lisibilité
                        display.setText(currentInput.toString());
                    }
                    break;

                case "-":
                    // Ajoute l'opérateur à l'entrée utilisateur
                    if (currentInput.length() > 0) {
                        currentInput.append(" " + command + " "); // Ajoute l'opérateur avec des espaces pour la lisibilité
                        display.setText(currentInput.toString());
                    }
                    break;

                case "+":
                    // Ajoute l'opérateur à l'entrée utilisateur
                    if (currentInput.length() > 0) {
                        currentInput.append(" " + command + " "); // Ajoute l'opérateur avec des espaces pour la lisibilité
                        display.setText(currentInput.toString());
                    }
                    break;
    
                case "Sci":
                    // Affiche la calculatrice scientifique
                    // À définir
                    break;
    
                case "=":
                    // Ici, on fait appel à engine pour calculer le résultat
                    if (currentInput.length() > 0) {
                        // Extraire et évaluer l'expression de currentInput
                        String result = evaluateExpression(currentInput.toString());
                        currentInput.setLength(0); // Réinitialiser l'entrée après le calcul
                        currentInput.append(result); // Conserver le résultat pour affichage futur
                        display.setText(currentInput.toString());
                    }
                    break;
    
                default:
                    // Ajoute le texte du bouton à l'entrée utilisateur
                if (Character.isDigit(command.charAt(0)) || command.equals(".")) {
                    // Vérifie si le point décimal n'est pas déjà présent
                    if (command.equals(".")) {
                        if (currentInput.length() == 0 || currentInput.toString().contains(" ")) {
                            currentInput.append("0."); // Ajoute 0. si c'est le premier point décimal
                        } else if (!currentInput.toString().contains(".")) {
                            currentInput.append(command); // Ajoute le point décimal
                        }
                    } else {
                        currentInput.append(command); // Ajoute le chiffre
                    }
                    display.setText(currentInput.toString());
                }
                break;
            }
        }

        /**
         * Évalue l'expression mathématique contenue dans la chaîne.
         * @param expression La chaîne d'expression à évaluer.
         * @return Le résultat du calcul.
         */
        private String evaluateExpression(String expression) {
            Calculator calculator = new Calculator();
            CalculationResult result = calculator.evaluateExpression(expression);
            return result.getFormattedResult();
        }
    }
}
