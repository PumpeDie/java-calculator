package fr.tse.fise2.ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ScientificCalculatorUI extends CalculatorUI {
    private JPanel scientificPanel;
    private boolean isScientificMode = false;

    private static final String[] SCIENTIFIC_BUTTONS = {
        "sin", "cos", "tan",
        "asin", "acos", "atan",
        "ln", "exp", "n!",
        "√", "x²", "xʸ",
        "π", "(" , ")",
    };

    public ScientificCalculatorUI() {
        super();
        initializeScientificPanel();
    }

    private void initializeScientificPanel() {
        scientificPanel = new JPanel();
        scientificPanel.setLayout(new GridLayout(5, 3, 5, 5));
        
        // Style du panel scientifique
        UIStyle.stylePanel(scientificPanel, Color.BLACK);
        scientificPanel.setPreferredSize(new Dimension(300, 600));
        
        for (String buttonText : SCIENTIFIC_BUTTONS) {
            JButton button = new JButton(buttonText);
            
            // Appliquer les mêmes styles que la calculatrice de base
            if ("sin".equals(buttonText) || "cos".equals(buttonText) || "tan".equals(buttonText) || "asin".equals(buttonText) || "acos".equals(buttonText) || "atan".equals(buttonText)) {
                // Style orange comme les opérateurs
                UIStyle.styleButton(button, Color.ORANGE, Color.WHITE, UIStyle.getUIFont());
            } else {
                // Style gris foncé
                UIStyle.styleButton(button, Color.DARK_GRAY, Color.WHITE, UIStyle.getUIFont());
            }
            
            button.addActionListener(new ScientificButtonClickListener());
            scientificPanel.add(button);
        }
        scientificPanel.setVisible(false);
    }

    @Override
    public void createAndShowGUI() {
        super.createAndShowGUI();
        
        // Créer un conteneur pour organiser les panels
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BorderLayout(5, 5));
        UIStyle.stylePanel(mainContainer, Color.BLACK);
        
        // Déplacer les composants existants
        Container parent = getPanel().getParent();
        parent.remove(getPanel());
        
        // Ajouter les panels au conteneur principal avec scientificPanel à GAUCHE
        mainContainer.add(scientificPanel, BorderLayout.WEST);
        mainContainer.add(getPanel(), BorderLayout.CENTER);
        
        // Ajouter le conteneur principal à la frame
        parent.add(mainContainer);
        
        // Initialiser en mode non-scientifique
        scientificPanel.setVisible(false);
    }

    @Override
    protected void handleScientificMode() {
        toggleScientificMode();
    }

    private void toggleScientificMode() {
        isScientificMode = !isScientificMode;
        scientificPanel.setVisible(isScientificMode);
        
        Window window = SwingUtilities.getWindowAncestor(getPanel());
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            frame.setSize(isScientificMode ? 700 : 400, 600);
            
            // Forcer la mise à jour de la disposition
            frame.revalidate();
            frame.repaint();
        }
    }

    private class ScientificButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField display = getDisplay(); // Utiliser le getter existant
            String command = e.getActionCommand();
            String currentText = display.getText();
            StringBuilder input = new StringBuilder(currentText);
            
            switch (command) {
                case "sin": case "cos": case "tan": 
                case "asin": case "acos": case "atan":
                case "ln":
                    input.append(command).append("(");
                    break;
                case "√":
                    input.append("sqrt(");
                    break;
                case "x²":
                    input.append("^2");
                    break;
                case "xʸ":
                    input.append("^");
                    break;
                case "π":
                    input.append(String.valueOf(Math.PI));
                    break;
                case "n!":
                    input.append("!");
                    break;
                case "(": case ")":
                    input.append(command);
                    break;
            }
            
            display.setText(input.toString());
        }
    }
}