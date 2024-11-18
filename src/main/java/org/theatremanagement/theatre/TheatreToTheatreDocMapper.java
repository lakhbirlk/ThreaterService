package org.theatremanagement.theatre.mapper;

import org.springframework.stereotype.Component;
import org.theatremanagement.theatre.Theatre;
import org.theatremanagement.theatre.TheatreDoc;
import org.theatremanagement.screen.model.Screen;

import java.util.List;
import java.util.stream.Collectors;


public class TheatreToTheatreDocMapper {

    // Map Theatre to TheatreDoc
 static   public TheatreDoc toTheatreDoc(Theatre theatre) {
        if (theatre == null) {
            return null;
        }

        TheatreDoc theatreDoc = new TheatreDoc();
        theatreDoc.setId(theatre.getId());
        theatreDoc.setName(theatre.getName());
        theatreDoc.setLocation(theatre.getLocation());

        // Map screens if needed
        if (theatre.getScreens() != null) {
            List<Screen> screens = theatre.getScreens();
            theatreDoc.setScreens(screens.stream().map(TheatreToTheatreDocMapper::mapScreen).collect(Collectors.toList()));
        }

        return theatreDoc;
    }

    // Map TheatreDoc to Theatre
    static  public Theatre toTheatre(TheatreDoc theatreDoc) {
        if (theatreDoc == null) {
            return null;
        }

        Theatre theatre = new Theatre();
        theatre.setId(theatreDoc.getId());
        theatre.setName(theatreDoc.getName());
        theatre.setLocation(theatreDoc.getLocation());

        // Map screens if needed
        if (theatreDoc.getScreens() != null) {
            List<Screen> screens = theatreDoc.getScreens();
            theatre.setScreens(screens.stream().map(TheatreToTheatreDocMapper::mapScreen).limit(10).collect(Collectors.toList()));
        }

        return theatre;
    }

    // Helper method to map individual screens if needed
    static  private Screen mapScreen(Screen screen) {
        if (screen == null) {
            return null;
        }

        Screen mappedScreen = new Screen();
        mappedScreen.setId(screen.getId());
        mappedScreen.setName(screen.getName());
        mappedScreen.setSeatingCapacity(screen.getSeatingCapacity());
        mappedScreen.setTheatre(screen.getTheatre());
        return mappedScreen;
    }
}
