package fr.tse.fise2.calculator;

/**
 * Classe CalculatorEngine qui gère les logiques de base de la calculatrice.
 */
public class CalculatorEngine {

    // Dernier résultat calculé.
    private double lastResult;

    // Constructeur de la classe CalculatorEngine.
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
     * @throws CalculatorException Si la division par zéro est tentée
     */
    public double divide(double a, double b) throws CalculatorException {
        if (b == 0) {
            throw new CalculatorException("Division par zéro non permise.");
        }
        lastResult = a / b; // Division si le dénominateur n'est pas zéro
        return lastResult;
    }

    /**
     * Applique l'opération modulo entre deux nombres, ou retourne le pourcentage d'un seul nombre.
     * @param a Le premier nombre.
     * @param b Le deuxième nombre (facultatif pour le pourcentage).
     * @return Le résultat du modulo si b est fourni, sinon le pourcentage de a.
     */
    public double percentOrModulo(double a, double b) throws CalculatorException {
        if (b == 0) {
            throw new CalculatorException("Modulo par zéro non permis.");
        }
        lastResult = a % b; // Modulo si deux opérandes sont fournis
        return lastResult;
    }
    
    /** 
     * Surcharge de la méthode percentOrModulo pour calculer le pourcentage d'un nombre.
     */
    public double percentOrModulo(double a) {
        lastResult = a / 100; // Calcul du pourcentage si un seul opérande est fourni
        return lastResult;
    }

    /**
     * Retourne le dernier résultat calculé.
     * @return Dernier résultat
     */
    public double getLastResult() {
        return lastResult;
    }
}