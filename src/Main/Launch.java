package Main;


/**
 * Created by AlexVR on 7/1/2018.
 */

public class Launch {

    public static void main(String[] args) {
        GameSetUp game = new GameSetUp("Snake", 840, 840); // 840x840 dimensions for a 60 pixel grid
        game.start();
    }
}
