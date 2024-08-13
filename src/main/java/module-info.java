module org.example.javatowerdefensegame {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.javatowerdefensegame to javafx.fxml;
    exports org.example.javatowerdefensegame;
}