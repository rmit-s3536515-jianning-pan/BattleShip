package player;

import java.util.Scanner;
import world.World;

/**
 * Player (handle hex) for bonus task.
 * Only implement this if attempting the bonus task, otherwise can leave alone.
 *
 * @author Youhan, Jeffrey
 */
public class BonusPlayer  implements Player{

    @Override
    public void initialisePlayer(World world) {
        // To be implemented.
    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.

        // dummy return
        return null;
    } // end of getAnswer()


    @Override
    public Guess makeGuess() {
        // To be implemented.

        // dummy return
        return null;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.

        // dummy return
        return true;
    } // end of noRemainingShips()

} // end of class BonusPlayer
