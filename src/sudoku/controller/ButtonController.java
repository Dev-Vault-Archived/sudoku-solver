package sudoku.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import sudoku.model.Game;

public class ButtonController implements ActionListener {
    private final Game game;

    public ButtonController(Game game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Baru":
                game.newGame();
                break;
            case "Solve - With Backtracking":
                game.solveGame();
                break;
            case "Keluar":
                System.exit(0);
            default:
                break;
        }
    }
}