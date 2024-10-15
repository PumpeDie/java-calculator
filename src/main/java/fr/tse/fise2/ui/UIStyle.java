package fr.tse.fise2.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Classe UIStyle qui gère les styles de mise en forme pour l'interface utilisateur Swing.
 */
public class UIStyle {

    /**
     * Couleurs prédéfinies pour les boutons.
     * @param button JButton
     * @param background Color
     * @param foreground Color
     * @param font Font
     */
    public static void styleButton(JButton button, Color background, Color foreground, Font font) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
    }

    /**
     * Couleurs prédéfinies pour le champ de texte.
     * @param textField JTextField
     * @param background Color
     * @param foreground Color
     * @param font Font
     * @param height int
     */
    public static void styleTextField(JTextField textField, Color background, Color foreground, Font font, int height) {
        textField.setBackground(background);
        textField.setForeground(foreground);
        textField.setFont(font);
        textField.setEditable(false); // Empêche la modification si nécessaire
        textField.setHorizontalAlignment(JTextField.RIGHT); // Alignement à droite
        textField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges internes
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, height)); // Définir la hauteur
    }

    /**
     * Police de caractère prédéfinie pour l'interface utilisateur.
     * @return Font
     */
    static Font getUIFont() {
        return new Font("Arial", Font.BOLD, 20);
    }

    /**
     * Couleurs prédéfinies pour le panneau.
     * @param panel JPanel
     * @param background Color
     */
    public static void stylePanel(JPanel panel, Color background) {
        panel.setBackground(background);
    }
}
