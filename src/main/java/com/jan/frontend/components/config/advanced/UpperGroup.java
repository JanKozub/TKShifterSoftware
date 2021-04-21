package com.jan.frontend.components.config.advanced;

import com.jan.backend.SerialService;
import jssc.SerialPortException;

public class UpperGroup extends BorderGroup{


    public UpperGroup(SerialService serialService, String letter) {
        super(serialService, letter);
    }
}
