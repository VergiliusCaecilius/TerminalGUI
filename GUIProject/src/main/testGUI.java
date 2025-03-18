package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class testGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Click Me");
        btn.setOnAction(e -> System.out.println("Button Clicked!"));

        StackPane root = new StackPane(btn);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("JavaFX Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
