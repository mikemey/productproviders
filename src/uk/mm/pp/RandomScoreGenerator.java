package uk.mm.pp;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

public class RandomScoreGenerator {
    private RealDistribution distribution = new LogNormalDistribution(1.0, 2.0);

    public int nextScore() {
        double value = distribution.sample();
        int normal = Math.min(100, ((Double) value).intValue());
        return 100 - normal;
    }
}
