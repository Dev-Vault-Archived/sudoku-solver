package sudoku.view;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import sudoku.controller.ButtonController;
import sudoku.model.Game;
import sudoku.model.UpdateAction;

/**
 * Main class of program.
 *
 * @author Eric Beijer
 */
public class Sudoku extends JFrame implements Observer {
    
    private final DisabledGlassPane glassPane = new DisabledGlassPane();
    
    public Sudoku() {
        super("Sudoku | With Solver using Backtracking");
    }
    
    public void initialize() {
        
        String html = "<html><body width='%1s'><h3>Backtracking</h3><h5>Solving Sudoku</h5>" + 
                "<p>Program dibuat untuk memenuhi tugas besar mata kuliah Struktur Data dan Algoritma 2</p>" + 
                "<br /><p><b>Anggota kelompok:</b><br /><ul><li>Afrima Dhia Defara (M0517003)</l1><li>Galih Akbar Moerbayaksa (M0517018)</l1><li>Maunab Galang Esanika (M0518028)</l1><li>Rizki Aulia Sari (M0517045)</l1></ul></p></body></html>";
        
        JOptionPane.showMessageDialog(this, String.format(html, 400, 250), "Informasi Program", JOptionPane.PLAIN_MESSAGE);
        
        glassPane.setBackground( new Color(255, 128, 128, 128) );
        glassPane.setForeground( Color.WHITE );
        
        setGlassPane(glassPane);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        Game game = new Game();

        ButtonController buttonController = new ButtonController(game);
        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.setController(buttonController);
        add(buttonPanel, BorderLayout.SOUTH);
        
        SudokuPanel sudokuPanel = new SudokuPanel();
//        sudokuPanel.setGameInitial(game);
        
        game.setSudokuController(sudokuPanel);
        
        add(sudokuPanel, BorderLayout.CENTER);

        game.addObserver(buttonPanel);
        game.addObserver(sudokuPanel);
        game.addObserver(this);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {}
        
        Sudoku sudoku = new Sudoku();
        sudoku.initialize();
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((UpdateAction)arg) {
            case NEW_GAME_GENERATION:
                glassPane.activate("Mohon tunggu...");
                break;
            case NEW_GAME_COMPLETED:
                glassPane.deactivate();
                break;
        }
    }
}