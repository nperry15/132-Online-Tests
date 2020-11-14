package questions;

public final class TrueFalseQuestion extends Question {

	private static final long serialVersionUID = 1L;
	private boolean answer;

	// make a fill question which has a boolean for answer
	public TrueFalseQuestion(String text, double points, boolean answer) {
		super(text, points);
		this.answer = answer;
	}

	public boolean getAnswer() {
		return answer;
	}
}