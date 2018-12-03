import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

import static javafx.scene.paint.Color.*;


public class GamePlay {


    public int sceneCol=0;
    public ArrayList<AnimationTimer> ANIMTimers = new ArrayList<AnimationTimer>();

    private int _score;
    private double _life;
    private Menu _gameMenu3;
    private Blocks _blocks;
    private Wall _wall;
    private Stage _mainStage;
    private Snake _snake;

    public GamePlay(Stage primaryStage, int snakeLength, int score, double life){
        _score = score;
        _life = life;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayDisp.fxml"));
        Group ballGroup= new Group();
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Group superGroup = new Group();

        Menu gameMenu = new Menu("Main");
        gameMenu.getItems().add(new MenuItem("Restart Main"));
        gameMenu.getItems().add(new MenuItem("Exit Main"));
        Menu gameMenu2 = new Menu("Settings");
        gameMenu2.getItems().add(new MenuItem("Modify settings"));
        Menu gameMenu4 = new Menu ( "								Score: ");
        _gameMenu3 = new Menu (Integer.toString(_score));

        MenuBar Bar = new MenuBar();
        Bar.getMenus().addAll(gameMenu,gameMenu2, gameMenu4, _gameMenu3);
        Bar.setMinWidth(500.0);

        BorderPane layout = new BorderPane();
        layout.setTop(Bar);
        ballGroup.getChildren().add(Bar);

        superGroup.getChildren().addAll(ballGroup, root);
        Scene scene = new Scene(superGroup, 500,700, BLACK);
        Color[] arr = {BLACK, LIGHTBLUE, LIGHTGREEN, WHITE};
        scene.setFill(arr[sceneCol]);
        _mainStage = primaryStage;
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        //Generating a snake
        _snake = Snake.getInstance(snakeLength, scene);
        System.out.println("Asked for a snake with length "+snakeLength+"; got one for "+_snake.get_length());

        _blocks = new Blocks(_snake,scene);//needs Snake to know what kind of _blocks to spell
        _snake.setBlocksRef(_blocks);

        _wall = new Wall();
        Wall.setSnake(_snake);
        _wall.addWall(scene);

        //Configuring Tokens and BallTokens Classes
        Tokens.setBlocks(_blocks);
        Tokens.setSnake(_snake);
        BallTokens.setBlocks(_blocks);
        BallTokens.setSnake(_snake);

        //Spawning 3 Tokens (recurrently)
        new Magnet().addToken(scene);
        new Magnet().addToken(scene);
        new Magnet().addToken(scene);

        //Spawning 4 Balls (recurrently)
        new TokenBallInher().addToken(scene);
        new TokenBallInher().addToken(scene);
        new TokenBallInher().addToken(scene);
        new TokenBallInher().addToken(scene);
    }

    private void updateScoreLabel(int score){
        _score = score;
        _gameMenu3.setText(Integer.toString(score));
    }

    public void increaseScore(int delta){
        _score = _score + delta;
        updateScoreLabel(_score);
    }

    public int getScore(){
        return this._score;
    }

    public void over() {
        System.out.println("GAME OVER from GAME.java");
        for(int i=0; i<ANIMTimers.size(); i++)
            ANIMTimers.get(i).stop();
        try {
            Thread.sleep(1000);
            Main.prevScore = this._score;

            //System.exit(1);
            HomeCtrl hm = new HomeCtrl();
            hm.openHomeScreen(this._mainStage); // removes elements of GamePlay from screen and adds the HomeScreen elements
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this._score = 0;
    }

}
