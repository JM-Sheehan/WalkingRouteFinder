package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.HandleXML;
import controllers.Landmark;
import controllers.Link;
import controllers.Menu;

public class Main {
	
//	Map that stores the Landmark objects along with an ID number.
	public static Map<Integer, Landmark<?>> landmarksMap = new HashMap<>();
	private static Menu menu;
	private static HandleXML handleXML;
	
	public static void main(String[] args) {
//		Reads the routes.xml file and constructs the landmark objects along with links.
		HandleXML.parseMap();
		menu = new Menu();
		menu.runMenu();
	}
}
