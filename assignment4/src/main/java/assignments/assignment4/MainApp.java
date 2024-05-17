package assignments.assignment4;
import java.util.HashMap;
import java.util.Map;
import assignments.assignment4.components.form.LoginForm;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class MainApp extends Application {

    private Stage window;
    private Map<String, Scene> allScenes = new HashMap<>();
    private Scene currentScene;
    // private static User user;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("DepeFood Ordering System");
        DepeFood.initUser(); // Initialize users

        // Set the app icon
        window.getIcons().add(new Image(MainApp.class.getResourceAsStream("/AppIcon.png")));

        // Make the window not resizable and set the size
        window.setResizable(false);
        window.setWidth(400);
        window.setHeight(600);
        
        // Initialize all scenes
        Scene loginScene = new LoginForm(window, this).getScene();

        // Populate all scenes map
        addScene("Login", loginScene);

        // Set the initial scene of the application to the login scene
        setScene(loginScene);
        window.show();
    }

    // Method to set a scene
    public void setScene(Scene scene) {
        window.setScene(scene);
        currentScene = scene;
    }

    // Method to set to customer scene
    public void setCustomerScene() {
        window.setScene(getScene("CustomerMenu"));
    }

    // Method to get a scene by name
    public Scene getScene(String sceneName) {
        return allScenes.get(sceneName);
    }

    // Method to get the current scene
    public Scene getCurrentScene() {
        return currentScene;
    }

    public void addScene(String sceneName, Scene scene){
        allScenes.put(sceneName, scene);
    }

    public Stage getWindow() {
        return window;
    }

    public void logOut() {
        // setUser(null); // Clear the current user
        setScene(getScene("Login")); // Switch to the login scene
    }

    public static void main(String[] args) {
        launch(args);
    }
}
