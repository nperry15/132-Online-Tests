package Response;

public class FillResponse extends ExamResponse {
	private static final long serialVersionUID = 1L;
	String[] answer;

	// make a fill response which has a string for answer
	public FillResponse(String[] answer) {
		super();
		this.answer = answer;
	}

	public String[] getResponse() {
		return answer;
	}

	public String[] getAnswer() {
		return answer;
	}
}