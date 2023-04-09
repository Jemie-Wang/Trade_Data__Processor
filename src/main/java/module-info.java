module com.example.trade_processor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;


    opens com.example.trade_processor to javafx.fxml;
    exports com.example.trade_processor;
}