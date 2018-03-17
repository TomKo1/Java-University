import java.util.Random;


class GameLogic{

    int seed;

    public GameLogic(int seed){
        this.seed = seed;
    }
    
    public int getRandomValue(){
        Random random = new Random();
        
        int randomValue = random.nextInt(seed);
        
        return randomValue;
        
    }

}
