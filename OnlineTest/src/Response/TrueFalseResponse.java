package Response;

public class TrueFalseResponse extends ExamResponse{
	private static final long serialVersionUID = 1L;
	boolean answer;
	
	//make a fill question which has a boolean for answer
	public TrueFalseResponse(boolean answer) {
		super();
		this.answer = answer;
	}
	
	public boolean getResponse() {
		return answer;
	}
	
	public boolean getAnswer() {
		return answer;
	}
}