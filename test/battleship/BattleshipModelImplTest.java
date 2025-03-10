package battleship;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the BattleshipModelImpl class using JUnit4.
 */
public class BattleshipModelImplTest {
  private BattleshipModelImpl model;

  /**
   * Sets up a new BattleshipModelImpl instance before each test.
   */
  @Before
  public void setUp() {
    model = new BattleshipModelImpl();
    model.startGame();
  }

  /**
   * Tests the initial game state to ensure it starts correctly.
   */
  @Test
  public void testInitialGameState() {
    assertFalse(model.isGameOver());
    assertEquals(0, model.getGuessCount());
    assertEquals(50, model.getMaxGuesses());
    assertNotNull(model.getCellGrid());
  }

  /**
   * Tests making a valid guess that results in a miss.
   */
  @Test
  public void testMakeGuessMiss() {
    int row = 0;
    int col = 0;
    while (model.getShipGrid()[row][col] != null) {
      col++;
    }
    boolean result = model.makeGuess(row, col);
    assertFalse(result);
    assertEquals(1, model.getGuessCount());
  }

  /**
   * Tests making a valid guess that results in a hit.
   */
  @Test
  public void testMakeGuessHit() {
    ShipType[][] shipGrid = model.getShipGrid();
    int hitRow = -1;
    int hitCol = -1;

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (shipGrid[i][j] != null) {
          hitRow = i;
          hitCol = j;
          break;
        }
      }
      if (hitRow != -1) {
        break;
      }
    }

    assertTrue(model.makeGuess(hitRow, hitCol));
  }

  /**
   * Tests making a guess outside of the grid bounds.
   */
  @Test
  public void testMakeGuessOutOfBounds() {
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(-1, 0));
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(10, 10));
  }

  /**
   * Tests making a guess on a cell that was already guessed.
   */
  @Test
  public void testMakeGuessAlreadyGuessed() {
    model.makeGuess(1, 1);
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(1, 1));
  }

  /**
   * Tests making guesses until the game is over using only empty cells,
   * then attempting another guess.
   * This test is expected to throw an IllegalStateException.
   * The game is over when the maximum number of guesses is reached.
   */
  @Test
  public void testMakeGuessAfterGameOver() {
    ShipType[][] shipGrid = model.getShipGrid();
    int guessesMade = 0;

    for (int i = 0; i < 10 && guessesMade < model.getMaxGuesses(); i++) {
      for (int j = 0; j < 10 && guessesMade < model.getMaxGuesses(); j++) {
        if (shipGrid[i][j] == null) { // Ensure we're only guessing empty cells
          model.makeGuess(i, j);
          guessesMade++;
        }
      }
    }

    assertEquals(50, model.getGuessCount());
    assertTrue(model.isGameOver());
    assertThrows(IllegalStateException.class, () -> model.makeGuess(5, 5));
  }

  /**
   * Tests if all ships are sunk when all ship positions are guessed.
   * Uses a cheat mode to retrieve ship locations directly.
   */
  @Test
  public void testAllShipsSunk() {
    ShipType[][] shipGrid = model.getShipGrid();

    // Cheat mode: Ensure we hit all ship locations
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if (shipGrid[i][j] != null) {
          model.makeGuess(i, j);
        }
        if (model.areAllShipsSunk()) {
          break;
        }
      }
      if (model.areAllShipsSunk()) {
        break;
      }
    }

    assertTrue(model.areAllShipsSunk());
    assertTrue(model.isGameOver());
  }
}
