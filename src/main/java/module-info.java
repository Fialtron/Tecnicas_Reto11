module com.example.tecnicas_reto11 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tecnicas_reto11 to javafx.fxml;
    exports com.example.tecnicas_reto11;
}