import battleship.BattleshipController;
import battleship.BattleshipModel;
import battleship.BattleshipModelImpl;
import battleship.BattleshipView;
import battleship.SwingBattleshipController;
import battleship.SwingBattleshipView;

/**
 * The Main class contains the main method that runs the Battleship game.
 */
public class Main {
  /**
   * The main method that runs the Battleship game.
   *
   * @param args not used
   */
  public static void main(String[] args) {
    BattleshipModel model = new BattleshipModelImpl();
    BattleshipView view = new SwingBattleshipView();
    BattleshipController controller = new SwingBattleshipController(model, view);

    controller.playGame();
  }
}
