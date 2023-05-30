module game.hangman {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens game.hangman to javafx.fxml;
    exports game.hangman;
}