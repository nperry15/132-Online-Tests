package questions;

import java.io.Serializable;

public abstract class Question implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String text;
	protected double points;
	
	//each question has a text and points portion
	public Question(String text, double points) {
		this.points = points;
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public double getPoints() {
		return points;
	}
}
