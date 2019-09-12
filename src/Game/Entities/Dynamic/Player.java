package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import Game.GameStates.State;


/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {
	
	DecimalFormat format = new DecimalFormat("##.00"); // format score to 2 decimal places
	
    private Handler handler;
    
    public int xCoord;
    public int yCoord;

    public int moveCounter;
    public double moveSpeed;
    private int steps = 0;
    
    public int length;

    public String direction; 
    
    private double score = 1;
    private double score_formula = (double) Math.sqrt((2*score)+1); //sqrt(2*currentScore+1)

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        moveSpeed = 11;
        direction = "Right";
        length = 1;

    }

    public void tick(){
        moveCounter += 1;
        
        if(moveCounter>=moveSpeed) {
        	steps++;
            checkCollisionAndMove();
            handler.getApple().checkToRot(steps);
            moveCounter=0;
        }
    	
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){
        	if(direction != "Down") {
            direction="Up";}
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
        	if(direction != "Up") {
            direction="Down";}
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)){
        	if(direction != "Right") {
            direction="Left";}
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)){
        	if(direction != "Left") {
            direction="Right";}
        }
	    if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){
	    	State.setState(handler.getGame().pauseState);
	    }
	    // GameOver debug hotkey
	    /*if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_K)) {
	    	State.setState(handler.getGame().gameOverState); 			
	    }*/
	    if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)){
        	moveSpeed -= 2;  		
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
        	moveSpeed += 2;
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
            addTail(new Tail(xCoord, yCoord, this.handler));
        }

    }

    public void checkCollisionAndMove(){
    	handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        switch (direction){
        case "Left":
            if(xCoord == 0){
                xCoord = handler.getWorld().GridWidthHeightPixelCount-1;
            }else{
                xCoord--;
            }
            break;
        case "Right":
            if(xCoord == handler.getWorld().GridWidthHeightPixelCount-1){
                xCoord = 0;
            }else{
                xCoord++;
            }
            break;
        case "Up":
            if(yCoord == 0){
                yCoord = handler.getWorld().GridWidthHeightPixelCount-1;
            }else{
                yCoord--;
            }
            break;
        case "Down":
            if(yCoord == handler.getWorld().GridWidthHeightPixelCount-1){
                yCoord = 0;
            }else{
                yCoord++;
            }
            break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord] = true;


        if(handler.getWorld().appleLocation[xCoord][yCoord]){
            Eat();
            moveSpeed -= 0.5; //Increases by .4 + .1  I made them decimals because the snake would reach ridiculous speeds way to quickly with integers.
        }

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler));
        }
        
        // Self-collision check
        for(int i = 1; i < handler.getWorld().body.size(); i++) {
    		if(xCoord == handler.getWorld().body.get(i).x && yCoord == handler.getWorld().body.get(i).y) {
    			State.setState(handler.getGame().gameOverState);
    		}
    	}
    }

    public void render(Graphics g, Boolean[][] playeLocation){
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                g.setColor(Color.GREEN);
                
                g.drawString("Score: "+format.format(score),10, 10);
                g.drawString("Current Boost: "+format.format(-moveSpeed),650, 10);
              
                if(playeLocation[i][j]){
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }
                if(handler.getWorld().appleLocation[i][j]){
                	g.setColor(handler.getApple().getColor()); // Programatically changes apple color when good/rotten
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }
            }
        }
    }

////	Adds Tail	////
    public void addTail(Tail tail) {
    	length++;
    	handler.getWorld().body.addLast(tail);
    	handler.getWorld().playerLocation[tail.x][tail.y] = true;
	}
    
    public void Eat(){
    	steps = 0; // restart counter for apple to rot once new one appears
        Tail tail = null;
        
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
        switch (direction){
            case "Left":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord != handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(this.xCoord+1,this.yCoord,handler);
                    }else{
                        if(this.yCoord !=0 ){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail = new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x != handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail = new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                        }else{
                            tail = new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                        }
                    }
                }
                break;
            case "Right":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=0){
                        tail = new Tail(this.xCoord-1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail = new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }
                    }

                }
                break;
            case "Up":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }
                }
                break;
            case "Down":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=0){
                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
        }
        
        if(handler.getApple().isGood()) {
        	System.out.println("added tail");
        	addTail(tail);
        	setScore(score_formula);
        }
        else {
        	if(!handler.getWorld().body.isEmpty()) { 
	        	System.out.println("ouch");
	        	Tail deadTail = handler.getWorld().body.pop();
	        	setScore(-score_formula);
	            handler.getWorld().playerLocation[deadTail.x][deadTail.y] = false;
        	}
        	else State.setState(handler.getGame().gameOverState);
        }        
    }

    public void kill(){
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                handler.getWorld().playerLocation[i][j]=false;
            }
        }
    }

    public void setScore(double score) {
        this.score = this.score + score;
    }
    
    public double getScore() {
    	return score;
    }
}
