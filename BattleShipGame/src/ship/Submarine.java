package ship;

/**
 * Submarine.
 *
 * @author Jeffrey, Youhan
 */
public class Submarine implements Ship{

    @Override
    public String name() {
        return "Submarine";
    }

    @Override
    public int len() {
        return 3;
    }
} // end of class Submarine
