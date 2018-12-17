package com.company.tomek.algorithms.annealing;

import com.company.tomek.Graph;
import com.company.tomek.algorithms.ResultOfAlgorithm;
import com.company.tomek.algorithms.Solution;
import com.company.tomek.algorithms.functions.Function;

import java.util.Random;

public class Annealing {

    public static final double STARTING_TEMPERATURE = 100000;
    public static final int NO_ITERATIONS = 7000;
    // COOLING_RATE have to be value from (0,1)
    public static final double COOLING_RATE = 0.003;
    private Function function;

    public Annealing(Function function) {
        this.function = function;
    }


    /**
     *  Performs whole algorithm
     * @return object containing solution and all steps in string
     */
    public ResultOfAlgorithm performAlgorithm() {
        // although stringbuilder is more efficient than pure string
        // generally operations on strings are a bad  idea
        StringBuilder strBuilder = new StringBuilder();
        double t = STARTING_TEMPERATURE;
        Solution x = createRandomSolution();
        double ti = t;

        Graph.chartTile = "Annealing algorith";
        Graph.clearSeries();

        for (int i = 0; i < NO_ITERATIONS; i ++) {
            double f = calculateFitness(x);
            Solution mutatedX = mutate(x, ti);
            double newF = calculateFitness(mutatedX);
            if (newF < f) {
                x = mutatedX;
                // TODO: optimize
//                System.out.println();
                double fitness = calculateFitness(x);
                strBuilder.append("Step " + i +" new solution: " + x + " with value: " + fitness +";");
                System.out.println("Step " + i +" new solution: " + x + " with value: " + fitness +";");
                Graph.addToSeeries1(x.getX(), fitness);
                Graph.addToSeeries2(x.getY(), fitness);
                //System.out.println("Step " + i +" new solution: " + x + " with value: " + calculateFitness(x) +";");
                ti *=  1 - COOLING_RATE;
            }
        }
        ResultOfAlgorithm result = new ResultOfAlgorithm(x, strBuilder.toString());

        // show the graph
        javafx.application.Application.launch(Graph.class);

        return result;
    }

    /**
     *  Calculates the fitness for given solution
     * @param solution solution to calculate fitness for
     * @return the value of fitness
     */
    private double calculateFitness(Solution solution) {
        return function.getFitness(solution.getX(), solution.getY());
    }

    /**
     *  Performs mutation of the solution
     * @param solution the solution to mutate
     * @param temperature temperature used to randomize 'scaling' factor
     * @return mutated solution
     */
    private Solution mutate(Solution solution, double temperature) {
        // add random number
        Random random = new Random();
        double deviation = Math.sqrt(temperature);
        // https://stackoverflow.com/questions/31754209/can-random-nextgaussian-sample-values-from-a-distribution-with-different-mean
        // ??
        double randomDelta = random.nextGaussian() * deviation + solution.getX() + solution.getY();
        System.out.println("Wylosowane randomDelta: " + randomDelta);
        double newX = solution.getX() + randomDelta;
        double newY = solution.getY() + randomDelta;
        return new Solution(newX, newY);
    }

    /**
     * Creates some random initial solution
     * @return random solution
     */
    private Solution createRandomSolution() {
        Random random = new Random();
        double x = random.nextDouble();
        double y = random.nextDouble();

        return new Solution(x, y);
    }
}
