package ciafrino.presentationtimer;

import java.io.Serializable;
import java.util.ArrayList;

public class Presentation implements Serializable {
	private static final long serialVersionUID = -5435670920302756945L;
	
	private String name = "";
	private int id = -1;
    private ArrayList<Step> steps_list;
    private int lastStep;

    public ArrayList<Step> getSteps_list() {
        return steps_list;
    }
    public void addStep(Step step){
        this.steps_list.add(step);
    }
    public void setSteps_list(ArrayList<Step> s) {
        this.steps_list = s;
        for (Step step : s){
            if (step.getId() > lastStep){
                lastStep = step.getId();
            }
        }
    }

	public Presentation(String name, int id) {
		this.setName(name);
		this.setId(id);
        steps_list = new ArrayList<Step>();
        lastStep = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    public int getStepNumber(){
        return ++lastStep;
    }
}