module com.jawa {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.jawa to javafx.fxml;
    exports com.jawa;
}
