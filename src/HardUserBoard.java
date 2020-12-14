import java.util.ArrayList;
import java.util.Random;

public class HardUserBoard extends UserBoard {
	private char typeOfShipHit = 'x';
	private char typeOfShipFound = 'x';
	private int[] guess = null;
	private int[] lastGuess = null;
	char direction = 'n';

	public void guess() {
		if (typeOfShipFound == 'x') {
			char[] resultOfGuess = { 'n', 'n' };

			// search until the AI finds spot that it hasn't already guessed
			while (resultOfGuess[1] == 'n') {
				guess = getGuessCoordinates();
				resultOfGuess = evaluateGuess(guess);
			}

			char guessHitOrMiss = resultOfGuess[1];
			char typeOfShip = resultOfGuess[0];

			// if he result of the guess is a hit on a ship, decrease the ship's count and
			// update typeOfShipFound
			if (guessHitOrMiss == hit) {
				ShipsMap.replace(typeOfShip, ShipsMap.get(typeOfShip) - 1);
				typeOfShipFound = typeOfShip;
			}

			// if it were a miss, simply update the gameboard in the correct coordinate
			gameBoard = updateGameBoard(guess, resultOfGuess);

		} else {
			// If a ship has been found, get the next guess for where the rest is on grid.
			char lastGuessHitOrMiss = findRestOfShip(guess[0], guess[1]);

			if (lastGuessHitOrMiss == hit) {
				ShipsMap.replace(typeOfShipHit, ShipsMap.get(typeOfShipHit) - 1);
			}

			// If the board contains no more of a particular ship, decrease undetectedNumber
			// of ships.
			if (!boardContains(typeOfShipFound)) {
				undetectedShipNumber--;
				System.out.println("AI sunk your " + typeOfShipFound);
				System.out.println();
				typeOfShipFound = 'x';
				direction = 'n';
			}

			gameBoard = updateGameBoard(lastGuess, new char[] { typeOfShipHit, lastGuessHitOrMiss });
		}
		printGameBoard();

	}

	private char findRestOfShip(int Y, int X) {
		char[] resultOfGuess = { 'n', 'n' };

		if (direction == 'n') {

			lastGuess = getRandomCoordinates(Y, X);
			int column = lastGuess[1];
			int row = lastGuess[0];

			if (column < 0 || row < 0 || column >= gameBoardLength || row >= gameBoardLength) {
				direction = 'n';
				return findRestOfShip(Y, X);
			}

			resultOfGuess = evaluateGuess(lastGuess);

			if (resultOfGuess[1] == 'n') {
				direction = 'n';
				return findRestOfShip(Y, X);
			}

			char lastGuessHitOrMiss = resultOfGuess[1];

			// if the last guess was a hit, update the direction to point in the direction
			// of the coordinate guessed.
			if (lastGuessHitOrMiss == hit) {
				typeOfShipHit = resultOfGuess[0];
				if (lastGuess[1] < X) {
					direction = 'l';
				} else if (lastGuess[1] > X) {
					direction = 'r';
				} else if (lastGuess[0] > Y) {
					direction = 'd';
				} else {
					direction = 'u';
				}
			}

			return lastGuessHitOrMiss;
		} else {
			if (direction == 'l') {

				lastGuess = new int[] { lastGuess[0], lastGuess[1] - 1 };
				if (lastGuess[1] < 0) {
					direction = 'n';
					return findRestOfShip(Y, X);
				}
				return getRandomCoordinatesHelper(resultOfGuess, Y, X);
			}
			if (direction == 'r') {
				lastGuess = new int[] { lastGuess[0], lastGuess[1] + 1 };
				if (lastGuess[1] >= gameBoardLength) {
					direction = 'n';
					return findRestOfShip(Y, X);
				}
				return getRandomCoordinatesHelper(resultOfGuess, Y, X);
			}
			if (direction == 'd') {

				lastGuess = new int[] { lastGuess[0] + 1, lastGuess[1] };
				if (lastGuess[0] >= gameBoardLength) {
					direction = 'n';
					return findRestOfShip(Y, X);
				}
				return getRandomCoordinatesHelper(resultOfGuess, Y, X);
			} else {

				lastGuess = new int[] { lastGuess[0] - 1, lastGuess[1] };
				if (lastGuess[0] < 0) {
					direction = 'n';
					return findRestOfShip(Y, X);
				}
				return getRandomCoordinatesHelper(resultOfGuess, Y, X);
			}
		}
	}

	private char getRandomCoordinatesHelper(char[] resultOfGuess, int Y, int X) {
		resultOfGuess = evaluateGuess(lastGuess);
		if (resultOfGuess[1] == 'n') {
			direction = 'n';
			return findRestOfShip(Y, X);
		}
		if (resultOfGuess[1] == miss) {
			direction = 'n';
		} else {
			typeOfShipHit = resultOfGuess[0];
		}
		return resultOfGuess[1];

	}

	private int[] getRandomCoordinates(int y, int x) {
		int random = new Random().nextInt(4);
		if (random == 0) {
			return new int[] { y, x - 1 };
		}
		if (random == 1) {
			return new int[] { y, x + 1 };
		}
		if (random == 2) {
			return new int[] { y - 1, x };
		} else {
			return new int[] { y + 1, x };
		}
	}

	private static int[] getGuessCoordinates() {
		int X = new Random().nextInt(gameBoardLength);
		int Y = new Random().nextInt(gameBoardLength);
		if (X + Y % 2 == 0) {
			return getGuessCoordinates();
		}
		return new int[] { Y, X };
	}
}
