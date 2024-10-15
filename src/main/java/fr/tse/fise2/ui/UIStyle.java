package fr.tse.fise2.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Classe UIStyle qui gère les styles de mise en forme pour l'interface utilisateur Swing.
 */
public class UIStyle {

    /**
     * 
     * @param button
     * @param background
     * @param foreground
     * @param font
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
     * 
     * @param textField
     * @param background
     * @param foreground
     * @param font
     */
    public static void styleTextField(JTextField textField, Color background, Color foreground, Font font) {
        textField.setBackground(background);
        textField.setForeground(foreground);
        textField.setFont(font);
        textField.setEditable(false); // Empêche la modification si nécessaire
        textField.setHorizontalAlignment(JTextField.RIGHT); // Alignement à droite
        textField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges internes
    }

    /**
     * 
     * @return
     */
    public static Font getUIFont() {
        return new Font("Arial", Font.BOLD, 20);
    }

    /**
     * 
     * @param panel
     * @param background
     */
    public static void stylePanel(JPanel panel, Color background) {
        panel.setBackground(background);
    }
}
