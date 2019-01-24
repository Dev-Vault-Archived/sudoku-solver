package sudoku.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public final class Field extends JLabel {
    private final int x;
    private final int y;

    public Field(int x, int y) {
        super("", CENTER);
        this.x = x;
        this.y = y;
        
        initialize();
    }
    
    public void initialize() {
        setPreferredSize(new Dimension(40, 40));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        setOpaque(true);
    }

    public void setNumber(int number, boolean userInput) {
        setForeground(userInput ? Color.BLUE : Color.BLACK);
        setText(number > 0 ? number + "" : "");
    }

    public int getFieldX() {
        return x;
    }
    
    public int getFieldY() {
        return y;
    }
}