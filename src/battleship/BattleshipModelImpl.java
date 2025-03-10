package battleship;

import java.util.Random;

/**
 * Represents the model of the Battleship game.
 *
 */
public class BattleshipModelImpl implements BattleshipModel {
  private static final int GRID_SIZE = 10;
  private static final int MAX_GUESSES = 50;

  private final CellState[][] cellGrid;
  private final ShipType[][] shipGrid;

  private final ShipType[] shipStatus;

  private int guessCount;
  private boolean gameOver;

  /**
   * Constructs a new instance of the Battleship game model.
   * Initializes the cell grid and ship grid.
   * As well as the ship status array.
   */
  public BattleshipModelImpl() {
    cellGrid = new CellState[GRID_SIZE][GRID_SIZE];
    shipGrid = new ShipType[GRID_SIZE][GRID_SIZE];
    shipStatus = new ShipType[5];
    guessCount = 0;
    gameOver = false;

    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        cellGrid[i][j] = CellState.UNKNOWN;
      }
    }
  }

  @Override
  public void startGame() {
    placeShipsRandomly();
    // we have 5 ships, so we need 5 elements in the array to keep track of the status of each ship
    // when a ship being hit by player, we will remove it from the array
    shipStatus[0] = ShipType.AIRCRAFT_CARRIER;
    shipStatus[1] = ShipType.BATTLESHIP;
    shipStatus[2] = ShipType.SUBMARINE;
    shipStatus[3] = ShipType.DESTROYER;
    shipStatus[4] = ShipType.PATROL_BOAT;

    /* Cheat mode for testing */
    // System.out.println("\nShip Grid showed for testing:");
    // System.out.print("  ");
    // for (int i = 0; i < shipGrid[0].length; i++) {
    //   System.out.print(i + " ");
    // }
    // System.out.println();
    // for (int i = 0; i < shipGrid.length; i++) {
    //   System.out.print((char) ('A' + i) + " ");
    //   for (int j = 0; j < shipGrid[i].length; j++) {
    //     System.out.print(shipGrid[i][j] == null ? "- " : shipGrid[i][j].getSymbol() + " ");
    //   }
    //   System.out.println();
    // }
  }

  private void placeShipsRandomly() {
    Random random = new Random();
    ShipType[] ships = ShipType.values();

    for (ShipType ship : ships) {
      boolean placed = false;
      while (!placed) {
        int row = random.nextInt(GRID_SIZE);
        int col = random.nextInt(GRID_SIZE);
        boolean horizontal = random.nextBoolean();

        if (canPlaceShip(ship, row, col, horizontal)) {
          placeShip(ship, row, col, horizontal);
          placed = true;
        }
      }
    }
  }

  private boolean canPlaceShip(ShipType ship, int row, int col, boolean horizontal) {
    int size = ship.getSize();
    if (horizontal) {
      if (col + size > GRID_SIZE) {
        return false;
      }
      for (int i = 0; i < size; i++) {
        if (shipGrid[row][col + i] != null) {
          return false;
        }
      }
    } else {
      if (row + size > GRID_SIZE) {
        return false;
      }
      for (int i = 0; i < size; i++) {
        if (shipGrid[row + i][col] != null) {
          return false;
        }
      }
    }
    return true;
  }

  private void placeShip(ShipType ship, int row, int col, boolean horizontal) {
    int size = ship.getSize();
    if (horizontal) {
      for (int i = 0; i < size; i++) {
        shipGrid[row][col + i] = ship;
      }
    } else {
      for (int i = 0; i < size; i++) {
        shipGrid[row + i][col] = ship;
      }
    }
  }

  @Override
  public boolean makeGuess(int row, int col) {
    if (gameOver) {
      throw new IllegalStateException("Game is already over.");
    }
    if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
      throw new IllegalArgumentException("Coordinates out of bounds.");
    }
    if (cellGrid[row][col] != CellState.UNKNOWN) {
      throw new IllegalArgumentException("Cell already guessed.");
    }

    guessCount++;
    if (guessCount == MAX_GUESSES) {
      gameOver = true;
    }

    if (shipGrid[row][col] != null) {
      cellGrid[row][col] = CellState.HIT;
      ShipType ship = shipGrid[row][col];
      removeShip(ship);
      if (areAllShipsSunk()) {
        gameOver = true;
      }
      return true;
    } else {
      cellGrid[row][col] = CellState.MISS;
      return false;
    }
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  /**
  * When one ship got hit, we need to remove it from the array.
  */
  private void removeShip(ShipType ship) {
    for (int i = 0; i < shipStatus.length; i++) {
      if (shipStatus[i] == ship) {
        shipStatus[i] = null;
        break;
      }
    }
  }

  /**
   * Checks if all ships have been sunk.
   * Just one hit should sink a ship.
   *
   * @return true if all ships are sunk, false otherwise
   */
  @Override
  public boolean areAllShipsSunk() {
    return shipStatus[0] == null
        && shipStatus[1] == null
        && shipStatus[2] == null
        && shipStatus[3] == null
        && shipStatus[4] == null;
  }

  @Override
  public int getGuessCount() {
    return guessCount;
  }

  @Override
  public int getMaxGuesses() {
    return MAX_GUESSES;
  }

  @Override
  public CellState[][] getCellGrid() {
    CellState[][] copy = new CellState[GRID_SIZE][GRID_SIZE];
    for (int i = 0; i < GRID_SIZE; i++) {
      System.arraycopy(cellGrid[i], 0, copy[i], 0, GRID_SIZE);
    }
    return copy;
  }

  @Override
  public ShipType[][] getShipGrid() {
    ShipType[][] copy = new ShipType[GRID_SIZE][GRID_SIZE];
    for (int i = 0; i < GRID_SIZE; i++) {
      System.arraycopy(shipGrid[i], 0, copy[i], 0, GRID_SIZE);
    }
    return copy;
  }
}