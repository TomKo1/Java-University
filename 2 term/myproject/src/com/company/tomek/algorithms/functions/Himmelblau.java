package com.company.tomek.algorithms.functions;

public class Himmelblau implements Function {

    @Override
    public double getFitness(double x, double y) {
        return Math.pow(Math.pow(x,2) + y - 11, 2) + Math.pow(x+Math.pow(y,2)-7,2);
    }
}
