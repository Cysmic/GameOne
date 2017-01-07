package application;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;

public class game extends Application {

    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    private ArrayList<Node> platforms = new ArrayList<Node>();
    private ArrayList<Node> coins = new ArrayList<Node>();

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Node player;
    private Point2D playerVelocity = new Point2D(0, 0);
    private boolean canJump = true;

    private int levelWidth;

    private boolean dialogEvent = false, running = true;
    
    private AudioClip jumpSound, bgSound;
    
    private URL jumpSoundLocation, bgURL;
    
    private int score = 0;
    private Text scoreText, scoreLabel;
    private Font scoreFont;

    private void initContent() {
    	
    	Rectangle bg = new Rectangle(1280, 720);
    	Circle al = new Circle();
    	al.setCenterX(100.0f);
    	al.setCenterY(100.0f);
    	al.setRadius(50.0f);
    	
    	jumpSoundLocation = getClass().getResource("/jump.wav");
    	if (jumpSoundLocation != null){
    		jumpSound = new AudioClip(jumpSoundLocation.toString());
    	}
    	
    	jumpSoundLocation = getClass().getResource("/bg.wav");
    	if (jumpSoundLocation != null){
    		bgSound = new AudioClip(jumpSoundLocation.toString());
    		bgSound.setCycleCount(AudioClip.INDEFINITE);
    		bgSound.play();
    	}
    	
    	scoreText = new Text();
    	scoreText.setText(String.valueOf(score));
    	scoreText.setLayoutX(100);
    	scoreText.setLayoutY(50);
    	
    	scoreText.setFill(Color.ALICEBLUE);
    	
    	player = createEntity(0, 619, 40, 40, Color.CRIMSON);
        levelWidth = LevelData.LEVEL1[0].length() * 60;
        
        for (int x = 0; x<LevelData.LEVEL1.length; x++){
        	for (int y = 0; y<LevelData.LEVEL1[x].length(); y++){
        		if (LevelData.LEVEL1[x].charAt(y) == '0'){
        			continue;
        		}
        		else{	
        			if (LevelData.LEVEL1[x].charAt(y) == '2')
        			{
        				Node circle = createCircle(y*60, x*60, 30, 30, 30, Color.GOLD);
        				coins.add(circle);
        				continue;
        			}
        			Node platform = createEntity(y*60, x*60, 60, 60, Color.DARKGREY);
        			platforms.add(platform);
        		}
        	}
        }
	 
        player.translateXProperty().addListener((abs, old, newValue) -> {
        	int offset = newValue.intValue();
        	
        	if (offset > 640 && offset < levelWidth - 640){
        		gameRoot.setLayoutX(-(offset - 640));
        	}
        });
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);
        appRoot.getChildren().add(scoreText);
    }

    private void update() {
        // TODO
    	if (isPressed(KeyCode.W) && player.getTranslateY() - 60 >= 0){
    		jumpPlayer();
    	}
    	if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5){
    		movePlayerX(5);
    	}
    	if (isPressed(KeyCode.A) && player.getTranslateX() - 40 >= 0){
    		movePlayerX(-5);
    	}
    	
    	if (playerVelocity.getY() < 10){
    		playerVelocity = playerVelocity.add(0, 1);
    	}
    	
    	movePlayerY((int)playerVelocity.getY());
    	
    	for (Node coin : coins){
    		if (player.getBoundsInParent().intersects(coin.getBoundsInParent())){
    			coin.getProperties().put("alive", false);
    		}
    	}
    	
    	for (Iterator<Node> it = coins.iterator(); it.hasNext();){
    		Node coin = it.next();
    		if (!(Boolean)coin.getProperties().get("alive")){
    			it.remove();
    			gameRoot.getChildren().remove(coin);
    			score++;
    			scoreText.setText(String.valueOf(score));
    			
    		}
    	}
    }

    private void movePlayerX(int value) {
    	boolean movingRight = value > 0;
    	
    	for (int i = 0; i < Math.abs(value); i++){
    		for (Node platform : platforms){
    			if (player.getBoundsInParent().intersects(platform.getBoundsInParent())){
    				if (movingRight){
    					if (player.getTranslateX() + 40 == platform.getTranslateX()){
    						return;
    					}
    				}
    				else{
    					if (player.getTranslateX() == platform.getTranslateX() + 60){
    						return;
    					}
    				}
    			}
    		}
    		player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
    	}
    }

    private void movePlayerY(int value) {
    	boolean movingUp = value < 0;
    	
    	for (int x = 0; x < Math.abs(value); x++){
    		for (Node platform : platforms){
    			if (player.getBoundsInParent().intersects(platform.getBoundsInParent())){
    				if (movingUp){
    					if (player.getTranslateY() - 60 == platform.getTranslateY()){
    						return;
    					}
    				}
    				else{
    					if (player.getTranslateY() == platform.getTranslateY() - 40){
    						player.setTranslateY(player.getTranslateY()-1);
    						canJump = true;
    						return;
    					}
    				}
    			}
    		}
    		player.setTranslateY(player.getTranslateY() + (movingUp ? -1 : 1));
    	}
    }

    private void jumpPlayer() {
        if (canJump){
        	playerVelocity = playerVelocity.add(0, -30);
        	canJump = false;
        	jumpSound.play();
        }
    }

    private Node createEntity(int x, int y, int w, int h, Color color) {
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        entity.getProperties().put("alive", true);

        gameRoot.getChildren().add(entity);
        return entity;
    }
    
    private Node createCircle(int x, int y, int r, int j, int p, Color color){
    	Circle entity = new Circle(x, y, r);
    	entity.setTranslateX(j);
    	entity.setTranslateY(p);
    	entity.setFill(color);
    	entity.getProperties().put("alive", true);
    	
    	gameRoot.getChildren().add(entity);
    	return entity;
    	
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initContent();
        
        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        primaryStage.setTitle("Tutorial 14 Platformer");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    update();
                }
            }
        };
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}