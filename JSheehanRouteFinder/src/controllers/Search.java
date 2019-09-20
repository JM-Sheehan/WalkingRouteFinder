package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search {

	public Search() {		
	}
	
	/*
	 * Uses dijkstra's algorithm to find the shortest path between two nodes.
	 */
	public static <T> CostedPath findShortestPath(Landmark<?>startLand, T lookingFor) {
//		Object to be returned with the shortest Path.
		CostedPath shortestPath = new CostedPath();
//		Lists to store encountered and unencountered nodes.
		List<Landmark<?>> encountered = new ArrayList<>();
		List<Landmark<?>> unencountered = new ArrayList<>();
//		Set starting value to 0;
		startLand.nodeValue = 0;
		unencountered.add(startLand); //Add the start node as the only value in the unencountered list to start

		Landmark<?> currentLand;
		do {//Loop until all nodes have been encountered.
			currentLand = unencountered.remove(0);//get node at start of unencountered list.
			encountered.add(currentLand);//add to encountered list.
			if(currentLand.data.equals(lookingFor)) {//result found,assemble path in reverse.
				shortestPath.pathList.add(currentLand);
				shortestPath.pathCost = currentLand.nodeValue;
				while(currentLand != startLand) {//keep going until start point reached.
					boolean foundPrevPathLand = false;
					for(Landmark<?> e: encountered) {
						for(Link  l :e.neighbours) {
							/*
							 * if link links back to current node &
							 * if the distance between current node and the node of this
							 * iteration is the difference between the two nodes node value.
							 */
							if(l.landmarkTwo.data.equals(currentLand.data) && 
									currentLand.nodeValue - l.distance == e.nodeValue) {
								shortestPath.pathList.add(0,  e);//add the landmark at e to the front of the path list.
								currentLand = e;
								foundPrevPathLand = true;// used to break out of outside loop.
								break;
							}
						}
						if(foundPrevPathLand) break;//Found previous node break outer loop.
					}
				}
				for(Landmark<?> e:encountered) e.nodeValue = Integer.MAX_VALUE;//resets all nodeValues.
				for(Landmark<?> e:unencountered) e.nodeValue = Integer.MAX_VALUE;//resets all nodeValues.
				return shortestPath;//returns shortest path found
			}			
			for(Link link: currentLand.neighbours) {
				if(!encountered.contains(link.landmarkTwo)) {
					/*
					 * set the landmarks node value to the lower of its current value or the value of the node linking to it
					 * + the distance between the two nodes.
					 */
					link.landmarkTwo.nodeValue = Integer.min(link.landmarkTwo.nodeValue, currentLand.nodeValue + link.distance);
					unencountered.add(link.landmarkTwo);
				}
			}
			Collections.sort(unencountered, (n1,n2) -> n1.nodeValue - n2.nodeValue);//Sort nodes in ascending order of nodeValue.
		}while(!unencountered.isEmpty());
		
		return null;//Return if there is no path.
	}
	
	/*
	 * find all routes from start node to end node.
	 * Recursively identifies all paths depth first and returns them to user.
	 */
	public static <T> List<CostedPath> findAllRoutes(Landmark<?> startLand, List<Landmark<?>> encountered,T lookingfor, int totalCost){
		List<CostedPath> result = new ArrayList<>();
		List<CostedPath> temp2 = null;
		if(startLand.data.equals(lookingfor)){//Found the node your looking for.
			CostedPath shortestPath = new CostedPath();//For storing the path.
			shortestPath.pathList.add(startLand);//Add current starting point to the pathlist.
			result = new ArrayList();
			result.add(shortestPath);//add costed path to final result.
			return result;
		}
		if(encountered == null) encountered = new ArrayList<>();//Creates empty arraylist on first run to store encountered nodes.
		encountered.add(startLand);//Adds starting point to the encountered list.
		
		for(Link link : startLand.neighbours) {//Iterates over the neighbours of the current starting point.
			if(!encountered.contains(link.landmarkTwo)) {//Ensures that landmarks are not checked multiple times.
				 temp2 = findAllRoutes(link.landmarkTwo, new ArrayList<>(encountered),
						lookingfor, totalCost+ link.distance);//Recursive method call, with the with the destination node of the current link used as a starting point.
				 
				 if(temp2!= null) {//If result found
					 for(CostedPath path : temp2) {//Iterate over results.
						 path.pathList.add(0, startLand);//add current path to front of pathlist.
						 path.pathCost+=link.distance;//add the cost of the current link to the total path cost.
					 }
					 if(result == null) result = temp2;//First result found.
					 else result.addAll(temp2);//All subsequent results.
				 }
			}
		}
		return result;//Final return statement.
	}
	
	
}
