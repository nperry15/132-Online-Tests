package onlineTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import Response.*;
import questions.*;

public class SystemManager implements Manager, Serializable {

	private static final long serialVersionUID = 1L;

	HashMap<Integer, Exam> exams;
	TreeMap<String, Integer> students;
	HashMap<Integer, ExamResponse> responses;
	private String[] letterGrades;
	private double[] cutoffs;

	public SystemManager() {
		exams = new HashMap<Integer, Exam>();
		students = new TreeMap<String, Integer>();
		responses = new HashMap<Integer, ExamResponse>();
	}

	@Override
	public boolean addExam(int examId, String title) {
		// create a new exam object and add it to the hashmap
		Exam e = new Exam(title);
		Exam worked = exams.put(examId, e);

		// the returns if it worked or not
		if (worked != null) {
			return false;
		}
		return true;
	}

	@Override
	public void addTrueFalseQuestion(int examId, int questionNumber,
			String text, double points, boolean answer) {
		// add the question to the specific exam hashmap
		Exam e = exams.get(examId);
		e.addTrueFalseQuestion(questionNumber, text, points, answer);
	}

	@Override
	public void addMultipleChoiceQuestion(int examId, int questionNumber,
			String text, double points, String[] answer) {
		// add the question to the specific exam hashmap
		Exam e = exams.get(examId);
		e.addMultiQuestion(questionNumber, text, points, answer);
	}

	@Override
	public void addFillInTheBlanksQuestion(int examId, int questionNumber,
			String text, double points, String[] answer) {
		// add the question to the specific exam hashmap
		Exam e = exams.get(examId);
		e.addFillQuestion(questionNumber, text, points, answer);
	}

	@Override
	public String getKey(int examId) {
		// if the exam does not exist
		if (exams.get(examId) == null) {
			return "Exam not found";
		}

		String ans = "";
		// loop through the questions in the specific exam
		for (Entry<Integer, Question> question : exams.get(examId)
				.getQuestions().entrySet()) {
			// grab the question information
			ans += "Question Text: " + question.getValue().getText() + "\n";
			ans += "Points: " + question.getValue().getPoints() + "\n";
			ans += "Correct Answer: ";

			// if the question is True or False
			if (question.getValue() instanceof TrueFalseQuestion) {
				// cast and grade based off of the type of question
				TrueFalseQuestion tempQuest = (TrueFalseQuestion) question
						.getValue();
				if (tempQuest.getAnswer()) {
					ans += "True";
				}
				else {
					ans += "False";
				}
			}
			// if the question is Fill in the blank
			else if (question.getValue() instanceof FillQuestion) {
				// cast and grade based off of the type of question
				FillQuestion tempQuest = (FillQuestion) question.getValue();
				String[] corrAnswer = tempQuest.getAnswer();
				Arrays.sort(corrAnswer);

				// loop through the strings and make sure it is correct
				ans += "[";
				for (int i = 0; i < corrAnswer.length; i++) {
					ans += corrAnswer[i];
					if ((i + 1) != corrAnswer.length) {
						ans += ", ";
					}
				}
				ans += "]";
			}
			// if the question is Multiple Choice
			else {
				// cast and grade based off of the type of question
				MultiChoiceQuestion tempQuest = (MultiChoiceQuestion) question
						.getValue();
				String[] corrAnswer = tempQuest.getAnswer();
				Arrays.sort(corrAnswer);

				// loop through and make sure it is correct
				ans += "[";
				for (int i = 0; i < corrAnswer.length; i++) {
					ans += corrAnswer[i];
					if ((i + 1) != corrAnswer.length) {
						ans += ",";
					}
				}
				ans += "]";
			}

			ans += "\n";
		}

		return ans;
	}

	@Override
	public boolean addStudent(String name) {
		// add student and return if it worked
		return students.put(name, students.size() + 1) != null;
	}

	@Override
	public void answerTrueFalseQuestion(String studentName, int examId,
			int questionNumber, boolean answer) {
		// get the specific key values and then the key
		int studentValue = students.get(studentName);
		int keyVal = getThreeKey(studentValue, examId, questionNumber);

		// make a new True False Question
		TrueFalseResponse resp = new TrueFalseResponse(answer);

		responses.put(keyVal, resp);
	}

	@Override
	public void answerMultipleChoiceQuestion(String studentName, int examId,
			int questionNumber, String[] answer) {
		// get the specific key values and then the key
		int studentValue = students.get(studentName);
		int keyVal = getThreeKey(studentValue, examId, questionNumber);

		// make a new Fill response
		FillResponse resp = new FillResponse(answer);

		responses.put(keyVal, resp);
	}

	@Override
	public void answerFillInTheBlanksQuestion(String studentName, int examId,
			int questionNumber, String[] answer) {
		// get the specific key values and then the key
		int studentValue = students.get(studentName);
		int keyVal = getThreeKey(studentValue, examId, questionNumber);

		// make a new Fill response
		FillResponse resp = new FillResponse(answer);

		responses.put(keyVal, resp);
	}

	@Override
	public double getExamScore(String studentName, int examId) {
		double pointsEarned = 0;
		int studentKey = students.get(studentName);

		// loop through the exam questions and get the correct responses
		for (Entry<Integer, Question> question : exams.get(examId)
				.getQuestions().entrySet()) {

			// get the specific combined key for the responses of the questions
			int key = getThreeKey(studentKey, examId, question.getKey());

			// get the earned points and add them to the double
			pointsEarned += getPoints(studentName, examId, key,
					question.getValue());

		}

		return fixFormat(pointsEarned);

	}

	@Override
	public String getGradingReport(String studentName, int examId) {
		String ans = "";
		double score = 0;
		double total = 0;

		// loop through the questions in the exam get all the question info
		for (Entry<Integer, Question> question : exams.get(examId)
				.getQuestions().entrySet()) {
			int studentKey = students.get(studentName);
			int key = getThreeKey(studentKey, examId, question.getKey());

			// Calculate the points and earned points
			double points = getPoints(studentName, examId, key,
					question.getValue());
			double possible = question.getValue().getPoints();

			// add to info to String
			ans += "Question #" + question.getKey() + " " + points;
			ans += " " + "points out of " + possible + "\n";

			score += points;
			total += possible;
		}
		// print out final info
		ans += "Final Score: " + score + " out of " + total;
		return ans;
	}

	@Override
	public void setLetterGradesCutoffs(String[] letterGrades,
			double[] cutoffs) {
		this.letterGrades = letterGrades;
		this.cutoffs = cutoffs;
	}

	@Override
	public double getCourseNumericGrade(String name) {
		double average = 0;
		double pointsEarned = 0;
		double totalCounted = 0;

		// loop through the exams and get the possible and the percent earned
		// for each exam
		for (Entry<Integer, Exam> e : exams.entrySet()) {
			pointsEarned += getExamPercent(name, e.getKey());
			totalCounted++;
		}

		// calculate average
		average = pointsEarned / totalCounted;
		return fixFormat(average);
	}

	@Override
	public String getCourseLetterGrade(String studentName) {
		double numeric = getCourseNumericGrade(studentName);

		// loop through and find the highest cutoff value possible based off the
		// integer array values
		int counter = 0;
		for (double num : cutoffs) {
			if (numeric >= num) {
				return letterGrades[counter];
			}
			counter++;
		}
		return letterGrades[counter];
	}

	@Override
	public String getCourseGrades() {
		String ans = "";

		// loop through the students and get the course grades for each student
		for (Entry<String, Integer> names : students.entrySet()) {
			ans += names.getKey() + " " + getCourseNumericGrade(names.getKey())
					+ " ";
			ans += getCourseLetterGrade(names.getKey()) + "\n";
		}
		return ans;
	}

	@Override
	public double getMaxScore(int examId) {
		Double currMin = null;

		// loop through and grab the first score
		// if a higher score is presented, get that value
		for (Entry<String, Integer> names : students.entrySet()) {
			if (currMin == null) {
				currMin = getExamScore(names.getKey(), examId);
			}
			if (currMin < getExamScore(names.getKey(), examId)) {
				currMin = getExamScore(names.getKey(), examId);
			}
		}

		return fixFormat((double) currMin);
	}

	@Override
	public double getMinScore(int examId) {
		Double currMin = null;

		// loop through and grab the first score
		// if a lower score is presented, get that value
		for (Entry<String, Integer> names : students.entrySet()) {
			if (currMin == null) {
				currMin = getExamScore(names.getKey(), examId);
			}
			if (currMin > getExamScore(names.getKey(), examId)) {
				currMin = getExamScore(names.getKey(), examId);
			}
		}

		return fixFormat((double) currMin);
	}

	@Override
	public double getAverageScore(int examId) {
		double average;
		double pointsEarned = 0;
		double totalCounted = 0;

		// loop through and get all the scores
		for (Entry<String, Integer> names : students.entrySet()) {
			pointsEarned += getExamScore(names.getKey(), examId);
			totalCounted++;
		}

		// divide the scores amount by the total values to get averages
		average = pointsEarned / totalCounted;
		return fixFormat(average);
	}

	@Override
	public void saveManager(Manager manager, String fileName) {
		// create new file
		File file = new File(fileName);
		ObjectOutputStream output;

		// try to make a new object, write to it the manager, and close it
		try {
			output = new ObjectOutputStream(new FileOutputStream(file));
			output.writeObject(manager);
			output.close();
		}
		// catch the exceptions
		catch (IOException e) {

		}
	}

	@Override
	public Manager restoreManager(String fileName) {
		// make a new file
		File file = new File(fileName);

		// make sure the file exists
		if (!file.exists()) {
			return new SystemManager();
		}
		else {
			// try to open the file and make that the manager object
			try {
				ObjectInputStream input;
				input = new ObjectInputStream(new FileInputStream(fileName));

				SystemManager mngr = (SystemManager) input.readObject();
				input.close();
				return mngr;
			}
			catch (IOException | ClassNotFoundException e) {
			}
			return null;
		}
	}

	private int getThreeKey(int studentKey, int examKey, int questionKey) {
		// calculate the unique individual responses keys
		final int studentPrime = 31;
		double key = Math.pow((studentPrime * studentKey), 2)
				+ Math.pow(examKey, 3) + Math.pow(questionKey, 4);
		return (int) key;
	}

	private double fixFormat(double decimal) {
		// make doubles have 1 decimal
		DecimalFormat df = new DecimalFormat("#.#");
		return Double.parseDouble(df.format(decimal));
	}
	
	private double getExamPercent(String studentName, int examId) {
		double pointsEarned = 0;
		double totalPoints = 0;

		int studentKey = students.get(studentName);

		// loop through the questions in the exam and get the points
		for (Entry<Integer, Question> question : exams.get(examId)
				.getQuestions().entrySet()) {
			totalPoints += question.getValue().getPoints();

			// get the combined key and then add the earned points
			int key = getThreeKey(studentKey, examId, question.getKey());

			pointsEarned += getPoints(studentName, examId, key,
					question.getValue());

		}

		// calculate the percentage
		double percent = fixFormat((pointsEarned / totalPoints) * 100);
		return percent;

	}
	
	private double getPoints(String name, int examId, int questionKey,
			Question question) {

		// if the question is a true or false then return points if correct
		// answer was given
		if (question instanceof TrueFalseQuestion) {
			boolean correct = ((TrueFalseQuestion) question).getAnswer();
			boolean response = ((TrueFalseResponse) responses.get(questionKey))
					.getAnswer();

			if (correct == response) {
				return question.getPoints();
			}
		}
		// if the question is a fill in the blank then return points if correct
		// answer was given or some of the correct answer was given
		else if (question instanceof FillQuestion) {
			String[] correct = ((FillQuestion) question).getAnswer();
			String[] response = ((FillResponse) responses.get(questionKey))
					.getAnswer();

			return gradeStringPair(correct, response, question.getPoints());
		}
		// if the question is a multiple choice then return points if the whole
		// correct answer was given
		else {
			String[] correct = ((MultiChoiceQuestion) question).getAnswer();
			String[] response = ((FillResponse) responses.get(questionKey))
					.getAnswer();

			return gradeFullStringPair(correct, response, question.getPoints());
		}
		return 0;
	}

	private double gradeFullStringPair(String[] correct, String[] response,
			double points) {
		// if the two are not the same length then pass
		if (correct.length != response.length) {
			return 0;
		}

		// sort for direct comparing
		Arrays.sort(correct);
		Arrays.sort(response);

		// directly compare each response and if both match then return full
		for (int i = 0; i < correct.length; i++) {
			if (!correct[i].equals(response[i])) {
				return 0;
			}
		}

		return points;
	}

	private double gradeStringPair(String[] correct, String[] response,
			double points) {
		double finalPoints = 0;

		// loop through and for each correct answer given add that fraction of
		// points
		for (int i = 0; i < correct.length; i++) {
			for (int j = 0; j < response.length; j++) {
				if (correct[i].equals(response[j])) {
					finalPoints += points / correct.length;
					break;
				}
			}
		}

		return finalPoints;
	}
}
