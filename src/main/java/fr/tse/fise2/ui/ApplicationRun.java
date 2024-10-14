package fr.tse.fise2.ui;

import fr.tse.fise2.ui.CalculatorUI;

/**
 * Classe ApplicationRun qui sert de point d'entrée pour l'application de calculatrice.
 */
public class ApplicationRun {
    
        /**
        * Point d'entrée de l'application.
        * Crée une instance de CalculatorUI et affiche l'interface graphique.
        * @param args Arguments de la ligne de commande (non utilisés)
        */
        public static void main(String[] args) {
            // Crée une instance de CalculatorUI
            CalculatorUI calculatorUI = new CalculatorUI();

            // Appelle la méthode pour créer et afficher l'interface graphique
            calculatorUI.createAndShowGUI();
        }
}