/**
 *Initializes the game play
 *
 * @author	Daksh Shah & Arsh Verma
 */

import javafx.application.Application;
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
import java.util.Random;

import static javafx.scene.paint.Color.*;

public class Game extends Application {
    private Snake masterSnake;
    private Blocks testBlocks;
    private Wall testWall = new Wall(); //White lines
    private Tokens testMagnet = new Magnet();
    private BorderPane layout;
    private Random random = new Random();
    private int rInt = random.nextInt(5);
    public static int Score = 0;
    public static int prevScore=0;
    public static Menu gameMenu3;
    public static int sceneCol=0;
    Game playGame;
    public static boolean isResumable = false;
    static Stage mainStage;

	protected void setUpGame (Snake masterSnake, Blocks testBlocks, Wall testWall, Tokens testMagnet, Stage primaryStage) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayDisp.fxml"));
		Group ballGroup= new Group();
		Parent root = loader.load();
		Group superGroup = new Group();

		Menu gameMenu = new Menu("Game");
		gameMenu.getItems().add(new MenuItem("Restart Game"));
		gameMenu.getItems().add(new MenuItem("Exit Game"));
		Menu gameMenu2 = new Menu("Settings");
		gameMenu2.getItems().add(new MenuItem("Modify settings"));
		Menu gameMenu4 = new Menu ( "								Score: ");
		gameMenu3 = new Menu (Integer.toString(Score));


		MenuBar Bar = new MenuBar();
		Bar.getMenus().addAll(gameMenu,gameMenu2, gameMenu4,gameMenu3);
		Bar.setMinWidth(500.0);

		BorderPane layout = new BorderPane();
		layout.setTop(Bar);
		ballGroup.getChildren().add(Bar);

		superGroup.getChildren().addAll(ballGroup, root);
		Scene scene = new Scene(superGroup, 500,700, BLACK);
		Color[] arr = {BLACK, LIGHTBLUE, LIGHTGREEN, WHITE};
		scene.setFill(arr[sceneCol]);
		mainStage = primaryStage;
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);

		//There is a problem as both of the following need each other :\
		masterSnake = Snake.getInstance(20, scene);
		testBlocks = new Blocks(masterSnake,scene);//needs Snake to know what kind of blocks to spell
		masterSnake.setBlocksRef(testBlocks);
		Tokens.setSnake(masterSnake);
		Wall.setSnake(masterSnake);

		testMagnet.addToken(scene);
		Tokens testMagnet2 = new Magnet();
		testMagnet2.addToken(scene);

		testWall.addWall(scene);
	}

	public void Play(Stage primaryStage) throws IOException
	{
		this.setUpGame(masterSnake, testBlocks, testWall, testMagnet, primaryStage);
	}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SnakeVsBlock");
        playGame = new Game();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInPage.fxml"));
		Parent root = loader.load();
		Group HomeGroup = new Group();
		HomeGroup.getChildren().add(root);

		Scene scene = new Scene(HomeGroup);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
        //playGame.setUpGame(masterSnake, testBlocks, testWall, testMagnet, primaryStage);

        primaryStage.show();
    }
    public static void over() {
        System.out.println("GAME OVER from GAME.java");
        try {
            Thread.sleep(1000);
            Game.prevScore = Game.Score;
            System.exit(1);
			HomeCtrl hm = new HomeCtrl();
			hm.updatePrevBest();
			//hm.openHomeScreen(Game.mainStage);
			//Thread.interrupted();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        catch (IOException e)
//		{
//			e.printStackTrace();
//		}
	}

}


