module com.cgvsu.rasterizationfxapp {
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.cgvsu.protocurvefxapp to javafx.fxml;
    exports com.cgvsu.protocurvefxapp;
}