package pl.e_science.git.client;

public class VectorMetric {
    public static double dotProduct(double[] a, double[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("Różne długości!");
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }
}