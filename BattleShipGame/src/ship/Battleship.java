package ship;

/**
 * Battleship.
 *
 * @author Jeffrey, Youhan
 */
public class Battleship implements Ship{

    @Override
    public String name() {
        return "Battleship";
    }

    @Override
    public int len() {
        return 4;
    }
} // end of class Battleship
