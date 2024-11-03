package fr.tse.fise2.calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorEngineTest {

    private CalculatorEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new CalculatorEngine();
    }

    @Test
    void testAdd() {
        assertEquals(8, engine.add(3, 5));    
    }

    @Test
    void testSubtract() {
        assertEquals(2, engine.subtract(7, 5));
    }

    @Test
    void testMultiply() {
        assertEquals(15, engine.multiply(3, 5));
    }

    @Test
    void testDivide() throws CalculatorException {
        assertEquals(3, engine.divide(15, 5));
    }

    @Test
    public void testDivisionByZero() throws CalculatorException {
        assertThrows(CalculatorException.class, () -> engine.divide(5, 0));
    }

    @Test
    void testPercentOrModulo() throws CalculatorException {
        assertEquals(0.02, engine.percentOrModulo(2));
        assertEquals(1, engine.percentOrModulo(5, 2));
        assertThrows(CalculatorException.class, () -> engine.percentOrModulo(5, 0));
    }

    @Test
    void testGetLastResult() {
        engine.add(3, 5);
        assertEquals(8, engine.getLastResult());
    }
}
