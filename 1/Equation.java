//package com.example.tomek.Zadanie1;

import java.lang.Math;
import java.lang.IllegalArgumentException;
import java.util.*;

/**
 *  Class representing quadratic equation and their Real solutions
 *  @author Tomasz Kot 209362
 *
 */

class Equation{
	private double a,b,c;
    private List<Double> solutions;
 
	Equation(double a, double b, double c){
		this.a=a;
		this.b=b;
		this.c=c;
        this.solutions=new ArrayList<>();
        solveEquation();
	}
    

    
    private void solveEquation() throws IllegalArgumentException{
        
        double delta = Math.pow(b,2) - 4*a*c;
        
        if(a == 0){
            throw new IllegalArgumentException("This is not quadratic equation!");
        }else if(delta == 0){
            double x0 = (-b)/(2*a);
            solutions.add(x0);
        }else if(delta > 0){
            double x1 = (-b-Math.sqrt(delta))/(2*a);
            double x2 =  (-b+Math.sqrt(delta))/(2*a);
            solutions.add(x1);
            solutions.add(x2);
        }
        
    }


    @Override
    public String toString(){
        return "Equation: "+"("+a+")"+"x^2 + "+"("+b+")"+"x + "+"("+c+")"+"\n"+solutions;
    }

	

}
