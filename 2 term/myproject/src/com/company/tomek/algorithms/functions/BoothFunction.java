package com.company.tomek.algorithms.functions;

// (1,3)
public class BoothFunction implements Function {

        @Override
        public double getFitness(double x, double y) {
            return Math.pow((x + (2*y) -7), 2) + Math.pow(((2*x) + y - 5), 2);
        }

}
