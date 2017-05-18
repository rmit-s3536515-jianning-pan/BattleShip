package ship;

/**
 * Aircraft carrier.
 *
 * @author Jeffrey, Youhan
 */
public class AircraftCarrier implements Ship{

    @Override
    public String name() {
        return "AircraftCarrier";
    }

    @Override
    public int len() {
        return 5;
    }
} // end of class AircraftCarrier
