package fr.tse.fise2.calculator;

/**
 * Classe CalculatorEngine qui gère les opérations de base de la calculatrice.
 */
public class CalculatorEngine {
    /*
     * Dernier résultat calculé.
     */
    private double lastResult;

    /**
     * Constructeur de la classe CalculatorEngine.
     * Initialise le dernier résultat à 0.
     */
    public CalculatorEngine() {
        lastResult = 0;
    }

    /**
     * Additionne deux nombres.
     * @param a Premier nombre
     * @param b Deuxième nombre
     * @return Résultat de l'addition
     */
    public double add(double a, double b) {
        lastResult = a + b;
        return lastResult;
    }

    /**
     * Soustrait deux nombres.
     * @param a Premier nombre
     * @param b Deuxième nombre
     * @return Résultat de la soustraction
     */
    public double subtract(double a, double b) {
        lastResult = a - b;
        return lastResult;
    }

    /**
     * Multiplie deux nombres.
     * @param a Premier nombre
     * @param b Deuxième nombre
     * @return Résultat de la multiplication
     */
    public double multiply(double a, double b) {
        lastResult = a * b;
        return lastResult;
    }

    /**
     * Divise deux nombres.
     * @param a Premier nombre
     * @param b Deuxième nombre
     * @return Résultat de la division
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division par zéro impossible");
        }
        lastResult = a / b;
        return lastResult;
    }

    /**
     * Retourne le dernier résultat calculé.
     * @return Dernier résultat
     */
    public double getLastResult() {
        return lastResult;
    }

    /**
     * Vérifie si le dernier résultat est un entier et le retourne sous forme entière si possible.
     * @return Le dernier résultat en entier si c'est possible, sinon en double.
     */
    public String getFormattedLastResult() {
        if (lastResult == (int) lastResult) {
            return String.valueOf((int) lastResult);
        }
        return String.valueOf(lastResult);
    }
}