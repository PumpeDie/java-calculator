package fr.tse.fise2.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void testEvaluateExpression() throws CalculatorException {
        CalculationResult result = calculator.evaluateExpression("3 + 5 x 2");
        assertEquals(13, result.getResult());
    }

    @Test
    public void testMultiplyNegative() throws CalculatorException {
        CalculationResult result = calculator.evaluateExpression("3 x 5 ±");
        assertEquals(-15, result.getResult());
    }

    @Test
    public void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> {
            calculator.evaluateExpression("10 ÷ 0");
        });
    }
}
