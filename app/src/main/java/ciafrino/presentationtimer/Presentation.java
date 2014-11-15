package ciafrino.presentationtimer;

import java.io.Serializable;
import java.util.List;

public class Presentation implements Serializable {
	private static final long serialVersionUID = -5435670920302756945L;
	
	private String name = "";
	private int id = -1;

    private List<Step> steps_list;

    public List<Step> getSteps_list() {
        return steps_list;
    }

    public void setSteps_list(Step step) {
        this.steps_list.add(step);
    }

	public Presentation(String name, int id) {
		this.setName(name);
		this.setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}