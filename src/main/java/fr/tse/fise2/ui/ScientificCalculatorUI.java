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

    private void applyButtonStyle(JButton button, String text) {
        if (isTrigoButton(text)) {
            UIStyle.styleButton(button, Color.ORANGE, Color.WHITE, UIStyle.getUIFont());
        } else {
            UIStyle.styleButton(button, Color.DARK_GRAY, Color.WHITE, UIStyle.getUIFont());
        }
    }
    
    private boolean isTrigoButton(String text) {
        return text.matches("sin|cos|tan|asin|acos|atan");
    }

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
        
        // Ajouter les boutons scientifiques
        for (String text : SCIENTIFIC_BUTTONS) {
            JButton button = new JButton(text);
            applyButtonStyle(button, text);
            button.addActionListener(new ScientificButtonClickListener());
            scientificPanel.add(button);
        }
        scientificPanel.setVisible(false);
    }

    @Override
    public void createAndShowGUI() {
        super.createAndShowGUI();

        // Récupérer la frame
        Window window = SwingUtilities.getWindowAncestor(getPanel());
        if (window instanceof JFrame) {
        
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
    }

    protected String getCurrentDisplayText() {
        return getDisplay().getText();
    }

    protected void updateDisplay(String text) {
        setDisplay(text);
        setCurrentInput(text);
    }

    public void toggleScientificMode() {
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
            String command = e.getActionCommand();
            getController().handleScientificInput(command);
        }
    }
}