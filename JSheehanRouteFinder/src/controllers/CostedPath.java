package controllers;

import java.util.ArrayList;
import java.util.List;

/*
 * Used to find a path and record the total cost of the path from one node to
 * another.
 */
public class CostedPath {
	public int pathCost = 0;//Total cost of path.
	public List<Landmark<?>> pathList = new ArrayList<>();//List of landmarks linking chosen paths.
	
	public CostedPath() {
		
	}
}
