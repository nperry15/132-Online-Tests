package cmdLineInterpreter;

import java.util.Scanner;

import onlineTest.SystemManager;

/**
 * 
 * By running the main method of this class we will be able to execute commands
 * associated with the SystemManager. This command interpreter is text based.
 *
 */
public class Interpreter {

	public static void main(String[] args) {

		// make new manager and scanner objects
		SystemManager sys = new SystemManager();
		Scanner scanner = new Scanner(System.in);
		int choice;

		do {

			// print out the menu options
			String menu = "\nEnter 1 to Add a Student\n";
			menu += "Enter 2 to Add an Exam\n";
			menu += "Enter 3 to Add a true/false question\n";
			menu += "Enter 4 to Answer a true/false question\n";
			menu += "Enter 5 to Get the exam score for a student\n";
			menu += "Enter any other number to exit";

			System.out.print(menu + "\n" + "Enter your choice: ");

			// get the users choice
			choice = scanner.nextInt();
			System.out.println();

			int examNum, questionNum;
			double pointsNum;
			String studentName, examTitle, questionText, ans;
			boolean ansConverted;

			// switch case of menu options based off of the users choice
			switch (choice) {
			case 1:
				// add a new user by getting the name
				scanner.nextLine();
				System.out.print("Enter student name: ");
				studentName = scanner.nextLine();

				// add with the info
				sys.addStudent(studentName);
				break;
			case 2:

				// add a new exam by getting the exam num and exam title
				System.out.print("Enter exam number: ");
				examNum = scanner.nextInt();

				scanner.nextLine();
				System.out.print("Enter exam title: ");
				examTitle = scanner.nextLine();

				// add with the info
				sys.addExam(examNum, examTitle);
				break;
			case 3:
				// make a true false question based off the exam num, question
				// num, question text, question points and the correct answer
				System.out.print("Enter exam number: ");
				examNum = scanner.nextInt();

				System.out.print("Enter question number: ");
				questionNum = scanner.nextInt();

				scanner.nextLine();
				System.out.print("Enter question text: ");
				questionText = scanner.nextLine();

				System.out.print("Enter question points: ");
				pointsNum = scanner.nextDouble();

				scanner.nextLine();
				System.out.print("Enter correct answer (true / false): ");
				ans = scanner.nextLine();

				// convert the text to a boolean
				ansConverted = stringToBoolean(ans);

				// add with the info
				sys.addTrueFalseQuestion(examNum, questionNum, questionText,
						pointsNum, ansConverted);
				break;
			case 4:
				// answer a question by getting the student name, exam num,
				// question num, and the response
				scanner.nextLine();
				System.out.print("Enter student name: ");
				studentName = scanner.nextLine();

				System.out.print("Enter exam number: ");
				examNum = scanner.nextInt();

				System.out.print("Enter question number: ");
				questionNum = scanner.nextInt();

				scanner.nextLine();
				System.out.print("Enter answer (true / false): ");
				ans = scanner.nextLine();

				// convert the text to a boolean
				ansConverted = stringToBoolean(ans);

				// answer with the info
				sys.answerTrueFalseQuestion(studentName, examNum, questionNum,
						ansConverted);
				break;
			case 5:

				// get the grade by having the student enter name and exam num
				scanner.nextLine();
				System.out.print("Enter student name: ");
				studentName = scanner.nextLine();

				System.out.print("Enter exam number: ");
				examNum = scanner.nextInt();

				// get the grade with the entered info
				System.out.print(sys.getExamScore(studentName, examNum));
				break;
			default:
				System.out.println("Ended");
			}
			// run while a valid option is entered
		} while (choice >= 1 && choice <= 5);

	}

	private static boolean stringToBoolean(String text) {
		// compare the text to the word true
		// true if true
		if (text.equals("true")) {
			return true;
		}
		else {
			return false;
		}
	}
}
