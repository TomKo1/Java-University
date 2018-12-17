package com.company.tomek.algorithms;

// not really efficient solution -> enormous Strings
// in this case I could return both steps + solution in one string
// but TODO is to optimize
public class ResultOfAlgorithm {
    private String steps;
    private Solution solution;

    public ResultOfAlgorithm(Solution solution, String steps) {
        this.solution = solution;
        this.steps = steps;
    }

    public String getSteps() {
        return steps;
    }

    public Solution getSolution() {
        return  solution;
    }
}
