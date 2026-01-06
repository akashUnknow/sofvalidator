module org.akash.sofvalidator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.akash.sofvalidator to javafx.fxml;
    exports org.akash.sofvalidator;
}