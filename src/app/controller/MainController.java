package app.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.sun.javafx.fxml.expression.LiteralExpression;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import app.model.Edge;
import app.model.Graph;
import app.model.GraphNode;
import app.model.Path;
import app.util.CSVFileIO;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class MainController {

/*************** FXML ************************/
	@FXML
	private Button viaButton;
	@FXML
	private Button avoidButton;
	@FXML
	private Button calculateRouteButton;
	@FXML
	private ComboBox<String> originChoice;
	@FXML
	private ComboBox<String> destinationChoice;
	@FXML
	private ComboBox<String> routeStyleChoice;
	@FXML
	private ComboBox<String> viaChoice;
	@FXML
	private ComboBox<String> avoidChoice;
	@FXML
	private ListView<String> viaList;
	@FXML
	private ListView<String> avoidList;
	@FXML
	private Pane drawPane; //Added in V2
/*************** Fields **********************/
	private Graph allNodes = new Graph();
	private Color[] colorBank = {Color.PURPLE, Color.ORANGE,Color.BLACK};

/*************** Constructor *****************/
	
	public MainController()
	{

	}
		
/*************** Save/Load Data ****************/
	
	/**
	 * Creates CSV File reader object, takes a text file and generates an arraylist using 
	 * the data in the csv file
	 * @throws IOException 
	 */
	public void loadNodeData() 
	{
		CSVFileIO csv = new CSVFileIO();			// Creates a CSV reader to read a file
	
		FileChooser csvLoader = new FileChooser();	//Creates a filechooser to select a csv file to read
		csvLoader.setTitle("Select CSV Node List file");
		csvLoader.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt", "*.TXT"));	//only allow text files

		File selectedList = csvLoader.showOpenDialog(null);

		try {
			allNodes.setGraphOfAllNodes(csv.readNodesFromFile(selectedList));	//read file and generate arraylist to populate allnode list with

		} catch (Exception ex) {
			System.out.print("file invalid");
		}
		
		populateDropDowns();
	}
		
	/**
	 * Creates CSV file reader, then takes a text file and adds edge data from that file to the graph of
	 * all nodes which has already been populated 
	 * @throws IOException 
	 */
	public void loadEdgeData() throws IOException 
	{
		CSVFileIO csv = new CSVFileIO();			// Creates a CSV reader to read a file
		
		FileChooser csvLoader = new FileChooser();	//Creates a filechooser to select a csv file to read
		csvLoader.setTitle("Select CSV Edge Data file");
		csvLoader.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt", "*.TXT"));	//only allow text files

		File selectedList = csvLoader.showOpenDialog(null);

//		try {
			allNodes.setGraphOfAllNodes((csv.readEdgesToGraph(allNodes.getGraphOfAllNodes(),selectedList)));	//read file and generate arraylist to populate allnode list with
//
//		} catch (Exception iOException) {
//			System.out.print("file invalid");
//		}
	}
	
	/**
	 * Creates CSV file reader, then takes a text file and adds edge data from that file to the graph of
	 * all nodes which has already been populated 
	 * @throws IOException 
	 */
	public void loadEdgeData2() throws IOException
	{
	CSVFileIO csv = new CSVFileIO();			// Creates a CSV reader to read a file
		
		FileChooser csvLoader = new FileChooser();	//Creates a filechooser to select a csv file to read
		csvLoader.setTitle("Select CSV Edge Data file");
		csvLoader.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt", "*.TXT"));	//only allow text files

		File selectedList = csvLoader.showOpenDialog(null);

		try {
			allNodes.setListOfAllEdges((csv.readEdgesToEdgeList(allNodes, selectedList)));	//read file and generate arraylist to populate allnode list with
			allNodes.assignEdgesToNodes();
		} catch (Exception iOException) {
			System.out.print("file invalid");
		}
	}
	
	/**
	 * Loads previously saved dining menu from xml file
	 * @Log Added in V2
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * 
	 * @throws Exception
	 */
	public void loadGraph() throws ClassNotFoundException, FileNotFoundException, IOException
	{
        XStream xstream = new XStream(new DomDriver());
        ObjectInputStream is1 = xstream.createObjectInputStream(new FileReader("graphOfAllNodes.xml"));
        allNodes= (Graph) is1.readObject();
      
        is1.close();
        populateDropDowns();
        allNodes.assignEdgesToNodes();
    }
	
	/**
	 * saves dining menu to xml
	 * @Log Added in V2
	 * @throws Exception
	 */
	public  void saveGraph() throws Exception
    {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out1 = xstream.createObjectOutputStream(new FileWriter("graphOfAllNodes.xml"));
        out1.writeObject(allNodes);        
        out1.close();    

    }

	/**
	 * Method which initializes the drop down menus. Does not add elbow nodes to selection
	 */
	private void populateDropDowns()
	{
		routeStyleChoice.getItems().addAll("No Preference","Shortest","Safest","Easiest");
		for(GraphNode node : allNodes.getGraphOfAllNodes())
		{
			if(!node.getName().contains("Elb")) {
			originChoice.getItems().add(node.getName());
			destinationChoice.getItems().add(node.getName());
			viaChoice.getItems().add(node.getName());
			avoidChoice.getItems().add(node.getName());
			}
		}
	}
	
/********************* Utility Methods ***********************************/
	
public int routeSelection()
{
	if(routeStyleChoice.getValue().equals("Safest"))
	{
		return 1;
	}
	else if(routeStyleChoice.getValue().equals("Easiest"))
	{
		return 2;
	}
	else
	 return 0;
}
 
 
 /*************************  Waypoint/Avoid Management Methods *********************************/
	/*
	 * Adds a selected node from a dropdown menu to the list of nodes to avoid 
	 */
	public void addVia()
	{
		if(viaChoice.getValue() !=null
				&& !viaList.getItems().contains(viaChoice.getValue()) //The list does not already contain the item
				&& originChoice.getValue()!=viaChoice.getValue() //The origin cannot be via
				&& destinationChoice.getValue()!=viaChoice.getValue()
				&& !avoidList.getItems().contains(viaChoice.getValue()))//the avoid list cannot also contain the node
		{
			viaList.getItems().add(viaChoice.getValue());
		}
	}

	/**
	 * removes a selected node from the list of nodes to avoid
	 */
	public void cancelVia()
	{
		if(viaList.getSelectionModel().getSelectedIndex()!=-1)
			viaList.getItems().remove(viaList.getSelectionModel().getSelectedIndex());
	}
	
	
	/*
	 * Adds a selected node from a dropdown menu to the list of nodes to avoid 
	 */
	public void addAvoid()
	{
		if(avoidChoice.getValue() !=null
				&& !avoidList.getItems().contains(avoidChoice.getValue()) //The list does not already contain the item
				&& originChoice.getValue()!=avoidChoice.getValue() //The origin cannot be via
				&& destinationChoice.getValue()!=avoidChoice.getValue()
				&& !viaList.getItems().contains(avoidChoice.getValue()))
		{
			avoidList.getItems().add(avoidChoice.getValue());
		}
	}

	/**
	 * removes a selected node from the list of nodes to avoid
	 */
	public void cancelAvoid()
	{
		if(avoidList.getSelectionModel().getSelectedIndex()!=-1)
			avoidList.getItems().remove(avoidList.getSelectionModel().getSelectedIndex());
		
		allNodes.assignEdgesToNodes();
	}
	
	public ArrayList<GraphNode> getViaFromList()
	{
		ArrayList<GraphNode> nodesToVia = new ArrayList<GraphNode>();
		
		for(GraphNode potentialMatch : allNodes.getGraphOfAllNodes()) {
			for(String nodeName:viaList.getItems())
			{
				if(potentialMatch.getName().equals(nodeName))
				{
					nodesToVia.add(potentialMatch);
				}
			}
		}
		return nodesToVia;
	}	
	
	/**
	 * builds an arraylist of nodes from the list of node names
	 * @return
	 */
	public ArrayList<GraphNode> getAvoidsFromList()
	{
		ArrayList<GraphNode> nodesToRemove = new ArrayList<GraphNode>();
		
		for(GraphNode potentialMatch : allNodes.getGraphOfAllNodes()) {
			for(String nodeName:avoidList.getItems())
			{
				if(potentialMatch.getName().equals(nodeName))
				{
					nodesToRemove.add(potentialMatch);
				}
			}
		}
		return nodesToRemove;
	}
	
/******************** Pathing Methods ***************************************/
//	
//
//	/**
//	 * For each node in the list of nodes to avoid, this method removes that node from the overall graph of nodes
//	 * @param graph
//	 * @return
//	 */
//	public Graph removeAvoidsFromGraph(Graph graph)
//	{
//		ArrayList<GraphNode> nodesToAvoid = getAvoidsFromList();
//		return graph.removeNodesFromGraph(nodesToAvoid);
//	}
//	
	public void findAPathDjikstra()
	{
		clearEdges();
		GraphNode origin=null;
		GraphNode destination=null;
		
		int selection = routeSelection();
		
		allNodes.assignEdgesToNodes();

		if(!avoidList.getItems().equals(null)) {
			ArrayList<GraphNode> nodesToAvoid = getAvoidsFromList();
			for(GraphNode node : nodesToAvoid)
			{
				allNodes.removeEdgesInvolvingAvoid(node);
			}
		}	
		
		for(GraphNode node : allNodes.getGraphOfAllNodes())
		{
			if(node.getName().equals(originChoice.getValue()))
			{
				origin=node;
			}
			if(node.getName().equals(destinationChoice.getValue()))
			{
				destination = node;
			}
		}
		Path path = new Path();
		if(viaList.getItems().isEmpty()) {
			path = allNodes.djikstra(origin, destination, selection);
		}
		else
		{
			 path =  findAPathWithVias(origin, destination,selection);
		}
		drawAPath2(path,Color.PURPLE);
		drawCities(allNodes);
	}
	
	public void findAPathDjikstra2()
	{
		clearEdges();
		GraphNode origin=null;
		GraphNode destination=null;

		ArrayList<GraphNode> nodesToAvoid = getAvoidsFromList();
	
		for(GraphNode node : allNodes.getGraphOfAllNodes())
		{
			if(node.getName().equals(originChoice.getValue()))
			{
				origin=node;
			}
			if(node.getName().equals(destinationChoice.getValue()))
			{
				destination = node;
			}
		}
	
		ArrayList<Path> paths = allNodes.multiPath(origin, destination,nodesToAvoid);
		drawManyPaths2(paths);
		drawCities(allNodes);

	}

	
	/** Merges two paths to combine into a single path
	 * Used to account for waypointing
	 * @param path1 The origin to waypoint path
	 * @param path2 The Waypoint to destination path
	 * @return mergedPath The new combined path
	 */
	public Path mergePaths(Path path1, Path path2)
	{
		Path mergedPath = path1;
		
		ArrayList<GraphNode> secondNodeList = path2.getNodeList();
		secondNodeList.remove(0);
		
		for(GraphNode node : secondNodeList)
		{
			
			mergedPath.getNodeList().add(node);
		}
		
		return mergedPath;
	}
	

	public Path findAPathWithVias(GraphNode origin, GraphNode destination, int selection)
	{
	
		/*
		 * Path origin to waypoint x
		 * reset origin to waypoint x
		 * if more waypoints, path origin to next waypoint and merge path
		 * else path origin to destination and merge
		 * @ToDo make this into own method
		 */
		
		ArrayList<GraphNode> nodesToVia = getViaFromList();
		 
		Path currentPath = allNodes.djikstra(origin, nodesToVia.get(0), selection);
		for(int x = 0; x<nodesToVia.size(); x++)
		{
			Path path2=new Path();
			if(x+1 <nodesToVia.size())
			{
				path2 = allNodes.djikstra(nodesToVia.get(x), nodesToVia.get(x+1),selection);
			}
			else
			{
				path2 = allNodes.djikstra(nodesToVia.get(x),destination,selection);
			}
			
			currentPath = mergePaths(currentPath, path2);
		}	
		
		
		
		return currentPath;
	}

/****************** DRAWING METHODS *********************************/


	public void drawNodes()
	{
		drawCities(allNodes);
	}
	
	public void drawEdges()
	{
		drawAllEdges(allNodes);
	}

	public void drawCities(Graph graph)
	{
		for(GraphNode node : graph.getGraphOfAllNodes())
		{
			if(!node.getName().contains("Elb"))
			{
			Circle nodeMarker = new Circle(node.getxCoord(), node.getyCoord(), 5, Color.DEEPSKYBLUE);
			nodeMarker.setStroke(Color.DARKRED);
			drawPane.getChildren().add(nodeMarker);
			}
		}
	}
	
	/**
	 * Iterates through the list of all edges in the graph and draws a red line for each connection between nodes
	 * Primarily for testing
	 * @param graph
	 */
	public void drawAllEdges(Graph graph)
	{
		for(Edge edge : graph.getListOfAllEdges())
		{
			Line connection = new Line(edge.getOrigin().getxCoord(), edge.getOrigin().getyCoord(),
					edge.getDestination().getxCoord(),edge.getDestination().getyCoord());
			connection.setStroke(Color.RED);
			connection.setStrokeWidth(1);
			drawPane.getChildren().add(connection);
		}
		drawCities(allNodes);
	}
	
	public void clearEdges()
	{
		drawPane.getChildren().clear();
		drawCities(allNodes);
	}

	
	/**
	 * Takes a path object, generates a list of all edges in that path, then draws a black line connecting each node on the map
	 * @param path
	 */
	public void drawAPath(Path path)
	{
		Color chosenColor = colorBank[routeSelection()];
		for(Edge edge : path.getEdgeList())
		{
			Line connection = new Line(edge.getOrigin().getxCoord(), edge.getOrigin().getyCoord(),
										edge.getDestination().getxCoord(),edge.getDestination().getyCoord());
			connection.setStroke(chosenColor);
			connection.setStrokeWidth(2);
			drawPane.getChildren().add(connection);
		}
	}
	
	public void drawAPath2(Path path, Color chosenColor)
	{
		for(int x =0 ;x< path.getNodeList().size()-1; x++)
		{
			Line connection = new Line(path.getNodeList().get(x).getxCoord(),path.getNodeList().get(x).getyCoord(),
									path.getNodeList().get(x+1).getxCoord(),path.getNodeList().get(x+1).getyCoord());
			connection.setStroke(chosenColor);
			connection.setStrokeWidth(2);
			drawPane.getChildren().add(connection);
		}
	}
	
	public void drawManyPaths(ArrayList<Path> manyPaths)
	{
		for(int x = 0; x <manyPaths.size(); x++)
		{	
			drawAPath(manyPaths.get(x));
		}
		
	}
	

	public void drawManyPaths2(ArrayList<Path> manyPaths)
	{
		
		for(int x = 0; x <manyPaths.size(); x++)
		{	
			drawAPath2(manyPaths.get(x),colorBank[x]);
		}
		
	}
/****************** Testing Methods *******************************/
	
	public void printNodeList(Graph graph)
	{
		for(GraphNode node: graph.getGraphOfAllNodes())
		{
			System.out.println("Node Name is: " + node.getName()
					+ "\tCoordinates: (" + node.getxCoord() + "," +node.getyCoord()+ ")");
			node.listEdges();
		}
		for(Edge edge :graph.getListOfAllEdges())
		{
			System.out.println("\tEdge Connects: " + edge.getOrigin().getName() 
					+ " --> " + edge.getDestination().getName()
					+ "...Distance = " + edge.getDistance());
		}
	}
	
	public void printNodeData()
	{
		printNodeList(allNodes);
	}
	
	/**
	 * Test method which draws a single box on the map
	 * 
	 * @Log added in V2
	 */
	public void drawBoxes()
	{
		Rectangle rect1 = new Rectangle(481,265,50,50);
		Rectangle rect2 = new Rectangle(265,481,50,50);
		Rectangle rect3 = new Rectangle(1500,600,50,50);
		rect1.setStroke(Color.DODGERBLUE);
		rect1.setFill(Color.RED);
		rect2.setStroke(Color.DODGERBLUE);
		rect2.setFill(Color.GREEN);
		rect3.setStroke(Color.DODGERBLUE);
		rect3.setFill(Color.RED);
		drawPane.getChildren().addAll(rect1,rect2,rect3);
	}
	
	public void testPrintEdges()
	{
		for(GraphNode node : allNodes.getGraphOfAllNodes())
		{
			if(node.getName().equals("Winterfell"))
			{
				System.out.println(node.getAdjacentNodes());
			}
			if(node.getName().equals("Castle Black"))
			{
				System.out.println(node.getAdjacentNodes());
			}
		}
	}

}
