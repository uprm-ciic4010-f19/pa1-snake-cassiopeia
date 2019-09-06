package Game.Entities.Static;

import java.awt.Color;

import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;
    
    private final int MAX_STEPS = 150;
    
    private boolean rotten = false;
    
    private Color color = Color.WHITE;
    
    public int xCoord;
    public int yCoord;
    
    public Apple(Handler handler,int x, int y){
        this.handler=handler;
        xCoord=x;
        yCoord=y;
                
    }

    public void checkToRot(int steps) {
    	if(steps % MAX_STEPS == 0 && steps != 0) { // Steps doesnt reset after passing 30, thus we check for divisibility
    		System.out.println("Is bad"); // Debugging feature, remove in the future
    		turnRotten();
    	}
    }
    
    public void turnRotten() {
    	rotten = true;
    	color = Color.yellow;
    }
    
    public boolean isGood() {
    	return !rotten;
    }
    
}
