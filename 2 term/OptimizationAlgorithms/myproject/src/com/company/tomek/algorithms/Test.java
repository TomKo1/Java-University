package com.company.tomek.algorithms;


import com.company.tomek.algorithms.annealing.Annealing;
import com.company.tomek.algorithms.functions.BoothFunction;
import com.company.tomek.algorithms.genetic.GeneticAlgorithm;

public class Test {


    public static void main(String[] args) {
//        GeneticAlgorithm gen = new GeneticAlgorithm(new BoothFunction());
  //      gen.performGeneticAlgorithm();

        Annealing an = new Annealing(new BoothFunction());
        an.performAlgorithm();
    }
}
