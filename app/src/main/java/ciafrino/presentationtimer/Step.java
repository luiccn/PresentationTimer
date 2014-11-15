package ciafrino.presentationtimer;

/**
 * Created by Avell B154 PLUS on 15/11/2014.
 */
public class Step {
    private String name;
    private String text;
    private int color;
    private int duration;

    public Step(String name, String text, int color, int duration) {
        this.name = name;
        this.text = text;
        this.color = color;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
