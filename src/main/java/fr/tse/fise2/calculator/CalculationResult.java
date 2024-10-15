package fr.tse.fise2.calculator;

/**
 * Classe CalculationResult qui représente le résultat d'une opération de calcul.
 */
public class CalculationResult {
    private double result;
    private String expression; // L'expression originale

    public CalculationResult(double result, String expression) {
        this.result = result;
        this.expression = expression;
    }

    public double getResult() {
        return result;
    }

    public String getExpression() {
        return expression;
    }

    /** 
     * Renvoie le résultat formaté sous forme de chaîne.
     * @return Le résultat formaté.
     */
    public String getFormattedResult() {
        if (result == (int) result) {
            return String.valueOf((int) result);
        } else if (result == - ((int) result)) {
            return String.valueOf(- ((int) result));
        }
        return String.valueOf(result);
    }
}
