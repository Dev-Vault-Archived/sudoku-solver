package sudoku.model;

/**
 * Enumeration used to inform observers what to update.
 *
 * @author Eric Beijer
 */
public enum UpdateAction {
    NEW_GAME_GENERATION,
    NEW_GAME_COMPLETED,
    CHANGE_CELL,
    SOLVE,
    SOLVE_COMPLETED
}