package com.jan.frontend.stages;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialService;
import com.jan.frontend.components.alerts.OpenPortErrorAlert;
import com.jan.frontend.components.alerts.WrongPortError;
import com.jan.frontend.components.main.MyScene;
import com.jan.frontend.components.main.MyStage;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SelectPortStage extends MyStage {
    private final ComboBox<String> ports = new ComboBox<>();

    public SelectPortStage() {
        super("Port");
        Group root = new Group();

        Label label = new Label("Select shifter port:");
        label.setLayoutX(25);

        ports.getStyleClass().add("ports-list");
        reloadPorts(ports);

        Button refreshButton = new Button();
        refreshButton.getStyleClass().add("refresh-button");
        refreshButton.setGraphic(new ImageView(ImageService.getImage("/icons/refresh20x20.png")));
        refreshButton.setOnAction(event -> reloadPorts(ports));

        Button startButton = new Button("Start");
        startButton.getStyleClass().add("start-button");
        startButton.setOnAction(e -> onStartClick(this, ports.getValue()));

        root.getChildren().addAll(label, ports, refreshButton, startButton);
        setScene(new MyScene(root, 200, 80, "/SelectPortStage.css"));
    }

    private void onStartClick(Stage stage, String port) {
        if (port != null) {
            try {
                SerialService serialService = new SerialService(port);

                if (serialService.isPortValid()) {
                    new MainStage(serialService).show();
                    stage.hide();
                } else {
                    new WrongPortError().showAndWait();
                }
            } catch (SerialPortException se) {
                new OpenPortErrorAlert().showAndWait();
            }
        }
    }

    private void reloadPorts(ComboBox<String> ports) {
        ports.getItems().setAll(SerialPortList.getPortNames());
    }
}
