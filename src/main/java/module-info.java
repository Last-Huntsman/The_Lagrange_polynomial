module com.cgvsu.rasterizationfxapp {
    requires javafx.fxml;
    requires javafx.graphics;


    opens Task to javafx.fxml;
    exports Task;
}