package app.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Path {

	private GraphNode origin;
	private GraphNode destination;
	private ArrayList<GraphNode> nodeList= new ArrayList<GraphNode>();
	private ArrayList<Edge> edgeList = new ArrayList<Edge>();
	

	/**NEWCODE
	 * 
	 */
	private int pathCost =0;


	private int totalDistance=0;
	private double totalDanger=0;
	private double totalDifficulty=0;
/****************** Constructors ***************************/
	public Path(GraphNode origin, GraphNode destination) {
		this.origin = origin;
		this.destination = destination;
		
		nodeList.add(origin);
		//path.add(destination);
	}
	
	public Path()
	{
		
	}
	
/*************** Add/Remove *************************/

	/**
	 * Adds a given node to the path if not already in the path. Updates all D-Values accordingly
	 * 
	 * @ToDo
	 * [Refactor] Move += operations to calculation methods
	 * @param nodeToAdd
	 * @return
	 */
	public boolean addNodeToEnd(GraphNode nodeToAdd)
	{
		if(!nodeList.contains(nodeToAdd))
		{
			nodeList.add(nodeToAdd);
			
			// Needs to be modified so it knows what its connecting to
			
	//		totalDistance += nodeToAdd.getAdjacentNodes().get(nodeToAdd).getDistance();
	//		totalDanger += nodeToAdd.getAdjacentNodes().get(nodeToAdd).getDanger();
	//		totalDifficulty += nodeToAdd.getAdjacentNodes().get(nodeToAdd).getDifficulty();
			return true;
		}
		else
			return false;
	}
	
	
	public boolean addNodeToPosition(int position, GraphNode nodeToAdd)
	{
		if(!nodeList.contains(nodeToAdd))
		{
			nodeList.add(position, nodeToAdd);
			
	//		totalDistance += nodeToAdd.getAdjacentNodes().get(nodeToAdd).getDistance();
	//		totalDanger += nodeToAdd.getAdjacentNodes().get(nodeToAdd).getDanger();
	//		totalDifficulty += nodeToAdd.getAdjacentNodes().get(nodeToAdd).getDifficulty();
			return true;
		}
		else
			return false;
	}
	/**
	 * Removes a given node from the ppath if contained in path. Will also update all D-values accordingly
	 * @param nodeToRemove
	 * @return
	 */
	public boolean removeNodeFromPath(GraphNode nodeToRemove)
	{
		if(nodeList.contains(nodeToRemove))
		{
			nodeList.remove(nodeToRemove);
			totalDistance -= nodeToRemove.getAdjacentNodes().get(nodeToRemove).getDistance();
			totalDanger -= nodeToRemove.getAdjacentNodes().get(nodeToRemove).getDanger();
			totalDifficulty -= nodeToRemove.getAdjacentNodes().get(nodeToRemove).getDifficulty();
	
			return true;
		}
		return false;
	}
	
	/************** Utility ***************************/
	
	public void generateEdgeList()
	{
		removeNulls();
		ArrayList<Edge> connections = new ArrayList<Edge>();
		GraphNode current = null;
		GraphNode adjacent = null;
		for(int x = 0;x <nodeList.size(); x++)
		{
			if(x+1 < nodeList.size())
			{
				current = nodeList.get(x);
				adjacent = nodeList.get(x+1);
			}
			//Edgelist [node in list][hashmap of node,edge pairs][edge which connects them]
			
			
			////////////////////null here
			if(current!=null && adjacent!=null)
			connections.add(current.getAdjacentNodes().get(adjacent));
		}
		
		this.edgeList = connections;
	}
	
	public void generateEdgeList2(Graph graph)
	{
		removeNulls();
		ArrayList<Edge> connections = new ArrayList<Edge>();
		for(int x = 0; x <nodeList.size()-1; x++)
		{
			for(Edge e : graph.getListOfAllEdges())
			{
				if(e.getOrigin().equals(nodeList.get(x)))
				{
					if(e.getDestination().equals(nodeList.get(x+1)))
					{
					 connections.add(e);
					}
				}
			}
		}
		this.edgeList=connections;
	}
	
	
	 public void removeNulls()
	 {
		 nodeList.removeAll(Collections.singletonList(null));
		 
		 //accounts for null origin and destination
		 setOrigin(nodeList.get(0));
		 setDestination(nodeList.get(nodeList.size()-1));
	
	 }
	/**
	 * Calculate average danger of route by dividing total danger of all links by number of links in path
	 * 
	 * @ToDo 
	 * [Refactor] Weight danger with distance of edge in mind?
	 * [Refactor] Move addition of total danger from add/remove to this method
	 * 
	 * @return Value representing average danger value
	 */
	public double calculateDanger()
	{
		return (totalDanger / (nodeList.size() -2));
		
	}
	
	/**
	 * Calculate average difficulty of route traversal by dividing total diffculty value by number of edges in path.
	 * 
	 * @ToDo
	 * [Refactor] Weight difficulty with distance of edge in mind?
	 * [Refactor] Move addition of total difficulty from add/remove to this method
	 * 
	 * @return Value representing average difficulty value
	 */
	public double calculateDifficulty()
	{
		return (totalDifficulty / (nodeList.size()-2));
	}
	
		
		
	/************** Setters & Getters ********************/
		
	public GraphNode getOrigin() {
		return origin;
	}
	
	public void setOrigin(GraphNode origin) {
		this.origin = origin;
	}
	
	public GraphNode getDestination() {
		return destination;
	}
	
	public void setDestination(GraphNode destination) {
		this.destination = destination;
	}
	
	/**
	 * This getter appends the destination node to the path
	 * @return
	 */
	public ArrayList<GraphNode> getNodeList() {
		if(!nodeList.contains(this.destination))
		{
			nodeList.add(destination);
		}
		return nodeList;
	}
	
	public void setNodeList(ArrayList<GraphNode> path) {
		this.nodeList = path;
	}
	
	/**
	 * NEWCODE
	 * @return
	 */
	public int getPathCost() {
		return pathCost;
	}
	
	/**
	 * NEWCODE
	 * @param pathCost
	 */
	public void setPathCost(int pathCost) {
		this.pathCost = pathCost;
	}
	
	/*
	 * NEWCODE
	 */
	public void updatePathCost(int increment)
	{
		this.pathCost += increment;
	}
	
	public int getTotalDistance()
	{
		return this.totalDistance;
	}
	
	public ArrayList<Edge> getEdgeList()
	{
		return this.edgeList;
	}

}




