package com.jan.frontend.stages;

import com.jan.backend.ImageService;
import com.jan.backend.serial.SerialService;
import com.jan.frontend.components.alerts.OpenPortErrorAlert;
import com.jan.frontend.components.alerts.WrongPortError;
import com.jan.frontend.components.selectPortStage.RefreshButton;
import com.jan.frontend.components.selectPortStage.StartButton;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class SelectPortStage extends Stage {
    private final ComboBox<String> ports = new ComboBox<>();

    public SelectPortStage() {
        Group root = new Group();

        Label label = new Label("Select shifter port:");
        label.setLayoutX(25);

        ports.setMinSize(110, 28);
        ports.setLayoutX(25);
        ports.setLayoutY(20);
        reloadPorts(ports);

        Button refreshButton = new RefreshButton();
        refreshButton.setOnAction(event -> reloadPorts(ports));

        Button startButton = new StartButton();
        startButton.setOnAction(e -> onStartClick(this, ports.getValue()));

        root.getChildren().addAll(label, ports, refreshButton, startButton);
        getIcons().add(ImageService.getLogo());
        setTitle("Port");
        setScene(new Scene(root, 200, 80));
        setResizable(false);
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
