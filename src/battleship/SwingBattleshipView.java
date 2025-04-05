package battleship;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * A clean Swing-based view for the Battleship game with hit animations and modern styling.
 */
public class SwingBattleshipView extends JFrame implements BattleshipView {
  public static final int GRID_SIZE = 10;

  private static final Color DARK_BG = new Color(30, 30, 30);
  private static final Color DARK_CELL = new Color(0, 120, 0);

  private final JButton[][] gridButtons;
  private final JLabel statusLabel;
  private final JLabel guessLabel;

  /**
   * Constructs the Battleship GUI view.
   */
  public SwingBattleshipView() {
    super("Battleship Game");
    gridButtons = new JButton[GRID_SIZE][GRID_SIZE];
    statusLabel = new JLabel("New game started. Make your guess!");
    guessLabel = new JLabel("Guesses: 0 / 50");

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));
    getContentPane().setBackground(DARK_BG);

    // Top info panel
    JPanel infoPanel = new JPanel(new BorderLayout());
    infoPanel.setBackground(DARK_BG);
    guessLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
    statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    guessLabel.setForeground(Color.WHITE);
    statusLabel.setForeground(Color.WHITE);
    infoPanel.add(guessLabel, BorderLayout.WEST);
    infoPanel.add(statusLabel, BorderLayout.EAST);

    // Center grid panel
    JPanel gridPanel = new JPanel(new GridBagLayout());
    gridPanel.setBackground(DARK_BG);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;

    for (int col = 0; col <= GRID_SIZE; col++) {
      gbc.gridx = col;
      gbc.gridy = 0;
      JLabel label;
      if (col == 0) {
        label = new JLabel(""); // Top-left corner
      } else {
        label = new JLabel(String.valueOf(col - 1), SwingConstants.CENTER);
      }
      styleHeader(label);
      gridPanel.add(label, gbc);
    }

    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col <= GRID_SIZE; col++) {
        gbc.gridy = row + 1;
        gbc.gridx = col;

        if (col == 0) {
          JLabel rowLabel = new JLabel(String.valueOf((char) ('A' + row)), SwingConstants.CENTER);
          styleHeader(rowLabel);
          gridPanel.add(rowLabel, gbc);
        } else {
          JButton button = new JButton();
          button.setPreferredSize(new Dimension(40, 40));
          button.setMargin(new Insets(0, 0, 0, 0));
          button.setFocusPainted(false);
          button.setBackground(DARK_CELL);
          button.setForeground(Color.WHITE);
          button.setFont(new Font("SansSerif", Font.BOLD, 16));
          button.setActionCommand(row + "," + (col - 1));
          gridButtons[row][col - 1] = button;
          gridPanel.add(button, gbc);
        }
      }
    }

    // Bottom control panel
    JPanel controlPanel = new JPanel();
    JButton exitButton = new JButton("Exit");

    exitButton.setFocusPainted(false);
    exitButton.setFont(new Font("SansSerif", Font.PLAIN, 14));

    controlPanel.add(exitButton);

    exitButton.addActionListener(e -> System.exit(0));

    add(infoPanel, BorderLayout.NORTH);
    add(gridPanel, BorderLayout.CENTER);
    add(controlPanel, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  @Override
  public void addGridButtonListener(ActionListener listener) {
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        gridButtons[i][j].addActionListener(listener);
      }
    }
  }

  @Override
  public void updateCell(int row, int col, CellState state) {
    JButton button = gridButtons[row][col];
    switch (state) {
      case HIT:
        button.setText("X");
        animateFlash(button, new Color(200, 0, 0));
        break;
      case MISS:
        button.setText("O");
        button.setBackground(new Color(30, 144, 255));
        break;
      default:
        button.setText("");
        button.setBackground(DARK_CELL);
        break;
    }
    button.setEnabled(false);
  }

  @Override
  public void updateMoves(int remainingMoves) {
    int used = 50 - remainingMoves;
    guessLabel.setText("Guesses: " + used + " / 50");
  }

  @Override
  public void updateStatus(String message) {
    statusLabel.setText(message);
  }

  @Override
  public void disableGrid() {
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        gridButtons[i][j].setEnabled(false);
      }
    }
  }

  @Override
  public void revealShips(ShipType[][] shipGrid) {
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        if (shipGrid[i][j] != null && gridButtons[i][j].isEnabled()) {
          gridButtons[i][j].setText(shipGrid[i][j].getSymbol());
          gridButtons[i][j].setBackground(Color.LIGHT_GRAY);
          gridButtons[i][j].setEnabled(false);
        }
      }
    }
  }

  /**
   * Applies header label styling.
   */
  private void styleHeader(JLabel label) {
    label.setPreferredSize(new Dimension(40, 40));
    label.setOpaque(true);
    label.setBackground(new Color(200, 200, 200));
    label.setFont(new Font("Monospaced", Font.BOLD, 13));
    label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
  }

  /**
   * Flash animation for hits.
   */
  private void animateFlash(JButton button, Color targetColor) {
    Timer timer = new Timer(100, null);
    final int[] count = {0};

    timer.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (count[0] % 2 == 0) {
          button.setBackground(Color.BLACK);
        } else {
          button.setBackground(targetColor);
        }
        count[0]++;
        if (count[0] >= 6) {
          timer.stop();
        }
      }
    });
    timer.start();
  }
}
