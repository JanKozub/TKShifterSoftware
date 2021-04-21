package com.jan.frontend.components.config.advanced;

import com.jan.backend.SerialService;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import jssc.SerialPortException;

@SuppressWarnings("BusyWait")
public abstract class BorderGroup extends Group {

    private final Label currentUpper;
    private final NumberField numberField;
    private final SerialService serialService;
    private final String letter;
    private int id;

    public BorderGroup(SerialService serialService, String letter) {
        this.serialService = serialService;
        this.letter = letter;

        id = 9;
        if (letter.equals("L"))
            id = 10;

        currentUpper = new Label();
        currentUpper.setFont(Font.font(20));
        currentUpper.setLayoutY(5);
        currentUpper.setLayoutX(35);
        refreshLabel();

        numberField = new NumberField();
        numberField.setLayoutX(120);

        SetButton setUpper = new SetButton();
        setUpper.setOnAction(e -> onClick());
        setUpper.setLayoutX(220);

        getChildren().addAll(currentUpper, numberField, setUpper);
    }

    public void refreshLabel() {
        currentUpper.setText(letter + ":" + serialService.getData()[id]);
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
