package ship;

/**
 * Destroyer.
 *
 * @author Jeffrey, Youhan
 */
public class Destroyer implements Ship{

    @Override
    public String name() {
        return "Destroyer";
    }

    @Override
    public int len() {
        return 2;
    }
} // end of class Destroyer
