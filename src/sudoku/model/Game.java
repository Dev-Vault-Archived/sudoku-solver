package sudoku.model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.swing.JOptionPane;
import sudoku.view.SudokuPanel;

public final class Game extends Observable {

    private int[][] solution;
    private int[][] game;

    private final int BOARD_SIZE = 9;
    private final int BOARD_START_INDEX = 0;
    private final int SUBSECTION_SIZE = 3;

    private boolean isValid(int row, int column) {
        return (rowConstraint(game, row)
                && columnConstraint(game, column)
                && subsectionConstraint(row, column));
    }

    private boolean rowConstraint(int[][] board, int row) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
                .allMatch(column -> checkConstraint(row, constraint, column));
    }

    private boolean columnConstraint(int[][] board, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
                .allMatch(row -> checkConstraint(row, constraint, column));
    }

    private boolean subsectionConstraint(int row, int column) {
        boolean[] constraint = new boolean[BOARD_SIZE];
        int subsectionRowStart = (row / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE;

        int subsectionColumnStart = (column / SUBSECTION_SIZE) * SUBSECTION_SIZE;
        int subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColumnStart; c < subsectionColumnEnd; c++) {
                if (!checkConstraint(r, constraint, c)) {
                    return false;
                }
            }
        }
        return true;
    }

    private final int NO_VALUE = 0;
    private final int MIN_VALUE = 1;
    private final int MAX_VALUE = BOARD_SIZE;

    boolean checkConstraint(
            int row,
            boolean[] constraint,
            int column) {
        if (game[row][column] != NO_VALUE) {
            if (!constraint[game[row][column] - 1]) {
                constraint[game[row][column] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }

    private int loops = 0;
    
    private void changeCell() {
        
        loops++;
        
        sudokuPanel.update(this, UpdateAction.CHANGE_CELL);
    }
    
    private boolean solve() throws InterruptedException {
        for (int row = BOARD_START_INDEX; row < BOARD_SIZE; row++) {
            for (int column = BOARD_START_INDEX; column < BOARD_SIZE; column++) {
                if (game[row][column] == NO_VALUE) {
                    for (int k = MIN_VALUE; k <= MAX_VALUE; k++) {
                        game[row][column] = k;
                        changeCell();
                        
                        if (isValid(row, column) && solve()) {
                            return true;
                        }
                        
                        game[row][column] = NO_VALUE;
                        changeCell();
                    }
                    return false;
                }
            }
        }

        return true;
    }

    public Game() {
        newGame();
    }

    public void newGame() {
        
        setChanged();
        notifyObservers(UpdateAction.NEW_GAME_GENERATION);
        
        Thread bg = new Thread(() -> {
            solution = generateSolution(new int[BOARD_SIZE][BOARD_SIZE], 0);
            game = generateGame(copy(solution));
            
            setChanged();
            notifyObservers(UpdateAction.NEW_GAME_COMPLETED);
        });
        
        bg.start();
    }

    SudokuPanel sudokuPanel;

    public void setSudokuController(SudokuPanel s) {
        sudokuPanel = s;
    }

    public void solveGame() {
        
        Locale locale = new Locale("id", "ID");

        NumberFormat numberFormat = NumberFormat.getInstance(locale);

        setChanged();
        notifyObservers(UpdateAction.SOLVE);
        
        Game _t = this;
        
        loops = 0;
        
        Thread background = new Thread(() -> {
            try {
                boolean isSolved = _t.solve();
                
                if (isSolved) {
                    JOptionPane.showMessageDialog(null, "Game sudoku anda telah terpecahkan dengan total loop : " + numberFormat.format(loops) + "!", "Sudoku Solved!", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Game sudoku tidak memiliki penyelesaian! Anda bisa coba menekan solve lagi.", "Sudoku Solving Failed!", JOptionPane.ERROR_MESSAGE);
                }
                
                setChanged();
                notifyObservers(UpdateAction.SOLVE_COMPLETED);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        background.start();
        
    }

    public void setNumber(int x, int y, int number) {
        game[y][x] = number;
    }

    public int getNumber(int x, int y) {
        return game[y][x];
    }

    private boolean isPossibleX(int[][] game, int y, int number) {
        for (int x = 0; x < BOARD_SIZE; x++) {
            if (game[y][x] == number) {
                return false;
            }
        }
        return true;
    }

    private boolean isPossibleY(int[][] game, int x, int number) {
        for (int y = 0; y < BOARD_SIZE; y++) {
            if (game[y][x] == number) {
                return false;
            }
        }
        return true;
    }

    private boolean isPossibleBlock(int[][] game, int x, int y, int number) {
        int x1 = x < 3 ? 0 : x < 6 ? 3 : 6;
        int y1 = y < 3 ? 0 : y < 6 ? 3 : 6;
        for (int yy = y1; yy < y1 + 3; yy++) {
            for (int xx = x1; xx < x1 + 3; xx++) {
                if (game[yy][xx] == number) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getNextPossibleNumber(int[][] game, int x, int y, List<Integer> numbers) {
        while (numbers.size() > 0) {
            int number = numbers.remove(0);
            if (isPossibleX(game, y, number) && isPossibleY(game, x, number) && isPossibleBlock(game, x, y, number)) {
                return number;
            }
        }
        return -1;
    }

    private int[][] generateSolution(int[][] game, int index) {
        if (index > 80) {
            return game;
        }

        int x = index % BOARD_SIZE;
        int y = index / BOARD_SIZE;

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= BOARD_SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        while (numbers.size() > 0) {
            int number = getNextPossibleNumber(game, x, y, numbers);
            if (number == -1) {
                return null;
            }

            game[y][x] = number;
            int[][] tmpGame = generateSolution(game, index + 1);
            if (tmpGame != null) {
                return tmpGame;
            }
            game[y][x] = 0;
        }

        return null;
    }

    private int[][] generateGame(int[][] game) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);
        return generateGame(game, positions);
    }

    private int[][] generateGame(int[][] game, List<Integer> positions) {
        while (positions.size() > 0) {
            int position = positions.remove(0);
            int x = position % BOARD_SIZE;
            int y = position / BOARD_SIZE;
            int temp = game[y][x];
            game[y][x] = 0;

            if (!isValid(game)) {
                game[y][x] = temp;
            }
        }

        return game;
    }

    private boolean isValid(int[][] game) {
        return isValid(game, 0, new int[]{0});
    }
    
    private boolean isValid(int[][] game, int index, int[] numberOfSolutions) {
        if (index > 80) {
            return ++numberOfSolutions[0] == 1;
        }

        int x = index % BOARD_SIZE;
        int y = index / BOARD_SIZE;

        if (game[y][x] == 0) {
            List<Integer> numbers = new ArrayList<>();
            for (int i = 1; i <= BOARD_SIZE; i++) {
                numbers.add(i);
            }

            while (numbers.size() > 0) {
                int number = getNextPossibleNumber(game, x, y, numbers);
                if (number == -1) {
                    break;
                }
                game[y][x] = number;

                if (!isValid(game, index + 1, numberOfSolutions)) {
                    game[y][x] = 0;
                    return false;
                }
                game[y][x] = 0;
            }
        } else if (!isValid(game, index + 1, numberOfSolutions)) {
            return false;
        }

        return true;
    }
    
    private int[][] copy(int[][] game) {
        int[][] copy = new int[BOARD_SIZE][BOARD_SIZE];
        for (int y = 0; y < BOARD_SIZE; y++) {
            System.arraycopy(game[y], 0, copy[y], 0, BOARD_SIZE);
        }
        return copy;
    }
    
}
