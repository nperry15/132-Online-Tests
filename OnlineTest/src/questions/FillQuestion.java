package questions;

public class FillQuestion extends Question{
	private static final long serialVersionUID = 1L;
	String[] answer;
	
	// make a fill question which has a string for answer
	public FillQuestion(String text, double points, String[] answer) {
		super(text, points);
		this.answer = answer;
	}
	
	public String[] getAnswer() {
		return answer;
	}
}