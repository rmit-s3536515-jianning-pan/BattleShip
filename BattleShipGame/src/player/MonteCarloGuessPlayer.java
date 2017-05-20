package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import world.World;
import ship.*;
/**
 * Monte Carlo guess player (task C).
 * Please implement this class.
 *
 * @author Youhan, Jeffrey
 */
public class MonteCarloGuessPlayer  implements Player{

	private World world;
	private Map<Guess,Integer> destroyer = new HashMap<Guess,Integer>(); //
	private Map<Guess,Integer> cruiser = new HashMap<Guess,Integer>();
	private Map<Guess,Integer> submarine = new HashMap<Guess,Integer>();
	private Map<Guess,Integer> battleship = new HashMap<Guess,Integer>();
	private Map<Guess,Integer> aircraft = new HashMap<Guess,Integer>();
	
	private ArrayList<Guess> parity = new ArrayList<Guess>(); //hard code for the parity pattern
	private ArrayList<Integer> indexArray = new ArrayList<Integer>(); // track for the random Index
	private Stack<Guess> hit = new Stack<Guess>(); // register for every single hit cell
	private ArrayList<Guess> allCell = new ArrayList<Guess>(); //tracking for every single cell
	private Map<Ship,Boolean> trackShipSunk = new HashMap<Ship,Boolean>();
	private Map<Ship,Integer> countInit = new HashMap<Ship,Integer>();
	private final int[] directions = {-1,1,-1,1};
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
    	}
    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.	
    	Answer answer = new Answer();
    	answer.isHit = false;
    	answer.shipSunk = null;
    	Ship ship = null;
    	int row = guess.row;
    	int col = guess.column;
    	for(int i=0; i<world.shipLocations.size();i++){
    		
    		for(int j=0; j<world.shipLocations.get(i).coordinates.size();j++){
    			if(world.shipLocations.get(i).coordinates.get(j).row==row && world.shipLocations.get(i).coordinates.get(j).column==col){
    				
    				
    				answer.isHit=true;
    				ship = world.shipLocations.get(i).ship;
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
        // dummy return
        return answer;
    } // end of getAnswer()


    @Override
    public Guess makeGuess() {
        // To be implemented.
    	Guess g = new Guess();
    	Random r = new Random();
    	
    	if(hit.isEmpty()){
    		
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
         	
         	
         	
         	// below code , is trying to identify the placement configurations of each cell
         	for(int i=0;i<world.shipLocations.size();i++){ // it has 5 ships
         		int size = world.shipLocations.get(i).coordinates.size(); //size is according to the ship
         		Ship ship = world.shipLocations.get(i).ship;
         		int count = directions.length;
         		
         		for(int k=0;k<directions.length;k++){
     				Guess guess = new Guess();
     				if(k==0 || k==1){
     					guess.row = g.row + ((directions[k])*(size-1));
    					guess.column = g.column;
     				}
     				else if(k==2 || k==3){
     					guess.row = g.row;
    					guess.column = g.column + ((directions[k])*(size-1));
     				}
     				
     				 if(guess.row < 0 || guess.row>=world.numRow || guess.column <0 || guess.column >=world.numColumn){
     					 count--;
     					 continue;
     				 }
     				
     				 
         		}//end loop k
         		
         		countInit.put(ship, count);
         	}
         	
         	
         	//try to get how many placement for each cell 
         	
         	for(int i=0;i<world.shipLocations.size();i++){ // it has 5 ships
         		int size = world.shipLocations.get(i).coordinates.size(); //size is according to the ship
         		Ship ship = world.shipLocations.get(i).ship;
         		
         		for(int j=0;j<size;j++){
         			 int count = directions.length; // 4 count
         		int overflow = directions.length;// Initial value is 4 , it is only for initial count value is 3 
         			for(int k=0;k<directions.length;k++){
         				Guess guess = new Guess();
         				if(k==0 || k==1){
         					guess.row = g.row + ((directions[k])*j);
        					guess.column = g.column;
         				}
         				else if(k==2 || k==3){
         					guess.row = g.row;
        					guess.column = g.column + ((directions[k])*j);
         				}
         				
         				 if(guess.row < 0 || guess.row>=world.numRow || guess.column <0 || guess.column >=world.numColumn){
         					 continue;
         				 }
         				
         				boolean b = false; 
         				for(Guess e : allCell){ //check if guess is duplicate or not 
         					if(e.row==guess.row && e.column==guess.column){
         						b = true;
         						break;
         					}
         					else{
         						b=false;
         					}
         				} 
         				System.out.println("i value is " + i + " " + "k index value is " + k + " " + "Guess value - row: " + guess.row + "col : "+ guess.column);
         				
         				if(!b){ //if it has no repeat cell in the overall 
         					
         					if(guess.row==g.row&&guess.column==g.column){
         						allCell.add(g); // above code , find the parity cell randomly 
         					}
         					if(size==2 || size==3 || size==4 || size==5){ //
         						for(Map.Entry<Ship, Integer> entry : countInit.entrySet()){
         			         		
         							if(ship==entry.getKey()){
         								System.out.println("Ship : " + entry.getKey() + " init Count : " + entry.getValue());
         			         			int v = entry.getValue();
         			         			int placement = 0;
         			         			if(v==2){
         			         				if(guess.row==g.row && guess.column==g.column){//if this cell is where the guess is 
         			         					placement = 2;
         			         				}
         			         				else
         			         					placement = 1;
         			         			}
         			         			else if(v==3){
         			         				if(guess.row==g.row && guess.column==g.column){//if this cell is where the guess is 
         			         					placement = v + (size-2);
         			         				}
         			         				
         			         				else{
         			         					int w = 0;
         			         					int h = 0;
         			         					
         			         					if(k==0){
         			         						 w = guess.row + directions[k+1]*(overflow+1);
         			         					 	h = guess.column;
         			         					}
         			         					else if(k==1){
         			         						w = guess.row + directions[k-1]*(overflow+1);
         			         						h = guess.column;
         			         					}
         			         					else if(k==2){
         			         						w = guess.row;
         			         						h = guess.column + directions[k+1]*(overflow+1);
         			         					}
         			         					else if(k==3){
         			         						w = guess.row;
         			         						h = guess.column + directions[k-1]*(overflow+1);
         			         					}
         			         					
         			         					if(w<0 || w>=world.numRow || h <0 || h>=world.numColumn){
         			         						placement = 1;
         			         					}
         			         					else{
         			         						placement = count - j;
         			         					}
         			         					
         			         				}
         			         			}
         			         			else if(v==4){
         			         				if(guess.row==g.row && guess.column==g.column){//if this cell is where the guess is 
         			         					placement = v + (2*(size-2));
         			         				}
         			         				else{
         			         					placement = count - j;
         			         				}
         			         			}
         			         			else{
         			         				System.out.println("get placement for each cell has a exception!!!!");
         			         			}
         			         			
         			         			if(ship instanceof Destroyer){
         			         					destroyer.put(guess, placement);
         			         			}
         			         			else if(ship instanceof Battleship){
         			         				battleship.put(guess, placement);
         			         			}
         			         			else if(ship instanceof Cruiser){
         			         				cruiser.put(guess, placement);
         			         			}
         			         			else if(ship instanceof Submarine){
         			         				submarine.put(guess, placement);
         			         			}
         			         			else if(ship instanceof AircraftCarrier){
         			         				aircraft.put(guess, placement);
         			         			}
         			         			System.out.println("Placement : "+placement);
         			         		}//end if ship is equal to entry.getkey
         							
         			         		
         			         		
         			         	}
         					}
         				}// end !b
         				
         			}//end loop k
         			overflow++;
         		}//loop j
         	}//loop i
         
    	}// end of hit that is empty
    	else{ // if hit has something in it
    		int maxDestroyer = 0;
    		for(Map.Entry<Guess, Integer> entry : destroyer.entrySet()){
    			if(entry.getValue()>maxDestroyer){
    				maxDestroyer = entry.getValue();
    			}
    		}
    		int maxCuriser = 0;
    		for(Map.Entry<Guess, Integer> entry : cruiser.entrySet()){
    			if(entry.getValue()>maxCuriser){
    				maxCuriser = entry.getValue();
    			}
    		}
    		int maxBattleship = 0;
    		for(Map.Entry<Guess, Integer> entry : battleship.entrySet()){
    			if(entry.getValue()>maxBattleship){
    				maxBattleship = entry.getValue();
    			}
    		}
    		int maxAircraft = 0;
    		for(Map.Entry<Guess, Integer> entry : aircraft.entrySet()){
    			if(entry.getValue()>maxAircraft){
    				maxAircraft = entry.getValue();
    			}
    		}
    		int maxSubmarine = 0;
    		for(Map.Entry<Guess, Integer> entry : submarine.entrySet()){
    			if(entry.getValue()>maxSubmarine){
    				maxSubmarine = entry.getValue();
    			}
    		}
    		List<Integer> array = new ArrayList<Integer>();
    		array.add(maxDestroyer);
    		array.add(maxCuriser);
    		array.add(maxBattleship);
    		array.add(maxAircraft);
    		array.add(maxSubmarine);
    		
    		int max = array.get(0);
    		int index = 0;
    		for(int i=1;i<array.size();i++){
    			if(array.get(i) > max){
    				max = array.get(i);
    				index = i;
    			}
    		}
    		if(index==0){
    			for(Map.Entry<Guess, Integer> entry : destroyer.entrySet()){
        			if(entry.getValue()==max){
        				Guess e = entry.getKey();
        				g.row = e.row;
        				g.column = e.column;
        				break;
        			}
        		}
    		}
    		else if(index==1){
    			for(Map.Entry<Guess, Integer> entry : cruiser.entrySet()){
        			if(entry.getValue()==max){
        				Guess e = entry.getKey();
        				g.row = e.row;
        				g.column = e.column;
        				break;
        			}
        		}
    		}
    		else if(index==2){
    			for(Map.Entry<Guess, Integer> entry : battleship.entrySet()){
        			if(entry.getValue()==max){
        				Guess e = entry.getKey();
        				g.row = e.row;
        				g.column = e.column;
        				break;
        			}
        		}
    		}
    		else if(index==3){
    			for(Map.Entry<Guess, Integer> entry : aircraft.entrySet()){
        			if(entry.getValue()==max){
        				Guess e = entry.getKey();
        				g.row = e.row;
        				g.column = e.column;
        				break;
        			}
        		}
    		}
    		else if(index==4){
    			for(Map.Entry<Guess, Integer> entry : submarine.entrySet()){
        			if(entry.getValue()==max){
        				Guess e = entry.getKey();
        				g.row = e.row;
        				g.column = e.column;
        				break;
        			}
        		}
    		}
    		
    		
    		allCell.add(g);
    		
    	
    	}
        // dummy return
        return g;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    	if(answer.isHit){
    		
    		if(hit.isEmpty())
    			hit.push(guess);
    	}
    	if(!answer.isHit && hit.isEmpty()){
    		
    		destroyer.clear();
    		submarine.clear();
    		aircraft.clear();
    		battleship.clear();
    		cruiser.clear();
    		countInit.clear();
    	}
    	
    	if(!hit.isEmpty() && !answer.isHit){
//    		int countDestroyer = 0;
//    		int countBattleship = 0;
//    		int countCruiser = 0;
//    		int countSub = 0;
//    		int countAir = 0;
//    		for(Map.Entry<Ship, Integer> entry : countInit.entrySet()){
//    			if(entry.getKey() instanceof Destroyer){
//    				countDestroyer = entry.getValue();
//    			}
//    			else if(entry.getKey() instanceof Submarine){
//    				countSub = entry.getValue();
//    			}
//    			else if(entry.getKey() instanceof AircraftCarrier){
//    				countAir = entry.getValue();
//    			}
//    			else if(entry.getKey() instanceof Battleship){
//    				countBattleship = entry.getValue();
//    			}
//    			else if(entry.getKey() instanceof Cruiser){
//    				countCruiser = entry.getValue();
//    			}
//    		}
    		int sizeDestroyer = 0;
    		int sizeBattleship = 0;
    		int sizeSub = 0;
    		int sizeAir =0;
    		int sizeCruiser = 0;
    		
    		for(int i=0; i<world.shipLocations.size();i++){
    			if(world.shipLocations.get(i).ship instanceof Destroyer){
    				sizeDestroyer = world.shipLocations.get(i).coordinates.size();
    			}
    			else if(world.shipLocations.get(i).ship instanceof Submarine){
    				sizeSub = world.shipLocations.get(i).coordinates.size();
    			}
    			else if(world.shipLocations.get(i).ship instanceof AircraftCarrier){
    				sizeAir = world.shipLocations.get(i).coordinates.size();
    			}
    			else if(world.shipLocations.get(i).ship instanceof Battleship){
    				sizeBattleship = world.shipLocations.get(i).coordinates.size();
    			}
    			else if(world.shipLocations.get(i).ship instanceof Cruiser){
    				sizeCruiser = world.shipLocations.get(i).coordinates.size();
    			}
    			
    		}
    		int row = guess.row-hit.firstElement().row ;
    		int col = guess.column -  hit.firstElement().column;
    		if(row>0){
    			for(int i=0;i<sizeDestroyer-1;i++){
    				int r = guess.row + (i*row);
    				for(Map.Entry<Guess, Integer> entry : destroyer.entrySet()){
    					Guess g =entry.getKey();
    					if(g.row==r){
    						destroyer.put(g, 0);
    						break;
    					}
    				}
    			}
    			
    			for(int i=0;i<sizeBattleship-1;i++){
    				int r = guess.row + (i*row);
    				for(Map.Entry<Guess, Integer> entry : battleship.entrySet()){
    					Guess g =entry.getKey();
    					if(g.row==r){
    						battleship.put(g, 0);
    						break;
    					}
    				}
    			}
    			
    			for(int i=0;i<sizeAir-1;i++){
    				int r = guess.row + (i*row);
    				for(Map.Entry<Guess, Integer> entry : aircraft.entrySet()){
    					Guess g =entry.getKey();
    					if(g.row==r){
    						aircraft.put(g, 0);
    						break;
    					}
    				}
    			}
    			for(int i=0;i<sizeSub-1;i++){
    				int r = guess.row + (i*row);
    				for(Map.Entry<Guess, Integer> entry : submarine.entrySet()){
    					Guess g =entry.getKey();
    					if(g.row==r){
    						submarine.put(g, 0);
    						break;
    					}
    				}
    			}
    			for(int i=0;i<sizeCruiser-1;i++){
    				int r = guess.row + (i*row);
    				for(Map.Entry<Guess, Integer> entry : cruiser.entrySet()){
    					Guess g =entry.getKey();
    					if(g.row==r){
    						cruiser.put(g, 0);
    						break;
    					}
    				}
    			}
    		}
    		else if(col >0){
    			for(int i=0;i<sizeDestroyer-1;i++){
    				int c = guess.column + (i*col);
    				for(Map.Entry<Guess, Integer> entry : destroyer.entrySet()){
    					Guess g =entry.getKey();
    					if(g.column==c){
    						destroyer.put(g, 0);
    						break;
    					}
    				}
    			}
    			
    			for(int i=0;i<sizeBattleship-1;i++){
    				int c = guess.column + (i*col);
    				for(Map.Entry<Guess, Integer> entry : battleship.entrySet()){
    					Guess g =entry.getKey();
    					if(g.column==c){
    						battleship.put(g, 0);
    						break;
    					}
    				}
    			}
    			
    			for(int i=0;i<sizeAir-1;i++){
    				int c = guess.column + (i*col);
    				for(Map.Entry<Guess, Integer> entry : aircraft.entrySet()){
    					Guess g =entry.getKey();
    					if(g.column==c){
    						aircraft.put(g, 0);
    						break;
    					}
    				}
    			}
    			for(int i=0;i<sizeSub-1;i++){
    				int c = guess.column + (i*col);
    				for(Map.Entry<Guess, Integer> entry : submarine.entrySet()){
    					Guess g =entry.getKey();
    					if(g.column==c){
    						submarine.put(g, 0);
    						break;
    					}
    				}
    			}
    			for(int i=0;i<sizeCruiser-1;i++){
    				int c = guess.column + (i*col);
    				for(Map.Entry<Guess, Integer> entry : cruiser.entrySet()){
    					Guess g =entry.getKey();
    					if(g.column==c){
    						cruiser.put(g, 0);
    						break;
    					}
    				}
    			}
    		}
//    		int row = guess.row;
//    		int col = guess.column;
//    		for(Map.Entry<Guess, Integer> entry : destroyer.entrySet()){
//    			Guess g  = entry.getKey();
//    			if(g.row==row && g.column==col){
//    				
//    				destroyer.remove(g);
//    				break;
//    			}
//    		}
//    		
//    		for(Map.Entry<Guess, Integer> entry : submarine.entrySet()){
//    			Guess g  = entry.getKey();
//    			if(g.row==row && g.column==col){
//    				submarine.remove(g);
//    				break;
//    			}
//    		}
//    		
//    		for(Map.Entry<Guess, Integer> entry : aircraft.entrySet()){
//    			Guess g  = entry.getKey();
//    			if(g.row==row && g.column==col){
//    				aircraft.remove(g);
//    				break;
//    			}
//    		}
//    		
//    		for(Map.Entry<Guess, Integer> entry : battleship.entrySet()){
//    			Guess g  = entry.getKey();
//    			if(g.row==row && g.column==col){
//    				battleship.remove(g);
//    				break;
//    			}
//    		}
//    		
//    		for(Map.Entry<Guess, Integer> entry : cruiser.entrySet()){
//    			Guess g  = entry.getKey();
//    			if(g.row==row && g.column==col){
//    				cruiser.remove(g);
//    				break;
//    			}
//    		}
    	}
    	
    	if(!hit.isEmpty()){
    		int row = guess.row;
    		int col = guess.column;
    		for(Map.Entry<Guess, Integer> entry : destroyer.entrySet()){
    			Guess g  = entry.getKey();
    			if(g.row==row && g.column==col){
    				destroyer.remove(g);
    				break;
    			}
    		}
    		
    		for(Map.Entry<Guess, Integer> entry : submarine.entrySet()){
    			Guess g  = entry.getKey();
    			if(g.row==row && g.column==col){
    				submarine.remove(g);
    				break;
    			}
    		}
    		
    		for(Map.Entry<Guess, Integer> entry : aircraft.entrySet()){
    			Guess g  = entry.getKey();
    			if(g.row==row && g.column==col){
    				aircraft.remove(g);
    				break;
    			}
    		}
    		
    		for(Map.Entry<Guess, Integer> entry : battleship.entrySet()){
    			Guess g  = entry.getKey();
    			if(g.row==row && g.column==col){
    				battleship.remove(g);
    				break;
    			}
    		}
    		
    		for(Map.Entry<Guess, Integer> entry : cruiser.entrySet()){
    			Guess g  = entry.getKey();
    			if(g.row==row && g.column==col){
    				cruiser.remove(g);
    				break;
    			}
    		}
    	}
    	if(answer.shipSunk!=null){
    		hit.clear();
    		destroyer.clear();
    		submarine.clear();
    		aircraft.clear();
    		battleship.clear();
    		cruiser.clear();
    		countInit.clear();
    	}
//    	if(destroyer.isEmpty() || submarine.isEmpty() || aircraft.isEmpty() || battleship.isEmpty() || cruiser.isEmpty()){
//    		hit.clear();
//    	}
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

} // end of class MonteCarloGuessPlayer
