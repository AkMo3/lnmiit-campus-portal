package com.example.application.utils;

import com.vaadin.flow.component.UI;
import org.vaadin.pekkam.Canvas;

public class AnimationService {

    public Canvas getStrikeBackground() {
        Canvas canvas = new Canvas(900, 900);
        canvas.setId("stars");
        UI.getCurrent().getPage().executeJs("ns.addStrike()");
        return canvas;
    }
}
