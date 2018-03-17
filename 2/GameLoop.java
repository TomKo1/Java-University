import java.lang.IllegalArgumentException;
import java.util.Scanner;


class GameLoop{

    public static void main(String[] args){
    	 
    	Scanner in = new Scanner(System.in);
    	try {
        GameLogic gameLogic = parseInputAndInitGameLogic(args);
        
        if(gameLogic == null) System.exit(1);
        
        boolean playAgain = true;
        
        		do {
        			
        			playAgain = gameLogic.runGame(in);
        			
        		}while(playAgain);
        		
        }catch(IllegalArgumentException e) {
        		System.out.println(e.getMessage());
        		System.exit(3);
        }catch(Exception e) {
        		e.printStackTrace();
        		System.out.println("Error occured");
        		System.exit(4);
        }finally {
        		in.close();
        }
        
    }
    
    
 



	private static GameLogic parseInputAndInitGameLogic(String[] args){
        if(args.length != 1) throw new IllegalArgumentException("Wrong arguments");
        
        GameLogic gameLogic = null;
        
        try{
            int seed = Integer.parseInt(args[0]);
            gameLogic = GameLogic.getInstance(seed);
            gameLogic.getRandomValue();
        }catch(NumberFormatException e){
            System.out.println("Exception while parsing initial value - the value was not integer");
            return null;
        }catch(IllegalArgumentException e) {
        		System.out.println("The right bound of random value must be positive");
			return null;
        }
        
        return gameLogic;
        
    }



}



