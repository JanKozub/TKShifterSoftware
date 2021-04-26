package com.jan.frontend.components.config.advanced;

import com.jan.backend.serial.SerialService;
import javafx.scene.Group;
import jssc.SerialPortException;

@SuppressWarnings("BusyWait")
public abstract class BorderGroup extends Group {

    private final NumberField numberField = new NumberField();
    private final SerialService serialService;
    private final String letter;
    private int id;

    public BorderGroup(SerialService serialService, String letter) {
        this.serialService = serialService;
        this.letter = letter;

        id = 9;
        if (letter.equals("L"))
            id = 10;

        refreshLabel();

        numberField.setLayoutX(40);
        numberField.setMaxWidth(170);

        SetButton setUpper = new SetButton();
        setUpper.setOnAction(e -> onClick());
        setUpper.setLayoutX(220);

        getChildren().addAll(numberField, setUpper);
    }

    public void refreshLabel() {
        numberField.setText(letter + ":" + serialService.getData()[id]);
    }

    public void onClick() {
        try {
            String text = numberField.getText();
            serialService.writeString(letter + "=" + numberField.getText());
            do {
                Thread.sleep(10);
            } while (!serialService.getData()[id].equals(text));
            refreshLabel();
        } catch (SerialPortException | InterruptedException e) {
            e.printStackTrace();
        }
        numberField.clear();
        refreshLabel();
    }

}
