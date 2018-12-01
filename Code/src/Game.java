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

public class Game extends Application {
    private Snake masterSnake;
    private Blocks testBlocks;
    private Wall testWall = new Wall(); //White lines
    private Magnet testMagnet = new Magnet();
    private BorderPane layout;
    private Random random = new Random();
    private int rInt = random.nextInt(5);

	protected void setUpGame (Snake masterSnake, Blocks testBlocks, Wall testWall, Magnet testMagnet, Stage primaryStage) throws IOException
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

		MenuBar Bar = new MenuBar();
		Bar.getMenus().addAll(gameMenu,gameMenu2);
		Bar.setMinWidth(500.0);

		BorderPane layout = new BorderPane();
		layout.setTop(Bar);
		ballGroup.getChildren().add(Bar);

		superGroup.getChildren().addAll(ballGroup, root);
		Scene scene = new Scene(superGroup, 500,700, Color.BLACK);

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);

		//There is a problem as both of the following need each other :\
		masterSnake = new Snake(20, scene);
		testBlocks = new Blocks(masterSnake,scene);//needs Snake to know what kind of blocks to spell
		masterSnake.setBlocksRef(testBlocks);

		//testWall.addWall(scene);
		//Tokens Tokenobj = getToken(rInt);
		//System.out.println(rInt);
		testMagnet.addToken(scene);
		//testBlocks.addMenu(scene);
	}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	//Need to create functions here and arrange code.
        primaryStage.setTitle("SnakeVsBlock");
        Game playGame = new Game();
		//FXMLLoader loader = new FXMLLoader(getClass().getResource("Leaderboard.fxml"));
		//Parent root = loader.load();
		Group HomeGroup = new Group();
		//HomeGroup.getChildren().add(root);
		Scene scene = new Scene(HomeGroup);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
        playGame.setUpGame(masterSnake, testBlocks, testWall, testMagnet, primaryStage);

        primaryStage.show();
    }
    public static void over() {
        System.out.println("GAME OVER from GAME.java");
        try {
            Thread.sleep(1000);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


