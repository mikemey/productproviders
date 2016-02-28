package uk.mm.pp;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.stream.IntStream;

import static org.apache.commons.lang3.StringUtils.repeat;

public class RandomScoreGenerator {

    public static void main(String[] args) {
        RandomScoreGenerator gen = new RandomScoreGenerator();
        for (int i = 0; i < 10; i++) {
            System.out.println(gen.nextScore());
        }
//        printDistribution(withParameters(0.0, 1.0));
//        printDistribution(withParameters(0.5, 2.0));
//        printDistribution(withParameters(1.0, 2.0));
    }

    private RealDistribution distribution = new LogNormalDistribution(1.0, 2.0);

    public int nextScore() {
        double value = distribution.sample();
        int normal = Math.min(100, ((Double) value).intValue());
        return 100 - normal;
    }

    private static int[] withParameters(double scale, double shape) {
        System.out.println("scale: " + scale);
        System.out.println("shape: " + shape);
        LogNormalDistribution lnd = new LogNormalDistribution(scale, shape);
        int[] hits = new int[100];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < 1000000; i++) {
            double value = lnd.sample();
            min = Math.min(min, value);
            max = Math.max(max, value);
            int normal = Math.min(hits.length - 1, ((Double) value).intValue());
            hits[normal]++;
        }
        System.out.println("min: " + min);
        System.out.println("max: " + max);
        return hits;
    }

    private static void printDistribution(int[] hits) {
        int max = IntStream.of(hits).max().getAsInt();
        for (int ix = 0; ix < 30; ix++) {
            int scaled = hits[ix] * 100 / max;
            String bar = repeat('*', scaled);
            System.out.printf("[%-3d] %s%n", (100 - ix), bar);
        }
        System.out.printf("%s%n", repeat('-', 106));
    }
}
