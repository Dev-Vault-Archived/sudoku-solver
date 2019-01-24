package sudoku.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import sudoku.model.Game;
import sudoku.model.UpdateAction;

public final class SudokuPanel extends JPanel implements Observer {
    private Field[][] fields;
    private JPanel[][] panels;

    public SudokuPanel() {
        super(new GridLayout(3, 3));

        initialize();
    }
    
    public void initialize() {
        panels = new JPanel[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                panels[y][x] = new JPanel(new GridLayout(3, 3));
                panels[y][x].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                add(panels[y][x]);
            }
        }

        fields = new Field[9][9];
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                fields[y][x] = new Field(x, y);
                panels[y / 3][x / 3].add(fields[y][x]);
            }
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        switch ((UpdateAction)arg) {
            case NEW_GAME_COMPLETED:
                setGameInitial((Game)o);
                break;
            case CHANGE_CELL:
                setGame((Game)o);
                break;
        }
    }

    public void setGame(Game game) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                fields[y][x].setBackground(Color.WHITE);
                fields[y][x].setNumber(game.getNumber(x, y), false);
            }
        }
    }
    
    public void setGameInitial(Game game) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                fields[y][x].setBackground(Color.WHITE);
                fields[y][x].setNumber(game.getNumber(x, y), true);
            }
        }
    }
}