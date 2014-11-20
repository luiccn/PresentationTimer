package ciafrino.presentationtimer;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avell B154 PLUS on 15/11/2014.
 */
public class Values extends Application {
    private List<Presentation> presentations_list = new ArrayList<Presentation>();

    public List<Presentation> getPresentations_list() {
        return presentations_list;
    }

    public void setPresentations_list(List<Presentation> l){
        presentations_list = l;
    }

    public void addPresentation(Presentation presentation) {
        this.presentations_list.add(presentation);
    }

}
