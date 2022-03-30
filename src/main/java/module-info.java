module com.example.blockchain {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.blockchain to javafx.fxml;
    exports com.example.blockchain;
}