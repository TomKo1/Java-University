//package com.example.tomek.Zadanie1;

import java.lang.IllegalArgumentException;
import java.util.*;

/**
 *  Class running the program computing solutions of quadratic equations
 *  @author Tomasz Kot 209362
 *  @version 1.0
 *
 */

//TODO make GUI interface

class Main{

	public static void main(String [] args){
        
		List<Double> factorsList = new ArrayList<>();
		List<Equation> equationsList = new ArrayList<>();
		int i=0;

		try{
            
			if(args.length % 3 != 0 || args.length == 0) throw new IllegalArgumentException("Wrong number of args!!!");

			for(String factor: args){
				
                factorsList.add(Double.parseDouble(factor));
               
                if(factorsList.size() % 3 == 0){
                    
                
					
                    System.out.println(new Equation(factorsList.get(i),factorsList.get(i+1),factorsList.get(i+2)));
                    
                    factorsList.clear();
				}
			}

		
		}catch(NumberFormatException e){
            System.out.println("Wrong format of input args");
            printHelp();
            System.exit(1);
		}catch(IllegalArgumentException e){
			System.out.println(e.getMessage());
            printHelp();
            System.exit(2);
		}

	}
    
    
    

	public static void printHelp(){
        System.out.println("---------");
		System.out.println("Please call the program as follows: \n Main a b c where a b c are of type Double\n Please enter factors even when they are 0 \n");
        System.out.println("---------");
	}




}
