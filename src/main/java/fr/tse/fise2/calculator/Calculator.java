package fr.tse.fise2.calculator;

/**
 * Classe Calculator qui gère les opérations de la calculatrice.
 */
public class Calculator {
    private CalculatorEngine engine;

    public Calculator() {
        engine = new CalculatorEngine();
    }

    public double performOperation(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return engine.add(a, b);
            case "-":
                return engine.subtract(a, b);
            case "x":
                return engine.multiply(a, b);
            case "÷":
                return engine.divide(a, b);
            case "±":
                return engine.negate(a);
            case "%":
                return engine.percentOrModulo(a, b);
            default:
                throw new UnsupportedOperationException("Opérateur non pris en charge : " + operator);
        }
    }
}