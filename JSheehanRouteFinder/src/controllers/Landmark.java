package controllers;

import java.util.ArrayList;
import java.util.List;

/*
 * Custom data type used for storing data for each landmark.
 */
public class Landmark<T> {
	public T data;
	public int id;
	public int nodeValue = Integer.MAX_VALUE;//Used for dijkstras algorithm.
	public List<Link> neighbours = new ArrayList<>();//Adjacency list implementation
	public static int counter = 0;//id used as key for hashmap.
	
//	constructor class
	public Landmark(T data) {
		this.data = data;
//		iterates counter and then sets this landmarks id = to the iterated value.
		counter ++;
		id = counter;
	}
	
//	Method used when creating a new landmark, creates a link from this node to the
//	destination node with the given data and also does so in the opposite direction
//	for the destination node.
	public void connectToNeighbour(Landmark<?> destination, int cost, String streets) {
		Link link = new Link(this, destination, cost, streets);
		Link reverseLink = new Link(destination, this, cost, streets);
		neighbours.add(link);
		destination.neighbours.add(reverseLink);
	}
	
//	Method used when reading the xml file to avoid duplicating links.
//	Only creates a link in one direction.
	public void connectDirected(Landmark<?> destination, int cost, String streets) {
		Link link = new Link(this, destination, cost, streets);
		neighbours.add(link);
	}
	
}
