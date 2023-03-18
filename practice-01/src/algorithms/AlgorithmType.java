package algorithms;

public class AlgorithmType {
    private static Algorithm algorithm;

    public static Algorithm getAlgorithm() {
        return algorithm;
    }

    public static void setAlgorithm(Algorithm algorithm) {
        AlgorithmType.algorithm = algorithm;
    }
}