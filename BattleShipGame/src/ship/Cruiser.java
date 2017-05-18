package ship;

/**
 * Cruiser.
 *
 * @author Jeffrey, Youhan
 */
public class Cruiser implements Ship{

    @Override
    public String name() {
        return "Cruiser";
    }

    @Override
    public int len() {
        return 3;
    }
} // end of class Cruiser
