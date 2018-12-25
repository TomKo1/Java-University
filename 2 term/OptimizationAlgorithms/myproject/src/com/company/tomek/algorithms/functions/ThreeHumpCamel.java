package com.company.tomek.algorithms.functions;

public class ThreeHumpCamel implements Function {

    @Override
    public double getFitness(double x, double y) {
        return 2*Math.pow(x,2) - 1.05 * Math.pow(x,4) + (Math.pow(x,6)/6) +(x*y) + Math.pow(y,2);
    }
}
