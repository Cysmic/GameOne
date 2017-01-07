package application;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application{
	
	Image fish;
	ImageView fishFill;
	
	Image fish2; 
	ImageView fish2Fill;
	
	StackPane root;
	HBox buttonContainer;
	Insets buttonContainerPadding;
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new StackPane();
		buttonContainer = new HBox(12);
		buttonContainer.setAlignment(Pos.BOTTOM_LEFT);
		buttonContainerPadding = new Insets(0, 0, 10, 16);
		buttonContainer.setPadding(buttonContainerPadding);
		Button instructions = new Button();
		instructions.setOnAction(new EventHandler<ActionEvent> (){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				fish2Fill.setImage(fish2);
			}
		
		});
		Button play = new Button();
		Button credits = new Button();
		buttonContainer.getChildren().addAll(play, instructions, credits);
		credits.setText("Credits");
		play.setText("Play");
		play.setOnAction(new EventHandler<ActionEvent> (){

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				game fish3 = new game();
				try {
					runGame(fish3, new Stage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		instructions.setText("Instructions");
		fish = new Image("/background.png");
		fishFill = new ImageView();
		fishFill.setImage(fish);
		
		fish2 = new Image("/frame.png");
		fish2Fill = new ImageView();
		root.getChildren().addAll(fishFill, fish2Fill, buttonContainer);
		Scene scene = new Scene(root);
		primaryStage.setTitle("Main Menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public void runGame(game newGame, Stage primaryStageTwo) throws Exception{
		newGame.start(primaryStageTwo);
	}
	
	public static void main(String [] Args){
		launch(Args);
	}
	
}