package app.model;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphNode {
/*************** Fields *****************************************************/
	private String name;
	private int xCoord;
	private int yCoord;
	private HashMap<GraphNode, Edge> adjacentNodes;
	private int dValue = Integer.MAX_VALUE;
	
/****************** Constructors  *******************************************/
	
	public GraphNode(String name, int x, int y)
	{
		this.name = name;
		this.xCoord = x;
		this.yCoord = y;
		adjacentNodes = new HashMap<GraphNode,Edge>();
	}
/*********************** Add/Remove Nodes *********************************/
	
	/**
	 *  Adds a node and path to the hashmap containing a list of adjacent nodes
	 * @param newNeighbour The new node to be added to the hashmap of neighbours and paths
	 * @param connector The path (Edge) connecting this node and another adjacent node
	 */
	@SuppressWarnings("finally")
	public boolean addAdjacentNode(GraphNode newNeighbour, Edge connector)
	{
		try {
			adjacentNodes.put(newNeighbour, connector);
			return true;
		}
		finally
		{
			return false;
		}			
	}
		
	
	/**
	 * Takes a neighbour node already in the hashmap and removes it
	 * @param neighbourToRemove
	 */
	public boolean removeAdjacentNode(GraphNode neighbourToRemove)
	{
		if(adjacentNodes.containsKey(neighbourToRemove)) {
			adjacentNodes.remove(neighbourToRemove);
			return true;
		}
		return false;
	}
	
/************************ Getters & Setters *********************************/
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public HashMap<GraphNode, Edge> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(HashMap<GraphNode, Edge> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}
	
	public void setDValue(int d)
	{
		this.dValue = d;
	}
	
	public int getDValue()
	{
		return this.dValue;
	}
	
	public String listEdges()
	{
		String edgeData = "";
		
		for (HashMap.Entry<GraphNode,Edge> entry : adjacentNodes.entrySet()) {
		    System.out.println("Destination = " + entry.getKey().getName() + ", Distance = " + entry.getValue().getDistance()
		    		+ ", Danger = " + entry.getValue().getDanger()
		    		+ ", Difficulty = " + entry.getValue().getDistance());
		}
		return edgeData;
	}
	
	public ArrayList<GraphNode> getAdjacentNodesAsList()
	{
		ArrayList<GraphNode> adjNodes = new ArrayList<GraphNode>();
		for(HashMap.Entry<GraphNode,Edge> entry : adjacentNodes.entrySet())
		{
			adjNodes.add(entry.getKey());
		}
		return adjNodes;
	}
	
	/**
	 * NEW
	 * 
	 * @return
	 */
	public ArrayList<Edge> getEdgesAsList(){
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		for(HashMap.Entry<GraphNode,Edge> entry : adjacentNodes.entrySet())
		{
			edgeList.add(entry.getValue());
		}
		return edgeList;
		
	}
	
	
	
	

}
