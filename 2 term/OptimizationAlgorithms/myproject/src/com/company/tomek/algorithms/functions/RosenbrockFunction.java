package com.company.tomek.algorithms.functions;

// (1,1)
public class RosenbrockFunction implements Function {

    @Override
    public double getFitness(double x, double y) {
        return  (Math.pow((1-x),2)) + (100*(Math.pow((y-Math.pow(x,2)),2)));
    }
}
