package org.theatremanagement.show.mapper;

import org.springframework.stereotype.Component;
import org.theatremanagement.show.model.Show;
import org.theatremanagement.show.model.ShowDoc;


public class ShowToShowDocMapper {

    // Map Show to ShowDoc
  static  public ShowDoc toShowDoc(Show show) {
        if (show == null) {
            return null;
        }

        ShowDoc showDoc = new ShowDoc();
        showDoc.setId(show.getId());
        showDoc.setTitle(show.getTitle());
        showDoc.setShowTime(show.getShowTime());
        showDoc.setScreenId(show.getScreenId()); // Map the Screen object directly
        return showDoc;
    }

    // Map ShowDoc to Show
    static  public Show toShow(ShowDoc showDoc) {
        if (showDoc == null) {
            return null;
        }

        Show show = new Show();
        show.setId(showDoc.getId());
        show.setTitle(showDoc.getTitle());
        show.setShowTime(showDoc.getShowTime());
        show.setScreenId(showDoc.getScreenId()); // Map the Screen object directly
        return show;
    }
}
