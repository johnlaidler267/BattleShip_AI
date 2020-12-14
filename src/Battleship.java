import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Battleship {
	public static void main(String[] args) {
		int boardLength = 8;
		HashMap<Character, Integer> ships = new HashMap<Character, Integer>();
		ships.put('A', 5);
		ships.put('B', 4);
		ships.put('C', 3);
		ships.put('S', 3);
		ships.put('D', 2);
		
		UserBoard user; // board that the AI guesses
		AIBoard AI = new AIBoard(); // board that the user guesses
		
		String difficulty = promptUserDifficulty();
		
		if(difficulty.equals("easy")) {
			user = new UserBoard();
		}
		else {
			user = new HardUserBoard();
		}

		user.setUp(boardLength, ships);
		AI.setUp(boardLength, ships);

		while (user.getUndetectedNumber() > 0 && AI.getUndetectedNumber() > 0) {
			AI.guess(); //User making the guess
			if (AI.getUndetectedNumber() == 0) {
				userWin();
				break;
			}
			user.guess(); //AI making the guess
			if (user.getUndetectedNumber() == 0) {
				AIWin();
				break;
			}
		}
	}
	
	private static String promptUserDifficulty() {
		System.out.print("Choose difficulty (easy or hard) : ");
		return new Scanner(System.in).nextLine();
	}

	private static void AIWin() {
		System.out.println("The AI won the game!");
	}

	private static void userWin() {
		System.out.println("You won the game!");
	}

}
