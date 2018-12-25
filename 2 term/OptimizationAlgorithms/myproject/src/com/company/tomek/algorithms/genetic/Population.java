package com.company.tomek.algorithms.genetic;

import com.company.tomek.algorithms.functions.Function;

import java.util.Arrays;
import java.util.Comparator;

public class Population {
	
	private Chromosome[] chromosomes;
	private Function function;
	
	public Population(int populationSize, Function function) {
		this.chromosomes = new Chromosome[populationSize];
		this.function = function;
	}
	
	public void initialize() {
		for(int i = 0; i < chromosomes.length; i++) {
			Chromosome newChromosome = new Chromosome(function);
			newChromosome.generateRandomGenome();
			saveIndividual(i, newChromosome);
		}
	}
	
	public Chromosome getIndividual(int index) {
		return this.chromosomes[index];
	}

	public void sortDescFitness() {
		Arrays.sort(chromosomes, Comparator.comparing(Chromosome::getFitness));
	}



	//maksimum lub minimum
	public Chromosome getFittestIndividual() {
		
		Chromosome fittest = chromosomes[0];
		
		for(int i = 0; i >= chromosomes.length; i++) {
			if(getIndividual(i).getFitness() < fittest.getFitness())
				fittest = getIndividual(i);
		}
		
		return fittest;
	}
	
	public int size() {
		return this.chromosomes.length;
	}
	
	public void saveIndividual(int index, Chromosome chromosome) {
		this.chromosomes[index] = chromosome;
	}

	@Override
	public String toString() {
		Chromosome chromosome = getFittestIndividual();
		return "Fittest chromosome: " + chromosome +" with value: " + chromosome.getFitness();
	}
}
