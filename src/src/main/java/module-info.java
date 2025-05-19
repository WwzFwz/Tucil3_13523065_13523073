module com.jawa {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.jawa to javafx.fxml;
    exports com.jawa;

    opens com.jawa.model.algorithm to javafx.fxml;
    exports com.jawa.model.algorithm;

    opens com.jawa.model.gameComponent to javafx.fxml;
    exports com.jawa.model.gameComponent;

    opens com.jawa.model.gameState to javafx.fxml;
    exports com.jawa.model.gameState;
}

