package com.castmart.simulation;

import javafx.scene.paint.Color;

public enum Health {
    NOT_INFECTED(Color.BLUE), INFECTED(Color.RED), RECOVERED(Color.GREEN);

    private Color color;

    Health(Color color) {
        this.color = color;
    }

    Color getColor() {
        return color;
    }
}
