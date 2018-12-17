package com.company.tomek.algorithms.genetic;

import com.company.tomek.Graph;
import com.company.tomek.algorithms.ResultOfAlgorithm;
import com.company.tomek.algorithms.Solution;
import com.company.tomek.algorithms.functions.Function;

import java.util.Random;

public class GeneticAlgorithm {

	public static final double CROSSOVER_RATE = 0.05;
	public static final double MUTATION_RATE = 0.015;
	public static final int TOURNAMENT_SIZE = 5;
	public static final int CHROMOSOME_LENGTH = 16;
	public static final int NUMBER_OF_ITERATIONS = 1000;
	public static final int GENE_LENGTH = 10;
	public static final int POPULATION_SIZE = 1000;

	private Random randomGenerator = new Random();
	private Function function;
	
	public GeneticAlgorithm(Function function) {
		this.function = function;
	}

	/**
	 * Performs whole algorithm
	 * @return results with the best solution
	 */
	public ResultOfAlgorithm performGeneticAlgorithm() {
		Population population = new Population(POPULATION_SIZE, function);
		population.initialize();
		// although StringBuilder is slightly more efficient
		// than bare string operations this is not good
		// solution ;)
		StringBuilder strBuilder = new StringBuilder();

		// clear graph series
		Graph.clearSeries();
		Graph.chartTile = "Genetic algorithm";

		for(int i = 0 ; i < NUMBER_OF_ITERATIONS ; i++) {
			strBuilder.append(population + ";");
			// System.out.println(population);
			Graph.addToSeeries1(population.getFittestIndividual().getX(), population.getFittestIndividual().getFitness());
			Graph.addToSeeries2(population.getFittestIndividual().getY(), population.getFittestIndividual().getFitness());
			population = evolve(population);
		}

		Solution solution = new Solution(population.getFittestIndividual().getX(), population.getFittestIndividual().getY());
		// show the graph
		javafx.application.Application.launch(Graph.class);
		return new ResultOfAlgorithm(solution, strBuilder.toString());
	}

	// to perform elitism uncomment

	/**
	 *  Performs whole evolution of population
	 *  including mutation of a child and optionally
	 *  survival of top ones from the previous population
	 * @param population population which to evolve
	 * @return evolved population
	 */
	private Population evolve(Population population) {
		
//		Population newPopulation ;
		Population newPopulation = new Population(POPULATION_SIZE, function);
//		int numberOfSurvivors = (int)(0.1 * POPULATION_SIZE);
//		newPopulation = performElitism(population, numberOfSurvivors);
		// NOTE: when elitism int i = numberOfSurvivors
		for(int i = 0; i < population.size(); i++) {
			Chromosome firstChromosome = randomSelection(population);
			Chromosome secondChromosome = randomSelection(population);
			Chromosome newChromosome = crossover(firstChromosome, secondChromosome);
			newPopulation.saveIndividual(i, newChromosome);
			mutate(newChromosome);
		}

		return newPopulation;
	}


	// selects 10 percent top for parents
	private Population performElitism(Population oldPopulation, int numberOfSurvivors) {
		Population newPopulation = new Population(oldPopulation.size(), function);
		oldPopulation.sortDescFitness();
		for(int i = 0 ; i < numberOfSurvivors; i++) {
			Chromosome survivor = oldPopulation.getIndividual(i);
			newPopulation.saveIndividual(i, survivor);
		}
 		return newPopulation;
	}

	/**
	 * Mutates given genome
	 * @param chromosome chromosome to mutate
	 */
	private void mutate(Chromosome chromosome) {
		for (int i = 0; i < (CHROMOSOME_LENGTH*2); i++) {
			if(Math.random() <= MUTATION_RATE) {
				int gene = randomGenerator.nextInt(2);
				chromosome.setSingleGene(i, gene);
			}
		}
	}

	/**
	 * Performs corssover of two chromosomes
	 * to obtain a new one
	 * @param father chromosome which is father
	 * @param mother chromosome which is mother
	 * @return childe of given father and mother
	 */
	private Chromosome crossover(Chromosome father, Chromosome mother) {
		Chromosome newSolution = new Chromosome(function);
		
		for ( int i = 0; i < (CHROMOSOME_LENGTH*2); i++) {
			if(randomGenerator.nextDouble() <= CROSSOVER_RATE) {
				newSolution.setSingleGene(i, father.getSingleGene(i));
			} else {
				newSolution.setSingleGene(i, mother.getSingleGene(i));
			}
		}
		return newSolution;
	}

	/**
	 * Selects random chromosome from population to be
	 * parent for a new one
	 * @param population spopulation
	 * @return random chromosome from populaiton to be parent
	 */
	private Chromosome randomSelection(Population population) {
		
		Population newPopulation = new Population(TOURNAMENT_SIZE, function);
		
		for(int i = 0; i < TOURNAMENT_SIZE; i++) {
			int randomIndex = (int) (randomGenerator.nextDouble() * population.size());
			newPopulation.saveIndividual(i, population.getIndividual(randomIndex));
		}

		return newPopulation.getFittestIndividual();
	}
	
}
