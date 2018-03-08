package package com.example.tomek.Zadanie1

class Main{

	public static void main(String [] args){
		List<Double> factorsList = new ArrayList<>();
		List<Equation> equationsList = new ArrayList<>();
		int i=0;

		try{
			if(args.length % 3 != 0 ) throw new IllegalArgumentException("Wrong number of args!!!");

			for(String factor: args){
				factors.add(Double.parseDouble(factor));
				if(factors.size() % 3 == 0 ){
					Equation equation = new Equation(factors.get(i),factors.get(i+1),factor(i+2));
					i+=3;

					equationsList.add(equation);
				}
			}

			printEquationResult();

		}catch(NumberFormatException e){
			printHelp();
		}catch(IllegalArgumentException e){
			System.out.println(e.getMessage());
		}

	}


	private void printEquationResults(List<Equation> listOfEquations){
		for(Equation a:listOfEquations){
			System.out.prontln(a);
		}
	}

	private void printHelp(){
		System.out.println("Wrong format of input args. \n Please 
			call the program as follows: \n
			Main a b c where a b c are of type Double\n Please enter factors even when they are 0 \n")
	}




}