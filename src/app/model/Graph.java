package app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;




public class Graph {

	private ArrayList<GraphNode> graphOfAllNodes = new ArrayList<GraphNode>();
	private ArrayList<Edge> listOfAllEdges = new ArrayList<Edge>();
	
	
	public void assignEdgesToNodes()
	{
		
		for(Edge edge:this.listOfAllEdges)
		{
			GraphNode origin = null;
			GraphNode destination = null;
			while(origin==null ) {
				for(GraphNode current :graphOfAllNodes)
				{
					if(current.getName().equals(edge.getOrigin().getName()))
					{
					origin=current;
					}
				}
			}
			while(destination==null)
			{
				for(GraphNode current : graphOfAllNodes)
				{
					if(current.getName().equals(edge.getDestination().getName()))
					{
						destination=current;
					}
				}
			}
	
			origin.getAdjacentNodes().put(destination, edge);
			
			//Edge origin and destination may need to be swapped here			
			Edge oppositeDirectionEdge = new Edge(edge.getDistance(),edge.getDanger(),edge.getDifficulty(),edge.getDestination(),edge.getOrigin());
			destination.getAdjacentNodes().put(origin, oppositeDirectionEdge); 
		}
	}
	
		
	/**
	 * NEW FOR DJIKSTRA
	 * 
	 */
public Path djikstra(GraphNode startNode, GraphNode lookingfor, int selection){
	Path cp=new Path(); 				//Create result object for cheapest path
	List<GraphNode> encountered=new ArrayList<>(), unencountered=new ArrayList<>(); //Create encountered/unencounteredlists
	startNode.setDValue(0);									 //Set the starting node value to zero
	unencountered.add(startNode); 							//Add the start node as the only value in the unencounteredlist to start
	GraphNode currentNode;
	do{ 												//Loop until unencountered list is empty
		currentNode=unencountered.remove(0); 			//Get the first unencountered node (sorted list, so will have lowest value)
		encountered.add(currentNode); 					//Record current node in encountered list
		if(currentNode.equals(lookingfor))
		{ 												//Found goal -assemble path list back to start and return it
			cp.getNodeList().add(currentNode); 			//Add the current (goal) node to the result list (only element)
			cp.setPathCost(currentNode.getDValue()); 	//The total cheapest path cost is the node value of the current/goal node
			
			while(currentNode!=startNode) 
			{ 												//While we're not back to the start node...
				boolean foundPrevPathNode=false; 			//Use a flag to identify when the previous path node is identified
				for(GraphNode node: encountered) 
				{ 											//For each node in the encountered list...
					
				
					for(Edge edge: node.getEdgesAsList()) 	//For each edge from that node...
						if(edge.getDestination()==currentNode 
						&& currentNode.getDValue()-edge.getCost(selection)==node.getDValue())
						{ 									//If that edge links to the
															//current node and the difference in node values is the cost of the edge -> found path node!
							if(!node.equals(null))
							cp.getNodeList().add(0,node); 	//Add the identified path node to the front of the result list
							currentNode=node; 				//Move the currentNode reference back to the identified path node
							foundPrevPathNode=true; 		//Set the flag to break the outer loop
							break; 							//We've found the correct previous path node and moved the currentNodereference 
															//back to it so break the inner loop 
						}
					if(foundPrevPathNode) break; 			//We've identified the previous path node, so break the inner loop to continue
				}
			}												//Reset the node values for all nodes to (effectively) infinity so we can search again (leave no footprint!)
			for(GraphNode n: encountered) n.setDValue(Integer.MAX_VALUE);
			for(GraphNode n: unencountered) n.setDValue(Integer.MAX_VALUE);
			
		cp.generateEdgeList();
			return cp; 										//The costed (cheapest) path has been assembled, so return it!
		}												//We're not at the goal node yet, so...
		for(Edge edge : currentNode.getEdgesAsList()) { 		//For each edge/link from the current node...
			if(!encountered.contains(edge.getDestination())) { //If the node it leads to has not yet been encountered (i.e. processed)
				edge.getDestination().setDValue(Integer.min(edge.getDestination().getDValue(), currentNode.getDValue()+edge.getCost(selection))); //Update the node value at the end //of the edge to the minimum of its current value or the total of the current node's value plus the cost of the edge
				unencountered.add(edge.getDestination());
			}
		}
		Collections.sort(unencountered,(n1,n2)->n1.getDValue()-n2.getDValue()); //Sort in ascending node value order
	}
	while(!unencountered.isEmpty());
	return null; 											//No path found, so return null
}

/**
 * NEW FOR DJIKSTRA
 * 
 */
public Path djikstra2(GraphNode startNode, GraphNode lookingfor, ArrayList<GraphNode> nodesToAvoid){
	
	for(GraphNode nodeToAvoid : nodesToAvoid) {
		removeEdgesInvolvingAvoid(nodeToAvoid);
	}
	
	Path cp=new Path(); 				//Create result object for cheapest path
	List<GraphNode> encountered=new ArrayList<>(), unencountered=new ArrayList<>(); //Create encountered/unencounteredlists
	startNode.setDValue(0);									 //Set the starting node value to zero

	unencountered.add(startNode); 							//Add the start node as the only value in the unencounteredlist to start
	GraphNode currentNode;
	do{ 												//Loop until unencountered list is empty
		currentNode=unencountered.remove(0); 			//Get the first unencountered node (sorted list, so will have lowest value)
		encountered.add(currentNode); 					//Record current node in encountered list
		if(currentNode.equals(lookingfor))
		{ 												//Found goal -assemble path list back to start and return it
			cp.getNodeList().add(currentNode); 			//Add the current (goal) node to the result list (only element)
			cp.setPathCost(currentNode.getDValue()); 	//The total cheapest path cost is the node value of the current/goal node
			
			while(currentNode!=startNode) 
			{ 												//While we're not back to the start node...
				boolean foundPrevPathNode=false; 			//Use a flag to identify when the previous path node is identified
				for(GraphNode node: encountered) 
				{ 									
					//For each node in the encountered list...
					for(Edge edge: node.getEdgesAsList()) 	//For each edge from that node...
						if(edge.getDestination()==currentNode 
						&& currentNode.getDValue()-edge.getDistance()==node.getDValue())
						{ 									//If that edge links to the
															//current node and the difference in node values is the cost of the edge -> found path node!
							if(!node.equals(null))
							cp.getNodeList().add(0,node); 	//Add the identified path node to the front of the result list
							currentNode=node; 				//Move the currentNode reference back to the identified path node
							foundPrevPathNode=true; 		//Set the flag to break the outer loop
							break; 							//We've found the correct previous path node and moved the currentNodereference 
															//back to it so break the inner loop 
						}
					if(foundPrevPathNode) break; 			//We've identified the previous path node, so break the inner loop to continue
				}
			}												//Reset the node values for all nodes to (effectively) infinity so we can search again (leave no footprint!)
			for(GraphNode n: encountered) n.setDValue(Integer.MAX_VALUE);
			for(GraphNode n: unencountered) n.setDValue(Integer.MAX_VALUE);
			

			cp.removeNulls();
			return cp; 										//The costed (cheapest) path has been assembled, so return it!
		}												//We're not at the goal node yet, so...
		for(Edge edge : currentNode.getEdgesAsList()) { 		//For each edge/link from the current node...
			if(!encountered.contains(edge.getDestination())) { //If the node it leads to has not yet been encountered (i.e. processed)
				edge.getDestination().setDValue(Integer.min(edge.getDestination().getDValue(), currentNode.getDValue()+edge.getDistance())); //Update the node value at the end //of the edge to the minimum of its current value or the total of the current node's value plus the cost of the edge
				unencountered.add(edge.getDestination());
			}
		}
		Collections.sort(unencountered,(n1,n2)->n1.getDValue()-n2.getDValue()); //Sort in ascending node value order
	}
	while(!unencountered.isEmpty());
	return null; 											//No path found, so return null
}
		
public ArrayList<Path> multiPath(GraphNode start, GraphNode dest, ArrayList<GraphNode> nodesToAvoid)
{
	ArrayList<Path> manyPaths = new ArrayList<Path>();
	ArrayList<GraphNode> nodeCache = graphOfAllNodes;
	
	Path path1 = djikstra2(start, dest, nodesToAvoid);
	manyPaths.add(path1);
	if(trimNodeListBasic(path1))
	{
		Path path2 = djikstra2(start, dest, nodesToAvoid);
		manyPaths.add(path2);
		
		if(trimNodeListBasic(path2))
		{
			Path path3 = djikstra2(start,dest, nodesToAvoid);
			manyPaths.add(path3);
		}
	}

	return manyPaths;	
}

	public boolean trimNodeListBasic(Path previousPath)
	{
		Random numGen = new Random();
		int indexToTrim = numGen.nextInt(previousPath.getNodeList().size());
		
		
		if(indexToTrim >= previousPath.getNodeList().size()) indexToTrim = previousPath.getNodeList().size() -2;
		if(indexToTrim == 0) indexToTrim++;

//		if(previousPath.getNodeList().get(indexToTrim).getAdjacentNodesAsList().size()>=2)
//		{
		GraphNode nodeToRemove = previousPath.getNodeList().get(indexToTrim);
		
		
		removeEdgesInvolvingAvoid(nodeToRemove);
		
		
		return true;
	}

	public void removeEdgesInvolvingAvoid(GraphNode nodeToAvoid)
	{
//		if(nodeCache.contains(nodeToAvoid)) {
//			nodeCache.add(nodeToAvoid);
//		}
		for(GraphNode nodeToRemoveEdgesFrom : graphOfAllNodes) { //get each node in the overall graph
				//prepare an iterator over that nodes hashmap of noe/edge pairs
				Iterator<GraphNode> iterator = nodeToRemoveEdgesFrom.getAdjacentNodes().keySet().iterator();
				while(iterator.hasNext())	//while there are still entries to compare
				{
					GraphNode adjacentNode = iterator.next(); //the node we are checking is the next key
					if(adjacentNode.equals(nodeToAvoid)) //if the key we check is the node to avoid
					{
						iterator.remove();	//remove that key...and therefore edge from that hashmap
					}
				}
			}	
	}
	

	
	public Graph removeNodesFromGraph(ArrayList<GraphNode> nodesToAvoid)
	{
		for(GraphNode nodeToAvoid: nodesToAvoid) {
			for(int x =0; x<graphOfAllNodes.size();x++)
			{
				if(graphOfAllNodes.get(x).equals(nodeToAvoid)) {
					graphOfAllNodes.remove(nodeToAvoid);
				}
			}
		}	
		return this;
	}
	
/******************* Getters and Setters ***********************/
	
	public ArrayList<Edge> getListOfAllEdges() {
		return listOfAllEdges;
	}

	public void setListOfAllEdges(ArrayList<Edge> listOfAllEdges) {
		this.listOfAllEdges = listOfAllEdges;
	}

	public ArrayList<GraphNode> getGraphOfAllNodes() {
		return graphOfAllNodes;
	}

	public void setGraphOfAllNodes(ArrayList<GraphNode> graphOfAllNodes) {
		this.graphOfAllNodes = graphOfAllNodes;
	}

	@Override
	public String toString() {
		return "Graph [graphOfAllNodes=" + graphOfAllNodes + "]";
	}
	

}
