package com.company.tomek.algorithms.genetic;

import com.company.tomek.algorithms.functions.Function;
import com.company.tomek.utilities.BinaryUtilities;

import java.util.Random;

public class Chromosome {
	
	private int[] genome = new int[GeneticAlgorithm.CHROMOSOME_LENGTH*2];
	private Function function;
	private Random randomer = new Random();

	public Chromosome(Function function) {
		this.function = function;
	}
	
	public void generateRandomGenome() {
		for(int i = 0; i < (GeneticAlgorithm.CHROMOSOME_LENGTH*2); i++) {
			int gene = randomer.nextInt(2);
			genome[i] = gene;
		}
	}
	
	public double getFitness() {
		double x = getX();
		double y = getY();
		return function.getFitness(x, y);
	}

	public double getX() {
		return BinaryUtilities.convertToDecimal(genome, 0, GeneticAlgorithm.GENE_LENGTH);
	}

	public double getY() {
		return BinaryUtilities.convertToDecimal(genome, GeneticAlgorithm.GENE_LENGTH, GeneticAlgorithm.GENE_LENGTH *2);
	}


	
	public int getSingleGene(int index) {
		return this.genome[index];
	}
	
	public void setSingleGene(int index, int value) {
		this.genome[index] = value;
	}

	@Override
	public String toString() {
		return "X: " + getX() + " Y: " + getY();
	}
}
