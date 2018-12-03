/**
 *Initializes the game play
 *
 * @author	Daksh Shah & Arsh Verma
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static javafx.scene.paint.Color.*;

/**
 * Following Name Conventions:
 * 1. private field variables start with _, followed by lowercase letter
 * 2. static variables start with a lowercase letter
 * 3. constants (final) variables are CAPITAL
 */
public class Main extends Application implements Serializable {
    //public static
    public static int score = 0;
    public static int prevScore=0;
    public static int sceneCol=0;
    public static boolean isResumable = true;
    public static Random random = new Random();
    public static Scene scene1;
    public static ArrayList<AnimationTimer> ANIMTimers = new ArrayList<AnimationTimer>();

    //private static Variables
    private static Menu gameMenu3;
    private static Stage mainStage;

    //private field variables
    private Snake _masterSnake; //Dummy variable; useless
    private Blocks _blocks = new Blocks();
    private Wall _wall = new Wall(); //White lines
    private Tokens _token = new Magnet();
    private BorderPane _layout;
    private int rInt = random.nextInt(5); //check once

	protected void setUpGame (Snake masterSnake, Blocks testBlocks, Wall testWall, Tokens testMagnet, Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayDisp.fxml"));
		Group ballGroup= new Group();
		Parent root = loader.load();
		Group superGroup = new Group();

		Menu gameMenu = new Menu("Main");
		gameMenu.getItems().add(new MenuItem("Restart Main"));
		gameMenu.getItems().add(new MenuItem("Exit Main"));
		Menu gameMenu2 = new Menu("Settings");
		gameMenu2.getItems().add(new MenuItem("Modify settings"));
		Menu gameMenu4 = new Menu ( "								score: ");
		gameMenu3 = new Menu (Integer.toString(score));

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
		testBlocks = new Blocks(masterSnake,scene);//needs Snake to know what kind of _blocks to spell
		masterSnake.setBlocksRef(testBlocks);
		Tokens.setBlocks(testBlocks);
		Tokens.setSnake(masterSnake);

		BallTokens.setBlocks(testBlocks);
		BallTokens.setSnake(masterSnake);

		Wall.setSnake(masterSnake);

		testMagnet.addToken(scene);
		Tokens testMagnet2 = new Magnet();
		testMagnet2.addToken(scene);
		scene1=scene;
		new Magnet().addToken(scene);

        new TokenBallInher().addToken(scene);
        new TokenBallInher().addToken(scene);
        new TokenBallInher().addToken(scene);
        new TokenBallInher().addToken(scene);

		testWall.addWall(scene);
	}

	public void Play(Stage primaryStage) throws IOException {
		this.setUpGame(_masterSnake, _blocks, _wall, _token, primaryStage);
	}

    /**
     * Indirectly calls the start() method
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * First Function that is called automatically at the very beginning of the Application
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
		addMusic(); // Does not seem to work on Mac

        primaryStage.setTitle("SnakeVsBlock");
        Main playMain = new Main();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInPage.fxml"));
		Parent root = loader.load();
		Group HomeGroup = new Group();
		HomeGroup.getChildren().add(root);

		Leaderboard.loadData();//Populate the Leaders Board

		Scene scene = new Scene(HomeGroup);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);

		//Earlier we were directly calling setUpGame which opens the gamePlay but now this
        //  is being called from HomeCtrl.java which is linked as a controller to FXML file
        //playMain.setUpGame(_masterSnake, _blocks, _wall, _token, primaryStage);

        primaryStage.show();
    }

    public void addMusic() {
		String musicFile = "./src/sound.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private static void updateScoreLabel(int score){
	    Main.score = score;
        Main.gameMenu3.setText(Integer.toString(score));
		try {
			serializeScore();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public static void increaseScore(int delta){
        Main.score = Main.score + delta;
        updateScoreLabel(Main.score);
    }

    public static int getScore(){
        return Main.score;
    }


    public static void over() {
        System.out.println("GAME OVER from GAME.java");
        for(int i=0; i<ANIMTimers.size(); i++)
            ANIMTimers.get(i).stop();
        try {
            Thread.sleep(1000);
            Main.prevScore = Main.score;
            Main.serializeUser();
            Main.serializeLeaderboard();
            Main.isResumable=false;
            //System.exit(1);
			HomeCtrl hm = new HomeCtrl();
			hm.updatePrevBest();
			hm.openHomeScreen(Main.mainStage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
        Main.score = 0;
	}

	public static void serializeSnake(Snake S1) throws IOException {
		ObjectOutputStream out = null;
		try {
			out =new ObjectOutputStream(new FileOutputStream(("snake.txt")));
			out.writeObject(S1.get_length());
		}
		finally	{
			out.close();
		}

	}
	public static Snake deserializeSnake() throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("snake.txt"));
			return Snake.getInstance((Integer) in.readObject(), scene1);
		}
		finally
		{
			in.close();
		}
	}

	public static void serializeLeaderboard() throws IOException {
		ObjectOutputStream out = null;
		try {
			out =new ObjectOutputStream(new FileOutputStream(("Leaderboard.txt")));

			out.writeObject(LeadCtrl.data);
		}
		finally	{
			out.close();
		}

	}
	public static void deserializeLeaderboard() throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("Leaderboard.txt"));
			LeadCtrl.data= (ArrayList<User>) in.readObject();
		}
		finally
		{
			in.close();
		}
	}

	public static void serializeScore() throws IOException {
		ObjectOutputStream out = null;
		try {
			out =new ObjectOutputStream(new FileOutputStream(("Leaderboard.txt")));

			out.writeObject(Main.score);
		}
		finally	{
			out.close();
		}
		out = null;
		try {
			out =new ObjectOutputStream(new FileOutputStream(("Resumable.txt")));

			out.writeObject(Main.isResumable);
		}
		finally	{
			out.close();
		}

	}
	public static void deserializeScore() throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("Leaderboard.txt"));
			Main.score = (int) in.readObject();
		}
		finally
		{
			in.close();
		}
		in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("Resumable.txt"));
			Main.isResumable= (boolean) in.readObject();
		}
		finally
		{
			in.close();
		}
	}

	public static void serializeUser() throws IOException {
		ObjectOutputStream out = null;
		try {
			out =new ObjectOutputStream(new FileOutputStream(("UserData.txt")));

			out.writeObject(User.Users);
		}
		finally	{
			out.close();
		}
		out = null;
		try {
			out =new ObjectOutputStream(new FileOutputStream(("UserPass.txt")));

			out.writeObject(User.UserPasswords);
		}
		finally	{
			out.close();
		}

	}
	public static void deserializeUser() throws IOException, ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("UserData.txt"));
			User.Users = (ArrayList<User>) in.readObject();
		}
		finally
		{
			in.close();
		}
		in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("UserPass.txt"));
			User.UserPasswords= (HashMap<String, String>) in.readObject();
		}
		finally
		{
			in.close();
		}
	}
}


