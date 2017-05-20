package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import world.World;
import ship.Ship;
import world.World.Coordinate;
/**
 * Greedy guess player (task B).
 * Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class GreedyGuessPlayer  implements Player{
	private World world;
	private ArrayList<Guess> parity = new ArrayList<Guess>(); //hard code for the parity pattern
	private ArrayList<Integer> indexArray = new ArrayList<Integer>(); // track for the random Index
	private Stack<Guess> hit = new Stack<Guess>(); // register for every single hit cell
	private ArrayList<Guess> allCell = new ArrayList<Guess>(); //tracking for every single cell
	private final int[] directions = {-1,1,-1,1};
	private ArrayList<Guess> adjCell = new ArrayList<Guess>(); // store adjacent cell
//	private ArrayList<Integer> indexAdjCell = new ArrayList<Integer>();
	private int adjCount = 0;
	private Map<Ship,Boolean> trackShipSunk = new HashMap<Ship,Boolean>();
	private ArrayList<Boolean> countTrue = new ArrayList<Boolean>();
	@Override
    public void initialisePlayer(World world) {
        // To be implemented.
    	this.world=world;
    	
    	for(int i=0;i<world.numRow;i++){
    		int turnNum = i%2; //check if it is 0 or 1
    		for(int j=0;j<world.numColumn;j++){
    			if(j%2!= turnNum){ //determine if it is 0 or 1 
    				Guess guess = new Guess();
    				guess.row=i;
    				guess.column=j;
    				parity.add(guess);
    			}
    		}
    	}
    	
    	for(int i=0;i<world.shipLocations.size();i++){
    		trackShipSunk.put(world.shipLocations.get(i).ship, false);
//    		countingShipSunk.put(world.shipLocations.get(i).ship, 0);
    	}
    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.
    	Answer answer = new Answer();
    	Ship ship = null;
    	answer.isHit=false;
    	answer.shipSunk=null;
    	int row = guess.row;
    	int col = guess.column;
    	for(int i=0; i<world.shipLocations.size();i++){
    		
    		for(int j=0; j<world.shipLocations.get(i).coordinates.size();j++){
    			if(world.shipLocations.get(i).coordinates.get(j).row==row && world.shipLocations.get(i).coordinates.get(j).column==col){
    				
    				
    				answer.isHit=true;
    				ship = world.shipLocations.get(i).ship;
//    				System.out.println("Ship class" + ship.getClass());
    				break;
    			}
    		}
    	}

    	if(ship!=null){
    		for(int i=0; i<world.shipLocations.size();i++){
        		
        		if(ship.equals(world.shipLocations.get(i).ship)){
        			
        			int count=0;
        			int size = world.shipLocations.get(i).coordinates.size();
        			
        			for(int j=0; j<size;j++){
        				for(int k=0; k<world.shots.size();k++){
        					if(world.shots.get(k).row==world.shipLocations.get(i).coordinates.get(j).row && world.shots.get(k).column==world.shipLocations.get(i).coordinates.get(j).column ){
        						count++;
        					System.out.println("Find it !! Row " + world.shots.get(k).row + " column : " + world.shots.get(k).column  );
        					}
        				}
        			}

        			System.out.println("Ship : " + ship + " has count " + count);
        			if(count==size){
        				answer.shipSunk=ship;
        				System.out.println("Ship : " + ship + "is sunk");
        				trackShipSunk.put(ship, true);
        			}
        			
        			
        		}
    		}	
    	}
    	
    	
//			 	System.out.println("Player : adjcent cell is empty");
			 	
        // dummy return
        return answer;
    } // end of getAnswer()


    @Override
    public Guess makeGuess() {
        // To be implemented.
    	Guess g = new Guess();
    	Random r = new Random();
    	
    	if(hit.isEmpty() && adjCell.isEmpty()){
    		 System.out.println("Player is in the isEmpty() State!! ");
        	boolean same = false;
        	
        	int index =0;
        	do{
        		 index = r.nextInt(parity.size());
        		for(Integer i : indexArray){
        			if(i==index){
        				same=true;
        				break;
        			}
        			else same=false;
        		}
        		if(same==false){
        			indexArray.add(index);
        			for(int i=0;i<parity.size();i++){
        				if(index==i){
        					g.row = parity.get(i).row;
        					g.column = parity.get(i).column;
        					for(int z =0;z<allCell.size();z++){
        						if(allCell.get(z).row==g.row && allCell.get(z).column==g.column){
        							same = true;
        							break;
        						}
        						else same=false;
        					}
        			
        		}
        	}
        		}
        	}while(same);
        	
        	allCell.add(g); //add the cell that has no repeat to the list
    	}
    	else{  //if in the stack , it contains the hit cell
//    		int direction = r.nextInt(directions.length); //get the index randomly
    		
    		 System.out.println("Player is in the Not Empty State  ");
    		 
    		if(adjCell.isEmpty()){
    			 System.out.println("Player starts to Create adjcent Cell ");
    			boolean same = false;
    			for(int i =0;i<directions.length;i++){
    				Guess guess = new Guess();
    				if(i==0 || i==1){
    					guess.row = hit.firstElement().row + directions[i];
    					guess.column = hit.firstElement().column;
    				}
    				else if(i==2 || i==3){
    					guess.row = hit.firstElement().row;
    					guess.column = hit.firstElement().column + directions[i];
    				}
    				 System.out.println("Player choose " + guess.row + " " + guess.column);
    				 if(guess.row < 0 || guess.row>=world.numRow || guess.column <0 || guess.column >=world.numColumn || (hit.firstElement().row==guess.row && hit.firstElement().column==guess.column)){
    					 continue;
    				 }
    				 else{
    					
    						 for(int j=0;j<allCell.size();j++){
    							if(allCell.get(j).row==guess.row && allCell.get(j).column==guess.column){
    								same = true;
    								break;
    								
    							}
    							else{
    								same = false;
    								
    							}
    						}
    						 countTrue.add(same);
    						 if(!same) adjCell.add(guess);
    						
    						
    					 
    				 }
    				 
    			}
    			
    			
    			for(Guess gues : adjCell){
    				System.out.println(gues.row + " ---- " + gues.column);
    			}
    			
    		}
    					
//			if(adjCount==adjCell.size()){
//				adjCount =0;
//				adjCell.clear();
//			}
//			else {
    		if(!adjCell.isEmpty()){
    			boolean duplicate = false;
				do{
					System.out.println("AdjCount : " + adjCount);
					g.row = adjCell.get(adjCount).row;
					g.column = adjCell.get(adjCount).column;
					
					for(int i=0;i<allCell.size();i++){
						if(allCell.get(i).row==g.row && allCell.get(i).column==g.column){
							duplicate = true;
							break;
						}
						else{
							duplicate = false;
						}
					}
					if(adjCount==adjCell.size()){
						System.out.println("IN the hit not empty , it has count which is equalt to size");
					}
					else
						adjCount++;
						
				}while(duplicate);
				
				
				allCell.add(g);
    		}
    		else{
    			boolean same = false;
            	
            	int index =0;
            	do{
            		 index = r.nextInt(parity.size());
            		for(Integer i : indexArray){
            			if(i==index){
            				same=true;
            				break;
            			}
            			else same=false;
            		}
            		if(same==false){
            			indexArray.add(index);
            			for(int i=0;i<parity.size();i++){
                    		if(index==i){
                    			g.row = parity.get(i).row;
                    			g.column = parity.get(i).column;
                    			for(int z =0;z<allCell.size();z++){
                    				if(allCell.get(z).row==g.row && allCell.get(z).column==g.column){
                    					same = true;
                    					break;
                    				}
                    				else same=false;
                    			}
                    			
                    		}
                    	}
            			
            		}
            	}while(same);
            	
            	allCell.add(g); //add the cell that has no repeat to the list
    		}
				
				
//			}
			
			
			
    		
    	}
    	
    	
        // dummy return
        return g;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    	if(answer.isHit){
    		hit.push(guess); //if fire it on the ship, add to the list
    	}
    	
    	if(adjCount==adjCell.size()&& adjCell.size()!=0){
			adjCount =0;
			adjCell.clear();
			hit.remove(0);
		}
    	
    	if(!countTrue.isEmpty()){
    		int c = 0;
			for(Boolean b : countTrue){
				if(b){
					c++;
				}
			}
			if(c==countTrue.size()) hit.remove(0);
		    countTrue.clear();
    	}
    	 
//    	System.out.println("In the update state : " + answer.shipSunk);
//    	if(answer.shipSunk!=null){
//    		if(!hit.isEmpty()){
//    			
//    				hit.clear();
//    			
//    		}
//    	}
//    	if(!hit.isEmpty()){
//	 		if(adjCell.isEmpty()){
//	 			hit.pop();
//	 		}
//	 	}
    	
//    	if(adjCell.isEmpty()) hit.clear();
    } // end of update()


    @Override
    public boolean noRemainingShips() {
        // To be implemented.
    	int count = 0;
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
        // dummy return
        
    } // end of noRemainingShips()

  
} // end of class GreedyGuessPlayer
