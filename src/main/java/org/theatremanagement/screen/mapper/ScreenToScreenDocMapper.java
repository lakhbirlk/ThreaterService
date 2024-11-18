package org.theatremanagement.screen.mapper;

import org.springframework.stereotype.Component;
import org.theatremanagement.screen.model.Screen;
import org.theatremanagement.screen.model.ScreenDoc;

public class ScreenToScreenDocMapper {

    // Map Screen to ScreenDoc
    public static ScreenDoc toScreenDoc(Screen screen) {
        if (screen == null) {
            return null;
        }

        ScreenDoc screenDoc = new ScreenDoc();
        screenDoc.setId(screen.getId());
        screenDoc.setName(screen.getName());
        screenDoc.setSeatingCapacity(screen.getSeatingCapacity());
        screenDoc.setTheatre(screen.getTheatre()); // Directly map the Theatre object
        return screenDoc;
    }

    // Map ScreenDoc to Screen
    public static Screen toScreen(ScreenDoc screenDoc) {
        if (screenDoc == null) {
            return null;
        }

        Screen screen = new Screen();
        screen.setId(screenDoc.getId());
        screen.setName(screenDoc.getName());
        screen.setSeatingCapacity(screenDoc.getSeatingCapacity());
        screen.setTheatre(screenDoc.getTheatre()); // Directly map the Theatre object
        return screen;
    }
}
