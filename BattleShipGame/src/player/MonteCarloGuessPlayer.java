package player;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import world.World;

/**
 * Monte Carlo guess player (task C).
 * Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class MonteCarloGuessPlayer  implements Player{

	private Map<Guess,Integer> destroyer = new HashMap<Guess,Integer>(); //store
	private Map<Guess,Integer> cruiser = new HashMap<Guess,Integer>();
	private Map<Guess,Integer> submarine = new HashMap<Guess,Integer>();
	private Map<Guess,Integer> battleship = new HashMap<Guess,Integer>();
	private Map<Guess,Integer> aircraft = new HashMap<Guess,Integer>();
	
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

} // end of class MonteCarloGuessPlayer
