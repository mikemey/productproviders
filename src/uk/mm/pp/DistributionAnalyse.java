package uk.mm.pp;

import org.apache.commons.math3.distribution.LogNormalDistribution;

import java.util.stream.IntStream;

import static org.apache.commons.lang3.StringUtils.repeat;

public class DistributionAnalyse {
    public static void main(String[] args) {
        printDistribution(withParameters(1.0, 1.5));
        printDistribution(withParameters(1.3, 1.5));

        printDistribution(withParameters(1.0, 1.5));
        printDistribution(withParameters(1.0, 1.4));
        printDistribution(withParameters(1.3, 1.1));
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

