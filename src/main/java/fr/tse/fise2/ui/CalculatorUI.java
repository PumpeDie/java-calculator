package fr.tse.fise2.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
     * Constructeur de la classe CalculatorUI.
     * Initialise le champ d'affichage et empêche sa modification directe par l'utilisateur.
     */
    public CalculatorUI() {
        display = new JTextField();
        display.setText("0"); // Initialise l'affichage à 0
        display.setEditable(false); // Empêche la modification directe du texte
    }

    /**
     * Crée l'interface graphique (GUI) et l'affiche.
     * Configure le JFrame principal, ajoute les boutons et configure la mise en page.
     */
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Calculatrice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);

        // Créer un panel pour les boutons
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5)); // Grille 5x4 pour les boutons avec un espacement de 5px

        // Ajouter les boutons
        String[] buttons = {
            "C", "±", "%", "÷",
            "7", "8", "9", "x",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "Sci", "0", ".", "="
        };

        // Boucle pour créer et ajouter les boutons au panel
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.addActionListener(new ButtonClickListener()); // Ajoute un écouteur d'événements
            panel.add(button); // Ajoute le bouton au panel
        }

        // Ajoute les composants au JFrame
        frame.add(display, BorderLayout.NORTH); // Affichage des résultats en haut
        frame.add(panel, BorderLayout.CENTER);  // Boutons au centre

        frame.setVisible(true); // Rend la fenêtre visible
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
            // Logique d'interaction avec le moteur de calcul viendra ici
        }
    }
}
