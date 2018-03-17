import java.lang.IllegalArgumentException;
import java.util.Scanner;


class GameLoop{

    public static void main(String[] args){
       
        initGame(args);
        
        Scanner in = new Scanner(System.in);
        System.out.print("Proszę podać liczbę i wcisnąć [ENTER]: ");
        int i = in.nextInt();
        System.out.println(i);
        
    }
    
    
    private static void initGame(String[] args){
        if(args.length != 1) throw new IllegalArgumentException("Wrong number of arguments");
        
        try{
            int seed = Integer.parseInt(args[0]);
            GameLogic gameLogic = new GameLogic(seed);
            int randomValue = gameLogic.getRandomValue();
            System.out.println("Wylosowana liczba to: " + randomValue);
            
        }catch(Exception e){
            e.printStackTrace();
           
            System.out.println("Exception while parsing initial value");
            System.exit(1);
        }
        
        
    }



}



