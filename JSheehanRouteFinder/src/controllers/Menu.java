package controllers;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import application.Main;
// Menu class which operates using user input.
public class Menu {

	private Search searcher;
	private Scanner input;
	
	public Menu() {
		input = new Scanner(System.in);
		searcher = new Search();
	}
	
//	Method for displaying the console based menu.
	public void mainMenu() {
		System.out.println("====== Main Menu =======");
		System.out.println("1)	List all Landmarks");
		System.out.println("2) 	List all Links.");
		System.out.println("3) 	Add Landmark.");
		System.out.println("4) 	Add Link.");
		System.out.println("5) 	List all Routes Between two Landmarks.");
		System.out.println("6) 	Generate Shortest Route Between two Landmarks.");
		System.out.println("7) 	Generate all Routes Between two Landmarks Under a Distance Limit.");
		System.out.println("0) 	Exit & Save.");
	}
	
//	runs the mainMenu method and loops until the loser decides to quit.
	public void runMenu() {
		mainMenu();
		System.out.println("Press Enter to Begin.....");
		input.nextLine();
		do {
			System.out.println();
			System.out.println("Choose an option");
			boolean check = true;
			int option = 10;
//			Asks for the user input and loops until the user provides acceptable input.
				try{
					option = input.nextInt();
				}
				catch(InputMismatchException exception){
					System.out.println("Incorrect input, only Integers are accepted.");
				}

//			Line Must be done to clear earlier input from scanner.
			input.nextLine();
			switch(option) {
			case 1:
				listLandmarks();
				break;
			case 2:
				listLinks();
				break;
			case 3:
				addLandmark();
				break;
			case 4:
				addLink();
				break;
			case 5:
				allRoutes();
				break;
			case 6:
				shortestRoute();
				break;
			case 7:
				timeLimit();
				break;
			case 0:
//				Automatically saves the map when the user chooses to exit.
				HandleXML.saveMap();
				System.exit(0);
			default:
				System.out.println("Invalid Input.");
				break;
			}
			mainMenu();
		}while(true);
	}
	

//	Prints all links to the console
	private void listLinks() {
		System.out.println("====== List of Links ======");
		System.out.println();
		for(int i = 1; i<=Main.landmarksMap.size();i++) {
			System.out.println(i + ")  " + " Links From " + Main.landmarksMap.get(i).data + " :");
			System.out.println("---------------------------------------------------");
			for(int j=0; j<Main.landmarksMap.get(i).neighbours.size();j++) {
				System.out.println("    Link " + (j+1) + ")");
				System.out.println("      Destination	: " + Main.landmarksMap.get(i).neighbours.get(j).landmarkTwo.data);
				System.out.println("      Time		: " + conversion(Main.landmarksMap.get(i).neighbours.get(j).distance) + " minutes.");
				System.out.println("      Distance		: " + Main.landmarksMap.get(i).neighbours.get(j).distance + " meters.");
				System.out.println("      Streets Linking	: " + Main.landmarksMap.get(i).neighbours.get(j).streets);
			}
			System.out.println();
			
		}
		System.out.println();
		
	}

//	Prints all landmarks to the console
	private void listLandmarks() {
		System.out.println("====== List of Landmarks ======");
		System.out.println();
		for(int i = 1; i<=Main.landmarksMap.size();i++) {
			System.out.println("id = " + i + ")");
			System.out.println("	Landmark Name = "+ Main.landmarksMap.get(i).data);
			System.out.println();
		}
		System.out.println();
		
	}

//	Allows the user to add their own landmark.
	public void addLandmark() {
		System.out.println("Name Landmark.");
//		Asks the user to choose a name for the landmark.
		String name = input.nextLine();
		System.out.println(name);
	
		Landmark<?> landmark =  new Landmark<>(name);
//		Adds the new landmark to the landmarksMap.
		Main.landmarksMap.put(landmark.id, landmark);
	}
	
//	Allows the user to add their own link between two landmarks.
	public void addLink() {
		System.out.println("====== List of Landmarks ======");
		System.out.println();
//		Prints all landmarks to the console so the user knows which landmarks to choose for the link.
		for(int i = 1; i<=Main.landmarksMap.size();i++) {
			System.out.println("id = " + i + ")");
			System.out.println("	Landmark Name = "+ Main.landmarksMap.get(i).data);
			System.out.println();
		}
		
//		Asks for user input for the link.
//		Integer variables for holding start and end point ids.
		int landmarkOneId = 0;
		int landmarkTwoId = 0;
		
//		boolean used for looping until input is accepted.
		boolean inputError = true;
//		loops until user provides an acceptable starting point.
		do {
	        try {
	        	System.out.println("Choose First Town for Link(by Id).");
	    		landmarkOneId = input.nextInt();
	        	inputError = false;
//	        	Ensures a landmark exists for the given Id.
	        	if(landmarkOneId <1 || landmarkOneId> Landmark.counter) {
	        		inputError = true;
	        		System.out.println("Id entered is not assigned to a Landmark.");
	        	}
	        } 
//	        Catches if input is not an integer.
	        catch (InputMismatchException inp) {
	            System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
	        }
	    } while (inputError);
		
		inputError = true;
//		loops until user provides an acceptable ending point.
		do {
	        try {
	        	System.out.println("Choose Second Town for Link(by Id).");
	    		landmarkTwoId = input.nextInt();
	        	inputError = false;
//	        	Ensures a landmark exists for the given Id.
	        	if(landmarkTwoId <1 || landmarkTwoId > Landmark.counter) {
	        		inputError = true;
	        		System.out.println("Id entered is not assigned to a Landmark.");
	        	}
	        	if(landmarkOneId == landmarkTwoId) {
	        		inputError = true;
	        		System.out.println("Second Town is the same as the first, please choose again.");
	        	}
	        } 
//	        Catches if input is not an integer.
	        catch (InputMismatchException inp) {
	            System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
	        }
	    } while (inputError);
		
		input.nextLine();
		String streets = "";
		int duration = 0;
		boolean done = false;
		while(!done) {
			System.out.println("Name Street Linking Landmarks");
			String s = input.nextLine();
			streets+= s;
			String more = "";
			
			while(true) {
//				Allows the user to define whether there are any more streets in the link.
				System.out.println("Are there more streets to add(y/n)");
				 more = input.nextLine();
				 
//				 input.next();
					if(more.equals("y")) {
						streets+= ", ";
						break;
					}
					else if (more.equals("n")){
						done = true;
						
						break;
					}
					else{
//						Ensures input is correct.
						System.out.println("Only 'y' & 'n' are accepted inputs.");
					}
			}
		}
		inputError = true;
		do {
	        try {
	        	System.out.println("Input Time of Journey in minutes as an integer.");
	        	duration = input.nextInt();
	        	inputError = false;
	        	if(duration < 1) {
	        		inputError = true;
	        		System.out.println("Please input a time greater than 1 minute.");
	        	}
	        } 
	        catch (InputMismatchException inp) {
	            System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
	        }
	    } while (inputError);
		
//		Finally links the two landmarks with the data provided by the user.
		Main.landmarksMap.get(landmarkOneId).connectToNeighbour(
				Main.landmarksMap.get(landmarkTwoId), reverseConversion(duration), streets);
		
	}

/*
 * Allows the user to find the shortest route between two landmarks.
 */
	public void shortestRoute() {
//		Prints list of landmarks to the console so the user knows the ID's to input.
		System.out.println("====== List of Landmarks ======");
		System.out.println();
		for(int i = 1; i<=Main.landmarksMap.size();i++) {
			System.out.println("id = " + i + ")");
			System.out.println("	Landmark Name = "+ Main.landmarksMap.get(i).data);
			System.out.println();
		}
		
//		Asks the user to input Id for the starting point, with exception handling.
		int landmarkOneId = 0;
		boolean inputError = true;
		do {
	        try {
	        	System.out.println("Choose Starting Point for Search(by Id).");
	        	landmarkOneId = input.nextInt();
	        	inputError = false;
	        	if(landmarkOneId <1 || landmarkOneId > Landmark.counter) {
	        		inputError = true;
	        		System.out.println("Id entered is not assigned to a Landmark.");
	        	}
	        } 
	        catch (InputMismatchException inp) {
	            System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
	        }
	    } while (inputError);
		
//		Asks the user to input Id for the end point, and provides exception handling.
		inputError = true;
		int landmarkTwoId = 0;
		do {
	        try {
	        	System.out.println("Choose End Point for Search(by Id).");
	        	landmarkTwoId = input.nextInt();
	        	inputError = false;
	        	if(landmarkTwoId <1 || landmarkTwoId > Landmark.counter) {
	        		inputError = true;
	        		System.out.println("Id entered is not assigned to a Landmark.");
	        	}
	        	else if(landmarkOneId == landmarkTwoId) {
	        		inputError = true;
	        		System.out.println("Please choose a different Id for the end Point to the starting point.");
	        	}
	        } 
	        catch (InputMismatchException inp) {
	            System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
	        }
	    } while (inputError);
		
//		Finds the shortest route by using the findShortestPath method from the searcher class and prints details to the console.
		Landmark<?> start = Main.landmarksMap.get(landmarkOneId);
		Landmark<?> end = Main.landmarksMap.get(landmarkTwoId);
		System.out.println();
		
		CostedPath shortest  = searcher.findShortestPath(start, end.data);
		
		if(shortest == null) {
			System.out.println("No shortest route linking two landmarks.");
		}
		else {
			System.out.println("Shortest Route");
			System.out.println("--------------------------------------------");
			System.out.println("	Route  contains " + shortest.pathList.size()
					+ " Landmarks.");
			System.out.println();
			for(int j=0; j<shortest.pathList.size(); j++) {
				System.out.println("	Landmark " + (j+1) + ": " + shortest.pathList.get(j).data);
			}
			System.out.println("--------------------------------------------");
			System.out.println("	Total Time : " + conversion(shortest.pathCost) + " minutes");
			System.out.println();
		}
	}

	/*
	 * Lists all the routes between two landmarks.
	 */
	private void allRoutes() {
//		Lists all landmarks.
		System.out.println("====== List of Landmarks ======");
		System.out.println();
		for(int i = 1; i<=Main.landmarksMap.size();i++) {
			System.out.println("id = " + i + ")");
			System.out.println("	Landmark Name = "+ Main.landmarksMap.get(i).data);
			System.out.println();
		}
		
//		Asks the user to input Id for the starting point, with exception handling.
		int landmarkOne = 0;
		boolean inputError = true;
		do {
	        try {
	        	System.out.println("Choose Starting Point for Search(by Id).");
	        	landmarkOne = input.nextInt();
	        	inputError = false;
	        	if(landmarkOne <1 || landmarkOne > Landmark.counter) {
	        		inputError = true;
	        		System.out.println("Id entered is not assigned to a Landmark.");
	        	}
	        } 
	        catch (InputMismatchException inp) {
	            System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
	        }
	    } while (inputError);
		
//		Asks the user to input Id for the ending point, with exception handling.
		inputError = true;
		int landmarkTwo = 0;
		do {
	        try {
	        	System.out.println("Choose End Point for Search(by Id).");
	        	landmarkTwo = input.nextInt();
	        	inputError = false;
	        	if(landmarkTwo <1 || landmarkTwo > Landmark.counter) {
	        		inputError = true;
	        		System.out.println("Id entered is not assigned to a Landmark.");
	        	}
	        	else if(landmarkOne == landmarkTwo) {
	        		inputError = true;
	        		System.out.println("Please choose a different Id for the end Point to the starting point.");
	        	}
	        } 
	        catch (InputMismatchException inp) {
	            System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
	        }
	    } while (inputError);
		
		
//		Gets the landmarks for the users chosen start and end ID's and runs the findAllRoutes method from the searcher class.
		Landmark<?> start = Main.landmarksMap.get(landmarkOne);
		Landmark<?> end = Main.landmarksMap.get(landmarkTwo);
		List<CostedPath> result = searcher.findAllRoutes(start, null, end.data, 0);
		
		if(result.size() == 0) {
			System.out.println("No route linking two landmarks.");
		}
		else {
//			Prints the data for all Costed Paths linking the start and point nodes.
			for(int i =0; i<result.size();i++) {
				CostedPath costPath = result.get(i);
				System.out.println("Route " + (i+1));
				System.out.println("	Route "+ (i+1) + " contains " + result.get(i).pathList.size() 
						+ " Landmarks.");
				for(int j =0; j<result.get(i).pathList.size() -1;j++) {
					;
					System.out.println("");
					System.out.println("    Landmark " + (j+1) + " :    " + costPath.pathList.get(j).data);
						for(Link link : costPath.pathList.get(j).neighbours) {
							if(link.landmarkTwo.data.equals(costPath.pathList.get(j+1).data)) {
								System.out.println("    Time to next Landmark:  " + conversion(link.distance) + " minutes.");
								System.out.println("--------------------------------------------------------------");
								break;
							
						}
					}
						
				}
				System.out.println();
				System.out.println("    Landmark " + costPath.pathList.size() + " :    " + costPath.pathList.get(costPath.pathList.size() -1).data);
				System.out.println();
				System.out.println("=================================================================");
				
	/*
	 * 			Note Total journey time may differ to sum of time between individual landmarks due
	 * 			to the conversion from distance to time rounding to the nearest minute.			
	 */
				System.out.println("    Total Journey Time:    " + conversion(costPath.pathCost) + " minutes");
				System.out.println();
				System.out.println();
				
				}
		}
		
	}
	
	/*
	 * Method that finds all routes under a certain limit inputed by the user.
	 */
	private void timeLimit() {
		System.out.println("====== List of Landmarks ======");
		System.out.println();
		for(int i = 1; i<=Main.landmarksMap.size();i++) {
			System.out.println("id = " + i + ")");
			System.out.println("	Landmark Name = "+ Main.landmarksMap.get(i).data);
			System.out.println();
		}

		int landmarkOne = 0;
		boolean inputError = true;
		do {
	        try {
	        	System.out.println("Choose Starting Point for Search(by Id).");
	        	landmarkOne = input.nextInt();
	        	inputError = false;
	        	if(landmarkOne <1 || landmarkOne > Landmark.counter) {
	        		inputError = true;
	        		System.out.println("Id entered is not assigned to a Landmark.");
	        	}
	        } 
	        catch (InputMismatchException inp) {
	            System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
	        }
	    } while (inputError);
		
		inputError = true;
		int landmarkTwo = 0;
		do {
	        try {
	        	System.out.println("Choose End Point for Search(by Id).");
	        	landmarkTwo = input.nextInt();
	        	inputError = false;
	        	if(landmarkTwo <1 || landmarkTwo > Landmark.counter) {
	        		inputError = true;
	        		System.out.println("Id entered is not assigned to a Landmark.");
	        	}
	        	else if(landmarkOne == landmarkTwo) {
	        		inputError = true;
	        		System.out.println("Please choose a different Id for the end Point to the starting point.");
	        	}
	        } 
	        catch (InputMismatchException inp) {
	            System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
	        }
	    } while (inputError);
		
		int limit = 0;
		inputError = true;
		do {
			try {
				System.out.println("Input Time Limit in minutes.");
				limit = input.nextInt();
				inputError = false;
				if(limit <= 0) {
					System.out.println("Please enter a limit greater than 0 metres");
					inputError = true;
				}
			}
			catch(InputMismatchException inp){
				System.out.println("Incorrect input, only Integers are Accepted.");
	            input.next();
			}
		}while(inputError);
		
	
		Landmark<?> start = Main.landmarksMap.get(landmarkOne);
		Landmark<?> end = Main.landmarksMap.get(landmarkTwo);
		List<CostedPath> result = searcher.findAllRoutes(start, null, end.data, 0);
		List<CostedPath> limited = new ArrayList<>();
//		Iterates over list of all Costed Paths.
		for(int i=0; i<result.size();i++) {
//			if pathCost is less than or equal to the user inputted limit add it to the limited ArrayList.
			if(conversion(result.get(i).pathCost)<= limit) {
				limited.add(result.get(i));
			}
		}
		
		if(limited.size()==0) {
			System.out.println("No route between two landmarks.");
		}
		
		else {
			for(int i=0;i<limited.size(); i++) {
				System.out.println("Route " + (i+1));
				System.out.println("--------------------------------------------");
				System.out.println("	Route "+ (i+1) + " contains " + limited.get(i).pathList.size() 
						+ " Landmarks.");
				System.out.println();
				for(int j=0; j<limited.get(i).pathList.size(); j++) {
					System.out.println("	Landmark " + (j+1) + ": " + limited.get(i).pathList.get(j).data);
				}
				System.out.println("--------------------------------------------");
				System.out.println("	Total Time : " + conversion(limited.get(i).pathCost) + " minutes.");
				System.out.println();
			}
		}
	}
	
//	Converts distance in meters to time in minutes.
	private int conversion(int distance) {
		double time = (distance/(1000 * 4.5)) *60;
		if(time<1) {
			time = 1;
		}
		else {
			time = Math.round(time);
		}
		
		int result = (int) time;
		return result;
		
	}
//	Converts time in minutes to distance in meters.
	private int reverseConversion(int time) {
		double distance = (time*4.5*1000)/60;
		distance = Math.round(distance);
		
		int result = (int) distance;
		return result;
		
	}
}
