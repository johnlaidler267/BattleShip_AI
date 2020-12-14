import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class UserBoard {
	protected static int undetectedShipNumber;
	protected static int gameBoardLength;
	protected static HashMap<Character, Integer> ShipsMap;
	protected static char[][] gameBoard;
	private static char water = '~';
	protected static char hit = 'X';
	protected static char miss = 'O';

	public void setUp(int boardLength, HashMap<Character, Integer> ships) {
		gameBoardLength = boardLength;
		ShipsMap = ships;
		undetectedShipNumber = ShipsMap.size();

		// create/print gameboard of given length & initialize w/ ships in random
		// locations
		gameBoard = createGameboard();
		printGameBoard();
	}

	public void guess() {
		char[] resultOfGuess = { 'n', 'n' };
		int[] guess = null;
		while (resultOfGuess[1] == 'n') {
			guess = getGuessCoordinates();
			resultOfGuess = evaluateGuess(guess);
		}
		if (resultOfGuess[1] == hit) {
			ShipsMap.replace(resultOfGuess[0], ShipsMap.get(resultOfGuess[0]) - 1);
			if (!boardContains(resultOfGuess[0])) {
				undetectedShipNumber--;
				System.out.println("AI sunk your " + resultOfGuess[0]);
				System.out.println();
			}
		}
		gameBoard = updateGameBoard(guess, resultOfGuess);
		printGameBoard();
	}

	protected boolean boardContains(char c) {
		return ShipsMap.get(c) != 0;
	}

	public int getUndetectedNumber() {
		return undetectedShipNumber;
	}

	protected static char[][] updateGameBoard(int[] guess, char[] resultOfGuess) {	
		int Y = guess[0];
		int X = guess[1];
		gameBoard[Y][X] = resultOfGuess[1];
		return gameBoard;
	}

	protected static char[] evaluateGuess(int[] guess) {
		String message;
		int Y = guess[0];
		int X = guess[1];

		// target = whatever is currently in the coordinate of the guess
		char target = gameBoard[Y][X];
		char resultOfGuess = 'x';

		if (shipsArrayIncludes(target)) {
			message = "The AI hit a ship!";
			resultOfGuess = hit;
		} else if (target == water) {
			message = "The AI missed!";
			resultOfGuess = miss;
		} else {
			message = "";
			resultOfGuess = 'n';
		}

		System.out.println("\n" + message + "\n");
		return new char[] { target, resultOfGuess };
	}

	private static boolean shipsArrayIncludes(char position) {
		return (position == 'A' || position == 'B' || position == 'C' || position == 'D' || position == 'S');
	}

	@SuppressWarnings("resource")
	private static int[] getGuessCoordinates() {
		int X = new Random().nextInt(gameBoardLength);
		int Y = new Random().nextInt(gameBoardLength);
		return new int[] { Y, X };
	}

	protected static void printGameBoard() {
		System.out.println("YOUR GAME BOARD  ");

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
				System.out.print(position + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static char[][] createGameboard() {
		gameBoard = new char[gameBoardLength][gameBoardLength];
		for (char[] row : gameBoard) {
			Arrays.fill(row, water);
		}
		return placeShips();
	}

	private static char[][] placeShips() {
		String type = "";
		for (Character ship : ShipsMap.keySet()) {
			switch (ship) {
			case 'A':
				type = "Aircraft carrier";
				break;
			case 'B':
				type = "Battleship";
				break;
			case 'C':
				type = "Cruiser";
				break;
			case 'S':
				type = "Submariner";
				break;
			case 'D':
				type = "Destroyer";
				break;

			}
			String orientation = getShipOrientation(type);
			int[] shipLocation = getShipCoordinates(ShipsMap.get(ship), orientation, type);
			System.out.print("ShipLocation : " + shipLocation[0] + shipLocation[1]);
			placeShip(ship, ShipsMap.get(ship), shipLocation, orientation);
			System.out.println();
			printGameBoard();
		}
		return gameBoard;
	}

	private static String getShipOrientation(String type) {
		System.out.print("orientation of " + type + " : ");
		String orientation = new Scanner(System.in).nextLine();
		if (!orientation.equals("horizontal") && !orientation.equals("vertical")) {
			return getShipOrientation(type);
		} else {
			return orientation;
		}
	}

	private static void placeShip(char ship, int length, int[] shipLocation, String orientation) {
		int startingRow = shipLocation[0];
		int startingColumn = shipLocation[1];
		int index;
		char type = ship;
		if (orientation.equals("horizontal")) {
			index = startingColumn;
			for (int i = 0; i < length; i++) {
				gameBoard[startingRow][index] = type;
				index--;
			}
		} else {
			index = startingRow;
			for (int i = 0; i < length; i++) {
				gameBoard[index][startingColumn] = type;
				index++;
			}
		}
	}

	private static boolean validCoordinate(int length, String orientation, int[] shipCoordinates) {
		int startingRow = shipCoordinates[0];
		int startingColumn = shipCoordinates[1];
		int index;
		if (orientation.equals("horizontal")) {
			index = startingColumn;
			for (int i = 0; i < length; i++) {
				if (index >= 0 && index < gameBoardLength) {
					if (gameBoard[startingRow][index] != water) {
						System.out.print("gameBoard at spot : " + gameBoard[startingRow][index]);
						return false;
					}
				} else {
					return false;
				}
				index--;
			}
			return true;
		} else {
			index = startingRow;
			for (int i = 0; i < length; i++) {
				if (index >= 0 && index < gameBoardLength) {
					if (gameBoard[index][startingColumn] != water) {
						return false;
					}
				} else {
					return false;
				}
				index++;
			}
			return true;
		}
	}

	@SuppressWarnings("resource")
	private static int[] getShipCoordinates(int length, String orientation, String type) {
		int[] shipCoordinates = new int[2];

		// Prompt user for column (X coordinate)
		System.out.print("X coordinate of " + type + " : ");
		shipCoordinates[1] = new Scanner(System.in).nextInt() - 1;
		int X = shipCoordinates[1];

		// Prompt user for row (Y coordinate)
		System.out.print("Y coordinate of " + type + " : ");
		shipCoordinates[0] = new Scanner(System.in).nextInt() - 1;
		int Y = shipCoordinates[0];

		
		if (Y < 0 || X < 0 || Y >= gameBoardLength || X >= gameBoardLength|| !validCoordinate(length, orientation, shipCoordinates)) {
			System.out.println("Invalid coordinate");
			return getShipCoordinates(length, orientation, type);
		}
		// Return XY coordinate of guess
		return new int[] { Y, X };
	}
}