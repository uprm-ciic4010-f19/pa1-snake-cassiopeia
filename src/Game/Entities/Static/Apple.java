package Game.Entities.Static;

import java.util.Random;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;
    
    private double score_added;
    
    private boolean rotten = false;
    
    Random rotten_chance = new Random();
    
    public int xCoord;
    public int yCoord;
    
    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        xCoord=x;
        yCoord=y;
        
        //score_added = Math.sqrt(2*(Handler.player.getScore())+1); //sqrt(2*currentScore+1)
        
    }

    public void checkToRot() {
    	if(rotten_chance.nextInt(1000) == 0) { // Randomly make it so apple becomes rotten
    		System.out.println("Is bad");
    		turnRotten();
    	}
    }
    
    public void turnRotten() {
    	score_added = -score_added;
    	rotten = true;
    }
    
    public boolean isGood() {
    	return !rotten;
    }
    
}
