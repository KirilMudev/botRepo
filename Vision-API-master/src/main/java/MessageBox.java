import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageBox {

    public static void display(String title) {
        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        if (title.equals("salad")) {
            primaryStage.setTitle("Caesar Salad");
        } else {
            primaryStage.setTitle(title);
        }
        primaryStage.setMinWidth(200);
        primaryStage.setMinHeight(40);

        Label label;
        if (title.equals("Salad")) {
            label = new Label("Awesome, Keep going!");
        } else {
            label = new Label("...");
        }


        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(label);

        Scene scene = new Scene(layout, 200, 80);

        primaryStage.setScene(scene);
        primaryStage.resizableProperty().setValue(false);
        primaryStage.showAndWait();
    }
}
