package battleship;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The controller for the Swing Battleship game.
 */
public class SwingBattleshipController implements BattleshipController, ActionListener {
  private final BattleshipModel model;
  private final BattleshipView view;

  /**
   * Constructs a SwingBattleshipController.
   *
   * @param model the game model
   * @param view  the game view
   */
  public SwingBattleshipController(BattleshipModel model, BattleshipView view) {
    this.model = model;
    this.view = view;
    view.addGridButtonListener(this);
  }

  @Override
  public void playGame() {
    model.startGame();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    String[] parts = command.split(",");
    if (parts.length != 2) {
      return;
    }

    int row = Integer.parseInt(parts[0]);
    int col = Integer.parseInt(parts[1]);

    try {
      model.makeGuess(row, col);  // This may throw if already guessed or out of bounds

      // Always get updated state from model
      CellState[][] grid = model.getCellGrid();
      CellState state = grid[row][col];
      view.updateCell(row, col, state);

      if (state == CellState.HIT) {
        view.updateStatus("Hit!");
      } else if (state == CellState.MISS) {
        view.updateStatus("Miss!");
      }

      int remaining = model.getMaxGuesses() - model.getGuessCount();
      view.updateMoves(remaining);

      if (model.isGameOver()) {
        if (model.areAllShipsSunk()) {
          view.updateStatus("Congratulations! You sunk all the ships!");
        } else {
          view.updateStatus("Game Over! Out of moves.");
        }
        view.revealShips(model.getShipGrid());
        view.disableGrid();
      }

    } catch (IllegalArgumentException ex) {
      view.updateStatus("Invalid move: " + ex.getMessage());
    } catch (IllegalStateException ex) {
      view.updateStatus("Game is over. " + ex.getMessage());
    }
  }
}
