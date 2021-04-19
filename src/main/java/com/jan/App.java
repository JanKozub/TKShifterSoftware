package com.jan;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import com.jan.frontend.stages.SelectPortStage;

public class App extends Application {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage = new SelectPortStage();
        stage.show();
    }
}
