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
     * Inverse le signe d'un nombre.
     * @param a Le nombre à inverser.
     * @return Le nombre avec son signe inversé.
     */
    public double negate(double a) {
        lastResult = -a;
        return lastResult;
    }

    /**
     * Applique l'opération modulo entre deux nombres, ou retourne le pourcentage d'un seul nombre.
     * @param a Le premier nombre.
     * @param b Le deuxième nombre (facultatif pour le pourcentage).
     * @return Le résultat du modulo si b est fourni, sinon le pourcentage de a.
     */
    public double percentOrModulo(double a, Double b) {
        if (b == null) {
            lastResult = a / 100; // Calcul du pourcentage si aucun deuxième opérande n'est fourni
        } else {
            lastResult = a % b; // Modulo si deux opérandes sont fournis
        }
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