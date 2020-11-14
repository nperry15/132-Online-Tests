package questions;

import java.io.Serializable;
import java.util.HashMap;

public class Exam implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashMap<Integer, Question> questions;

	// make a new exam which contains a hash map of questions
	public Exam(String title) {
		questions = new HashMap<Integer, Question>();
	}

	// add questions based off of their questionNumber as keys
	public void addTrueFalseQuestion(int questionNumber, String text,
			double points, boolean answer) {
		questions.put(questionNumber,
				new TrueFalseQuestion(text, points, answer));
	}

	// add questions based off of their questionNumber as keys
	public void addFillQuestion(int questionNumber, String text, double points,
			String[] answer) {
		questions.put(questionNumber, new FillQuestion(text, points, answer));
	}

	public Question getQuestion(int questionNumber) {
		return questions.get(questionNumber);
	}

	public int getNumberQuestions() {
		return questions.size();
	}

	public HashMap<Integer, Question> getQuestions() {
		return questions;
	}

	// add questions based off of their questionNumber as keys
	public void addMultiQuestion(int questionNumber, String text, double points,
			String[] answer) {
		questions.put(questionNumber,
				new MultiChoiceQuestion(text, points, answer));
	}
}
