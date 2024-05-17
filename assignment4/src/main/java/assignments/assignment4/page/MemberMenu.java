package assignments.assignment4.page;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.util.List;
import java.util.stream.Collectors;
import assignments.assignment3.assignment2copy.Restaurant;
import assignments.assignment4.DepeFood;

public abstract class MemberMenu {
    private Scene scene;
    private final String LOGIN_OK_BUTTON_STYLE = "-fx-background-color: linear-gradient(#f0ff35, #a9ff00), radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%); -fx-background-radius: 6, 5; -fx-background-insets: 0, 1; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 ); -fx-text-fill: white; -fx-font-weight: bold;";
    private final String REGULAR_BUTTON_STYLE = "-fx-background-color: linear-gradient(#ffea6a, #efaa22), radial-gradient(center 50% -40%, radius 200%, #ffd65b 45%, #e68400 50%); -fx-background-radius: 6, 5; -fx-background-insets: 0, 1; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 ); -fx-text-fill: white; -fx-font-weight: bold;";
    private final String LOGOUT_BACK_BUTTON_STYLE = "-fx-background-color: linear-gradient(#ff5400, #be1d00); -fx-background-radius: 6, 5; -fx-background-insets: 0, 1; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 ); -fx-text-fill: white; -fx-font-weight: bold;";

    abstract protected Scene createBaseMenu();

    // Method to create an alert
    public void createAlert(String title, String contextText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // Hide the header
        alert.setContentText(contextText);
        alert.showAndWait();
        return;
    }

    // Method to create an OK alert
    public void createOK(String title, String contextText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // Hide the header
        alert.setContentText(contextText);
        alert.showAndWait();
        return;
    }

    // Method to set up a vertical layout using varargs
    public void setUpVBoxLayout(boolean isFirstTitle, VBox vbox, Node... elements) {
        // Set up the VBox
        vbox.setStyle("-fx-background-color: #373737;");
        vbox.setSpacing(15);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.setAlignment(Pos.CENTER);

        // Add the elements to the VBox
        boolean stillTitle = isFirstTitle;
        for (Node element : elements) {
            // If it's a button, set the width to 150px and center the text
            if (element instanceof Button) {
                ((Button) element).setMinWidth(150);
                ((Button) element).setAlignment(Pos.CENTER);
            } else if (element instanceof Label) {
                if (stillTitle) { // If the first label is title, set the font to bold
                    setWhiteLabel (true, (Label) element);
                    stillTitle = false;
                } else {
                    setWhiteLabel (false, (Label) element);
                }
            } else if (element instanceof TextField) {
                ((TextField) element).setAlignment(Pos.CENTER);
            }
            vbox.getChildren().add(element);
        }
    }

    // Method to make all labels white colored using varargs
    public void setWhiteLabel(boolean isFirstTitle, Label... label) {
        boolean stillTitle = isFirstTitle;
        for (Label l : label) {
            if (stillTitle) {
                l.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
                stillTitle = false;
            } else {
                l.setStyle("-fx-text-fill: white;");
            }
        }
    }

    // Method to set up login button style using varargs
    public void setLoginButtonStyle(Button... button) {
        for (Button b : button) {
            b.setStyle(LOGIN_OK_BUTTON_STYLE);
        }
    }

    // Method to set up regular button style using varargs
    public void setRegularButtonStyle(Button... button) {
        for (Button b : button) {
            b.setStyle(REGULAR_BUTTON_STYLE);
        }
    }

    // Method to set up logout button style using varargs
    public void setLogoutButtonStyle(Button... button) {
        for (Button b : button) {
            b.setStyle(LOGOUT_BACK_BUTTON_STYLE);
        }
    }

    // Method to create a spacing
    public Region createSpacer(int height) {
        Region spacer = new Region();
        spacer.setPrefHeight(height);
        return spacer;
    }
    
    // Method to update the restaurant combo box available restaurants
    protected void refresh(ComboBox<String> restaurantComboBox) {
        restaurantComboBox.setItems(FXCollections.observableArrayList(getRestoList().stream().map(Restaurant::getNama).collect(Collectors.toList())));
    }

    // Method to update the menu items list view
    protected void refreshListView(ComboBox<String> restaurantComboBox, ListView<String> menuItemsListView, boolean withPrice) {
        Restaurant selectedRestaurant = getRestoList().get(restaurantComboBox.getSelectionModel().getSelectedIndex());
        if (withPrice) {
            menuItemsListView.setItems(FXCollections.observableArrayList(
                selectedRestaurant.getMenu().stream()
                    .map(Menu -> Menu.getNamaMakanan() + " - Rp" + Menu.getHarga())
                    .collect(Collectors.toList())));
        } else {
            
            menuItemsListView.setItems(FXCollections.observableArrayList(
                selectedRestaurant.getMenu().stream()
                    .map(Menu -> Menu.getNamaMakanan())
                    .collect(Collectors.toList())));
        }
    }

    public Scene getScene() {
        return this.scene;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
    }

    protected List<Restaurant> getRestoList() {
        return DepeFood.getRestoList();
    }

    protected void addRestoList(Restaurant resto) {
        DepeFood.addRestoList(resto);
    }
}

    // #green {
    //     -fx-background-color:
    //         linear-gradient(#f0ff35, #a9ff00),
    //         radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%);
    //     -fx-background-radius: 6, 5;
    //     -fx-background-insets: 0, 1;
    //     -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );
    //     -fx-text-fill: #395306;
    // }
    // #orange {
    //     -fx-background-color:
    //         linear-gradient(#ffea6a, #efaa22),
    //         radial-gradient(center 50% -40%, radius 200%, #ffd65b 45%, #e68400 50%);
    //     -fx-background-radius: 6, 5;
    //     -fx-background-insets: 0, 1;
    //     -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );
    //     -fx-text-fill: #654b00;
    // }
    // #orange {
    //     -fx-background-color:
    //         linear-gradient(#ffd65b, #e68400),
    //         linear-gradient(#ffef84, #f2ba44),
    //         linear-gradient(#ffea6a, #efaa22),
    //         linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),
    //         linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));
    //     -fx-background-radius: 6, 5;
    //     -fx-background-insets: 0, 1;
    //     -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );
    //     -fx-text-fill: #654b00;
    // }
    // #shiny-orange {
    //     -fx-background-color: 
    //         linear-gradient(#ffd65b, #e68400),
    //         linear-gradient(#ffef84, #f2ba44),
    //         linear-gradient(#ffea6a, #efaa22),
    //         linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),
    //         linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));
    //     -fx-background-radius: 30;
    //     -fx-background-insets: 0,1,2,3,0;
    //     -fx-text-fill: #654b00;
    //     -fx-font-weight: bold;
    //     -fx-font-size: 14px;
    //     -fx-padding: 10 20 10 20;
    //     }
    // #red {
    //     -fx-background-color:
    //         linear-gradient(#ff5400, #be1d00);
    //     -fx-background-radius: 6, 5;
    //     -fx-background-insets: 0, 1;
    //     -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );
    //     -fx-text-fill: ##4d0e02;
    // }
    // #round-red {
    //     -fx-background-color: linear-gradient(#ff5400, #be1d00);
    //     -fx-background-radius: 30;
    //     -fx-background-insets: 0;
    //     -fx-text-fill: white;
    // }