import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AIBoard {
	private static int undetectedShipNumber;
	protected static int gameBoardLength;
	private static HashMap<Character, Integer> ShipsMap;
	private static char[][] gameBoard;
	private static char water = '~';
	private static char hit = 'X';
	private static char miss = 'O';

	public void setUp(int boardLength, HashMap<Character, Integer> ships) {
		gameBoardLength = boardLength;
		ShipsMap = ships;
		undetectedShipNumber = ShipsMap.size();

		// create/print gameboard of given length & initialize w/ ships in random locations
		gameBoard = createGameboard();
		printGameBoard();
	}

	public void guess() {
		int[] guess = getGuessCoordinates();
		char[] resultOfGuess = evaluateGuess(guess); // returns {what it hit, whether it was a hit/miss}
		if (resultOfGuess[1] == hit) {
			System.out.println(ShipsMap.get('A'));
			ShipsMap.replace(resultOfGuess[0], ShipsMap.get(resultOfGuess[0]) - 1);
			System.out.println(ShipsMap.get('A'));
				if(!boardContains(resultOfGuess[0])) {
					undetectedShipNumber--;
					System.out.println("You sunk the AI's " + resultOfGuess[0]);
					System.out.println();
				}
		}
		gameBoard = updateGameBoard(guess, resultOfGuess);
		printGameBoard();
	}

	private boolean boardContains(char c) {
		return ShipsMap.get(c) != 0;
	}

	public int getUndetectedNumber() {
		return undetectedShipNumber;
	}

	private static char[][] updateGameBoard(int[] guess, char[] resultOfGuess) {
		int Y = guess[0];
		int X = guess[1];
		if (resultOfGuess[1] == 'h') {
			gameBoard[Y][X] = hit;
		}
		else if(resultOfGuess[1] == 'm') {
			gameBoard[Y][X] = miss;
		}
		else {
			gameBoard[Y][X] = resultOfGuess[1];
		}
		return gameBoard;
	}

	private static char[] evaluateGuess(int[] guess) {
		String message;
		int Y = guess[0];
		int X = guess[1];

		// target = whatever is currently in the coordinate of the guess
		char target = gameBoard[Y][X];
		char resultOfGuess = 'x';

		if (isShip(target)) {
			message = "You hit a ship!";
			resultOfGuess = hit;
		} else if (target == water) {
			message = "You missed!";
			resultOfGuess = miss;
		} else {
			message = "You already guessed that spot! (turn skipped)";
			if(target == hit) {
				resultOfGuess = 'h';
			}
			else {
				resultOfGuess = 'm';
			}
		}

		System.out.println("\n" + message + "\n");
		return new char[]{target, resultOfGuess};
	}

	@SuppressWarnings("resource")
	private static int[] getGuessCoordinates() {
		int X;
		int Y;

		// Prompt user for column (X coordinate)
		do {
			System.out.println();
			System.out.print("X coordinate of guess: ");
			X = new Scanner(System.in).nextInt();
		} while (X < 0 || X >= gameBoardLength + 1);

		// Prompt user for row (Y coordinate)
		do {
			System.out.print("Y coordinate of guess: ");
			Y = new Scanner(System.in).nextInt();
		} while (Y < 0 || Y >= gameBoardLength + 1);

		// Return XY coordinate of guess
		return new int[] { Y - 1, X - 1 };
	}

	private static void printGameBoard() {
		System.out.println("OPPONENT's GAME BOARD  ");
		
		// print out column numbers
		System.out.print("  ");
		for (int column = 0; column < gameBoardLength; column++) {
			System.out.print(column + 1 + " ");
		}
		System.out.println();

		for (int row = 0; row < gameBoardLength; row++) {
			System.out.print(row + 1 + " ");
			for (int column = 0; column < gameBoardLength; column++) {
				char position = gameBoard[row][column];
				if (isShip(position)) { // if there is a ship there, don't display it
					System.out.print(position + " ");
				} else {
					System.out.print(position + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private static boolean isShip(char position) {
		return (position == 'A' || position == 'B' || position == 'C' || position == 'D' || position == 'S');
	}

	private static char[][] createGameboard() {
		gameBoard = new char[gameBoardLength][gameBoardLength];
		for (char[] row : gameBoard) {
			Arrays.fill(row, water);
		}
		return placeShips();
	}

	private static char[][] placeShips() {
		for(Character ship: ShipsMap.keySet()) {
			int orientation = getShipOrientation();
			int[] shipLocation = getShipCoordinates(ShipsMap.get(ship), orientation);
			placeShip(ship, ShipsMap.get(ship), shipLocation, orientation);
		}
		return gameBoard;
	}

	private static void placeShip(char ship, int length, int[] shipLocation, int orientation) {
		int startingRow = shipLocation[0];
		int startingColumn = shipLocation[1];
		int index;
		char type = ship;
		if(orientation == 0) {
			index = startingColumn;
			for(int column = 0; column < length; column++) {
				gameBoard[startingRow][index] = type;
				index--;
			}
		}
		else {
			index = startingRow;
			for(int row = 0; row < length; row++) {
				gameBoard[row][startingColumn] = type;
				index--;
			}
		}
	}

	protected static boolean validCoordinate(int length, int orientation, int[] shipCoordinates) {
		int startingRow = shipCoordinates[0];
		int startingColumn = shipCoordinates[1];
		int index;
		if(orientation == 0) {
			index = startingColumn;
			for(int column = 0; column < length; column++) {
				if(index < 0 || gameBoard[startingRow][index] != water) { return false; }
				index--;
			}
			return true;
		}
		else {
			index = startingRow;
			for(int row = 0; row < length; row++) {
				if(index < 0 || gameBoard[index][startingColumn] != water) { return false; }
				index--;
			}
			return true;
		}
	}

	private static int getShipOrientation() {
		return new Random().nextInt(2);
	}

	private static int[] getShipCoordinates(int length, int orientation) {
		int[] shipCoordinates = new int[2]; // array will contain x,y coordinate of the ship
		for (int i = 0; i < shipCoordinates.length; i++) {
			shipCoordinates[i] = new Random().nextInt(gameBoardLength - length) + length; // generates random number between 0 -> 1-length of gameboard
		}
		if(validCoordinate(length, orientation, shipCoordinates)) {
			return shipCoordinates;
		}
		else {
			return getShipCoordinates(length, orientation);
		}
	}
		
}