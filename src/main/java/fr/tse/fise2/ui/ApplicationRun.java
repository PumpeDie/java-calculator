package fr.tse.fise2.ui;

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
        // Utiliser SwingUtilities pour s'assurer que l'interface est créée dans l'EDT
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Créer une instance de ScientificCalculatorUI au lieu de CalculatorUI
            ScientificCalculatorUI calculator = new ScientificCalculatorUI();

            // Appelle la méthode pour créer et afficher l'interface graphique
            calculator.createAndShowGUI();
        });
    }
}