package controllers;

import java.util.ArrayList;
import java.util.List;

// Custom data type for storing link data.
public class Link {

	public int distance;
	public String streets;
	public Landmark<?> landmarkOne;
	public Landmark<?> landmarkTwo;
	
//	Constructor method.
	public Link(Landmark<?> landmarkOne, Landmark<?> landmarkTwo, int distance, String streets) {
		this.landmarkOne = landmarkOne;
		this.landmarkTwo = landmarkTwo;
		this.distance = distance;
		this.streets = streets;
	}

}
