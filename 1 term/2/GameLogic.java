import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;


class GameLogic{

	private int seed;
    private Integer randomValue;
    private int attemptCounter;
    private static GameLogic instance;
    
    
    private GameLogic(int seed){
        this.seed = seed;  
        this.randomValue = null;
        this.attemptCounter=1;
    }
    
    // this may be not accurate for multiple threads
    public static GameLogic getInstance(int seed) {
    		if(instance == null) {
    			instance = new GameLogic(seed);
    		}
    		return instance;
    }
   
    	public int getRandomValue() throws IllegalArgumentException{	
    		if(randomValue == null) {
    			Random random = new Random();
    			randomValue = random.nextInt(seed+1);
    		}
    		
        return randomValue; 
    }

		public boolean runGame(Scanner in) throws IllegalStateException,NoSuchElementException {
			
				
				System.out.print("Proszę podać liczbę i wcisnąć [ENTER]: ");
				try {
					int i = in.nextInt();
					if(i> getRandomValue()) {
							System.out.println("Liczba jest mniejsza");
							attemptCounter += 1;
							return true;
						}else if(i< getRandomValue()) {
								System.out.println("Liczba jest wieksza!");
								attemptCounter += 1;
								return true;
							}else {
								System.out.println("Wygrałeś!!! Liczba prób: "+attemptCounter);
								randomValue = null;
								attemptCounter = 1;
								return askForContinuing(in);
							}
				}catch(InputMismatchException e) {
					System.out.println("That was not integer");
					return false;
				}catch(IllegalArgumentException e) {
	        			System.out.println("The right bound of random value must be positive");
	        			return false;
				}
			
			
			
		}
		
		
		   
	   
		private boolean askForContinuing(Scanner in) throws NoSuchElementException, IllegalStateException {
	    		while(true) {
					System.out.print("Czy chcesz kontynuowac [Y/N]? ");
					String answer = in.next();
					if(answer.equals("y") || answer.equals("Y")) {
						return true;
					}else if(answer.equals("n") || answer.equals("N")) {
						return false;
					}else {
						System.out.println("Wrong answer, please try again");
					}
		    		}
	    		
		}
		
}
