package fr.tse.fise2.ui;

import fr.tse.fise2.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Classe CalculatorUI qui gère l'interface utilisateur graphique pour la calculatrice.
 * Cette classe est responsable de la création des composants de l'interface utilisateur
 * et de la gestion des événements des boutons.
 * Cette classe est conçue pour être étendue par une classe ScientificCalculatorUI
 * qui ajoute des fonctionnalités supplémentaires à la calculatrice.
 */
public class CalculatorUI {
    // Contrôleur pour gérer les événements des boutons
    private Controller controller;

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

    // Constructeur
    public CalculatorUI() {
        currentInput = new StringBuilder();
        controller = new Controller(this);
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
        display.addKeyListener(controller.getKeyListener());

        frame.setVisible(true);

        display.requestFocusInWindow();
        SwingUtilities.invokeLater(() -> display.requestFocusInWindow());
    }

    public void closeWindow() {
        Window window = SwingUtilities.getWindowAncestor(panel);
        window.dispose();
    }
    
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            controller.handleInput(command);
        }
    }
    
    // Getters et setters
    public JPanel getPanel() {
        return panel;
    }
    public JTextField getDisplay() {
        return display;
    }

    public StringBuilder getCurrentInput() {
        return currentInput;
    }

    public JButton getAcButton() {
        return acButton;
    }

    public Controller getController() {
        return controller;
    }

    public void setDisplay(String text) {
        display.setText(text);
    }

    public void setCurrentInput(String text) {
        currentInput.setLength(0);
        currentInput.append(text);
    }

    public void setExpressionDisplay(String text) {
        expressionDisplay.setText(text);
    }
    public void setACButtonToBackspace() {
        acButton.setText("←");
    }

    public void setACButtonToAC() {
        acButton.setText("AC");
    }

    public void clearCurrentInput() {
        currentInput.setLength(0);
    }

    public void appendToCurrentInput(String text) {
        currentInput.append(text);
    }
}