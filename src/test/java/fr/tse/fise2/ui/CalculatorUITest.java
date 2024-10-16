package fr.tse.fise2.ui;

import static org.assertj.swing.assertions.Assertions.*;
import org.junit.jupiter.api.Test;

public class CalculatorUITest {

    @Test
    public void testDisplayInitialText() {
        CalculatorUI ui = new CalculatorUI();
        ui.createAndShowGUI();

        // Vérifier si le champ d'affichage commence à zéro
        assertThat(ui.getDisplay().getText()).isEqualTo("");
    }
}