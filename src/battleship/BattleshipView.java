package battleship;

/**
 * The BattleshipView interface defines the view functionality for the Battleship game.
 */
public interface BattleshipView {
  /**
   * Registers an ActionListener for the grid buttons.
   *
   * @param listener the ActionListener to register
   */
  void addGridButtonListener(java.awt.event.ActionListener listener);

  /**
   * Updates the appearance of a cell based on its state.
   *
   * @param row   the row index
   * @param col   the column index
   * @param state the cell state (HIT, MISS, or UNKNOWN)
   */
  void updateCell(int row, int col, CellState state);

  /**
   * Updates the moves label with the number of remaining moves.
   *
   * @param remainingMoves the number of remaining moves
   */
  void updateMoves(int remainingMoves);

  /**
   * Updates the status message displayed to the player.
   *
   * @param message the status message
   */
  void updateStatus(String message);

  /**
   * Disables all grid buttons.
   */
  void disableGrid();

  /**
   * Reveals the ship grid after the game is over.
   *
   * @param shipGrid the grid containing ship positions
   */
  void revealShips(ShipType[][] shipGrid);
}
