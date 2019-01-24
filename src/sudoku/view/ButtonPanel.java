package sudoku.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import sudoku.controller.ButtonController;
import sudoku.model.UpdateAction;

public final class ButtonPanel extends JPanel implements Observer {
    JButton btnNew, btnSolve, btnExit;
    JCheckBox cbHelp;

    public ButtonPanel() {
        super(new BorderLayout());

        initialize();
    }
    
    public void initialize() {
        JPanel pnlAlign = new JPanel();
        pnlAlign.setLayout(new BoxLayout(pnlAlign, BoxLayout.PAGE_AXIS));
        add(pnlAlign, BorderLayout.NORTH);

        JPanel pnlOptions = new JPanel(new FlowLayout(FlowLayout.LEADING));
        pnlOptions.setBorder(BorderFactory.createTitledBorder(" Pilihan "));
        pnlAlign.add(pnlOptions);

        btnNew = new JButton("Baru");
        btnNew.setFocusable(false);
        btnNew.setEnabled(false);
        pnlOptions.add(btnNew);

        btnSolve = new JButton("Solve - With Backtracking");
        btnSolve.setFocusable(false);
        btnSolve.setEnabled(false);
        pnlOptions.add(btnSolve);

        btnExit = new JButton("Keluar");
        btnExit.setFocusable(false);
        pnlOptions.add(btnExit);
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((UpdateAction)arg) {
            case SOLVE:
            case NEW_GAME_GENERATION:
                btnSolve.setEnabled(false);
                btnNew.setEnabled(false);
                break;
            case SOLVE_COMPLETED:
            case NEW_GAME_COMPLETED:
                btnSolve.setEnabled(true);
                btnNew.setEnabled(true);
                break;
        }
    }

    public void setController(ButtonController buttonController) {
        btnNew.addActionListener(buttonController);
        btnSolve.addActionListener(buttonController);
        btnExit.addActionListener(buttonController);
    }
}