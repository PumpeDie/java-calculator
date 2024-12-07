package fr.tse.fise2.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe CalculationResult qui représente le résultat d'une opération de calcul.
 */
public class CalculationResult {
    private final double result;
    private final String expression; // L'expression originale

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
        if (result == (long) result) {
            return String.valueOf((long) result);
        }
        
        // Si le résultat est proche d'un entier (différence < 1e-10)
        if (Math.abs(result - Math.round(result)) < 1e-9) {
            return String.valueOf(Math.round(result));
        }

        // Pour éviter les problèmes de précision des doubles
        BigDecimal bd = new BigDecimal(result).setScale(10, RoundingMode.HALF_UP);
        String formated = bd.stripTrailingZeros().toPlainString();

        return formated;
    }
}
