package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import world.World;
import ship.Ship;
/**
 * Random guess player (task A).
 * Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class RandomGuessPlayer implements Player{
    
	private World world;
	private ArrayList<Guess> guess = new ArrayList<Guess>();//keep track of guess 
//	private ArrayList<Guess> hit = new ArrayList<Guess>(); keep track of hitting on the ship
	private Map<Ship,Boolean> trackShipSunk = new HashMap<Ship,Boolean>();
	private Map<Ship,Integer> countingShipSunk = new HashMap<Ship,Integer>(); //each ship has how many already hit 
    @Override
    public void initialisePlayer(World world) {
        // To be implemented.
    	this.world = world;
    	for(int i=0;i<world.shipLocations.size();i++){
    		trackShipSunk.put(world.shipLocations.get(i).ship, false);
    		countingShipSunk.put(world.shipLocations.get(i).ship, 0);
    	}
    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.   
    	Answer answer = new Answer();
    	answer.isHit = false;
    	answer.shipSunk = null;
//    	hit.add(guess);
    	int rowValue = guess.row;
    	int colValue = guess.column;
    	Ship ship = null;
    	
    	for(int i=0;i<world.shipLocations.size();i++){
    		for(int j=0; j<world.shipLocations.get(i).coordinates.size();j++){
    			if(rowValue==world.shipLocations.get(i).coordinates.get(j).row && colValue==world.shipLocations.get(i).coordinates.get(j).column){
        			answer.isHit = true;
        			int count = 0;
        			System.out.println("In the answer function , and the guess is equal to the part of ships");
        			for(Map.Entry<Ship, Integer> entry : countingShipSunk.entrySet()){
        				if(entry.getKey()==world.shipLocations.get(i).ship){
        					
        					count = entry.getValue();
        					System.out.println("In the countingShipSunk map , it is equal to ship, Count is equal to " + count) ;
        					countingShipSunk.put(world.shipLocations.get(i).ship, ++count);
        					break;
        				}
        			}
        			ship = world.shipLocations.get(i).ship;
        			if(world.shipLocations.get(i).coordinates.size()==count){
        				answer.shipSunk = ship;
        				trackShipSunk.put(answer.shipSunk, true);
        			}
        			
        			System.out.println("outer loop count is "+ count + " ship " + ship);
        			break;
        		}
    		}	
    	}
    	
    	
    	
//    		for(int i=0;i<world.shipLocations.size();i++){
//    			if(ship==world.shipLocations.get(i)){
//    				for(Map.Entry<Ship, Integer> entry : countingShipSunk.entrySet()){
//        				if(entry.getKey()==ship){
//        					 if(entry.getValue()==world.shipLocations.get(i).coordinates.size()){
//        						 answer.shipSunk= ship;
//        						 trackShipSunk.put(answer.shipSunk,true);
//        						 break;
//        					 }
//        						 
//        				}
//        			}
//    			}
    			
//    			if(ship==world.shipLocations.get(i).ship){
//    				int count = 0;
//            		int length = world.shipLocations.get(i).coordinates.size();
//            		for(int j=0; j<length;j++){
//            			if(world.shots.contains(world.shipLocations.get(i).coordinates.get(j))){
//            				count++;
//            			}
//            		}
//            		
//            		if(count==length){
//        				answer.shipSunk = world.shipLocations.get(i).ship;
//        				trackShipSunk.put(answer.shipSunk, true);
//        				
//        				break;
//        			}
//    			}
        		
        	
    	
    	
    	
        // dummy return
        return answer;
    } // end of getAnswer()


    
    @Override
    public Guess makeGuess() {
        // To be implemented.
    	Guess g = new Guess();
    	Random row = new Random();
    	Random col = new Random();
    	
    	
       
//        boolean noDuplicate = true;
        boolean same = false;
        int rowValue;
        int colValue;
        do{
        	rowValue = row.nextInt(world.numRow);
            colValue = col.nextInt(world.numColumn);
            g.row=rowValue;
            g.column=colValue;
            for(Guess e : guess){
            	if(e.row==g.row && e.column ==g.column){
            		same = true;
            		break;
            	}
            	else same=false;
            }
        }while(same);
        	guess.add(g);

        return g;
        
        
        // dummy return
        
    } // end of makeGuess()

    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    	
    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.
    	int count = 0;
//    	for (Map.Entry<Ship, Boolean> entry : trackShipSunk.entrySet()) {
//    	    
//    	    if(entry.getValue().equals(true)){
//    	    	count++;
//    	    }
//    	}
    	for(Boolean b : trackShipSunk.values()){
    		if(b){
    			count++;
    			System.out.println(count);
    		}
    	}
    	
    	if(count==world.shipLocations.size()){
    		System.out.println("In the NoRemaingShip function , count equals to ship location count : " + count);
    		return true;
    	}
    	else return false;
//    	boolean test = false;
//    	for(int i =0; i<world.shipLocations.size();i++){
//    		
//    		for(int j=0;j<world.shipLocations.get(i).coordinates.size();j++){
//    			Guess g = new Guess();
//    			g.row = world.shipLocations.get(i).coordinates.get(j).row;
//    			g.column = world.shipLocations.get(i).coordinates.get(j).column;
//    			if(guess.contains(g)){
//    				test = true;
//    			}
//    			else test =false;
//    		}
//    	}
//    	if(test) return true;
//    	else return false;
    	
        // dummy return
        
    } // end of noRemainingShips()
   
} // end of class RandomGuessPlayer
