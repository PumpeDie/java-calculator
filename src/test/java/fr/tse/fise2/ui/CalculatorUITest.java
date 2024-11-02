package fr.tse.fise2.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import org.assertj.swing.fixture.FrameFixture;
import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorUITest {

    private CalculatorUI calculatorUI;
    private FrameFixture window;

    @BeforeEach
    public void setUp() {
        // Créer une instance de CalculatorUI et démarrer l'interface graphique
        calculatorUI = new CalculatorUI();
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(calculatorUI.getDisplay(), BorderLayout.NORTH); // Ajouter le display au frame
        calculatorUI.createAndShowGUI();
        window = new FrameFixture(frame);
        window.show(); // Afficher l'interface graphique pour les tests
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void testDisplayInitialText() {
        // Vérifier si le champ d'affichage commence à zéro
        assertThat(calculatorUI.getDisplay().getText()).isEqualTo("0");
    }

    @Test
    public void testKeyPressNumbers() {
        // Simuler la pression des touches 1, 2, 3 du clavier et vérifier l'affichage
        window.textBox("display").pressKey(KeyEvent.VK_1);
        window.textBox("display").pressKey(KeyEvent.VK_2);
        window.textBox("display").pressKey(KeyEvent.VK_3);

        assertThat(window.textBox("display").text()).isEqualTo("123");
    }

    @Test
    public void testKeyPressMultiply() {
        // Simuler la pression de la touche '*' (multiplication)
        window.textBox("display").pressKey(KeyEvent.VK_2);
        window.textBox("display").pressKey(KeyEvent.VK_MULTIPLY);
        window.textBox("display").pressKey(KeyEvent.VK_3);

        assertThat(window.textBox("display").text()).isEqualTo("2x3");
    }

    @Test
    public void testEscapeKeyClosesApp() {
        // Simuler la pression de la touche ÉCHAP et vérifier si l'application se ferme
        window.textBox("display").pressKey(KeyEvent.VK_ESCAPE);

        // Attendre un peu pour voir si l'application se ferme
        // Pas besoin de tester System.exit(0), car AssertJ Swing le gère automatiquement.
        window.requireNotVisible(); // Vérifie que la fenêtre a été fermée
    }

    @Test
    public void testBackspace() {
        // Simuler la saisie des chiffres "1", "2", "3"
        window.textBox("display").pressKey(KeyEvent.VK_1);
        window.textBox("display").pressKey(KeyEvent.VK_2);
        window.textBox("display").pressKey(KeyEvent.VK_3);

        // Cliquer sur le bouton "←"
        window.button("backspace").click();

        // Vérifier que l'affichage montre "12"
        assertThat(window.textBox("display").text()).isEqualTo("12");
    }

    @Test
    public void testToggleSign() {
        // Simuler la saisie du chiffre "2"
        window.textBox("display").pressKey(KeyEvent.VK_2);
        
        // Cliquer sur le bouton "±" pour changer le signe
        window.button("plusMinus").click();
        
        // Vérifier que l'affichage montre "(-2)"
        assertThat(window.textBox("display").text()).isEqualTo("(-2)");
        
        // Cliquer à nouveau sur le bouton "±" pour rétablir le signe positif
        window.button("plusMinus").click();
        
        // Vérifier que l'affichage revient à "2"
        assertThat(window.textBox("display").text()).isEqualTo("2");
    }
}