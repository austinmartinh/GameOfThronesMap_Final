package app.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.model.Edge;
import app.model.Graph;
import app.model.GraphNode;

public class CSVFileIO {

	String lineOfCSV = "";
	String seperator = ",";
	

	/**
	 * Takes a CSV file containing a list of node data, then generates an arraylist list of nodes
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public ArrayList<GraphNode> readNodesFromFile(File csvFile) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		ArrayList<GraphNode> nodes = new ArrayList<>();
		
		while ((lineOfCSV = reader.readLine()) != null) 
		{
			
			String[] nodeData = lineOfCSV.split(seperator);
			GraphNode newNode = new GraphNode(nodeData[0], Integer.parseInt(nodeData[1]), Integer.parseInt(nodeData[2]));
			
			nodes.add(newNode);
		}
		reader.close();
		return nodes;
	}

	
	/**
	 * Read a CSV file of edge data and adds the edges to a graph of nodes. 
	 * It assign each edge to the adjacency list of each node
	 * 
	 * @Todo Does not yet assign to both ends of the edge
	 * @param filePath
	 * @throws IOException 
	 * @return graph A 
	 */
	public ArrayList<GraphNode> readEdgesToGraph(ArrayList<GraphNode> nodes,File csvFile) throws IOException
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
			
		while ( (lineOfCSV = reader.readLine()) != null ) 
		{
			
			String[] csvData= lineOfCSV.split(seperator);
			GraphNode origin = null;
			GraphNode destination = null;
		
			for(GraphNode current : nodes)
			{
				if(current.getName().equals(csvData[0]))
				{
					origin = current;
				}
			}
			for(GraphNode current : nodes)
			{
				if(current.getName().equals(csvData[1]))
				{
					destination = current;
				}
			}
			
			Edge newLink = new Edge(Integer.parseInt(csvData[2]),Double.parseDouble(csvData[3]),Double.parseDouble(csvData[4]));
			origin.getAdjacentNodes().put(destination,newLink);
			
		}
		reader.close();
		return nodes;
	}
	/** 
	 * This method reads the edgedata file and generates an arraylist of all edges
	 * It does not actually assign the edges to the adjacency lists of each node
	 * @param csvFile
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Edge> readEdgesToEdgeList(Graph graph,File csvFile) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		ArrayList<Edge> listOfAllEdges = new ArrayList<Edge>();
		while ( (lineOfCSV = reader.readLine()) != null ) 
		{
			
			String[] csvData= lineOfCSV.split(seperator);	
			
			GraphNode origin = null;
			GraphNode destination = null;
			while(origin == null)
			{
				for(GraphNode node: graph.getGraphOfAllNodes())
				{
					if(node.getName().equals(csvData[0]))
					{
						origin=node;
					}
				}
			}
			while(destination == null)
			{
				for(GraphNode node: graph.getGraphOfAllNodes())
				{
					if(node.getName().equals(csvData[1]))
					{
						destination=node;
					}
				}
			}
			Edge newLink = new Edge(Integer.parseInt(csvData[2]),Double.parseDouble(csvData[3]),Double.parseDouble(csvData[4]),origin,destination);
			listOfAllEdges.add(newLink);
			
		}
		reader.close();
		return listOfAllEdges;
	}
	
	/**
	 * Takes an absolute filepath for a CSV file containing a list of node data, then generates a list of nodes
	 * This is used to generae a list of all nodes without adjacencies
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public ArrayList<GraphNode> readNodesFromFile(String filePath) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		ArrayList<GraphNode> nodes = new ArrayList<>();
		
		while ((lineOfCSV = reader.readLine()) != null) 
		{
			
			String[] nodeData = lineOfCSV.split(seperator);
			GraphNode newNode = new GraphNode(nodeData[0], Integer.parseInt(nodeData[1]), Integer.parseInt(nodeData[2]));
			
			nodes.add(newNode);
		}
		reader.close();
		return nodes;
	}
	
	/**
	 * Read a CSV file of node names and edge data 
	 * @param filePath
	 * @throws IOException 
	 */
	public HashMap<GraphNode, HashMap<GraphNode,Edge>> readFileToEdgeList(ArrayList<GraphNode> allNodes, String filePath) throws IOException
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		HashMap<GraphNode,HashMap<GraphNode,Edge>> mapOfNodesToEdgeLists  = new HashMap<GraphNode,HashMap<GraphNode,Edge>>();
		HashMap<GraphNode,Edge> edgeListForNode= new HashMap<GraphNode,Edge>();
		
		while ((lineOfCSV = reader.readLine()) != null) 
		{
			
			String[] csvData= lineOfCSV.split(seperator);
			int positionOnLine = 0; 
			for(GraphNode current : allNodes)
			{
				if(current.getName() == csvData[positionOnLine])
				{
					GraphNode origin = current;
				}
				if(current.getName() == csvData[1])
				{
					GraphNode destination = current;
				}
			}
			
			Edge newLink = new Edge(Integer.parseInt(csvData[2]),Double.parseDouble(csvData[3]),Double.parseDouble(csvData[4]));
			
		}
		reader.close();
		return mapOfNodesToEdgeLists;
	}
	
	

	
	
	/**
	 * Take a list of nodes and filepath to write to and write the node data to a csv file at that location
	 * @param nodeList
	 * @param filePath
	 * @throws IOException
	 */
	public void writeNodeListToFile(ArrayList<GraphNode> nodeList, String filePath) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		
		for(GraphNode nodeToWrite: nodeList)
		{
			writer.write(nodeToWrite.getName() + "," + nodeToWrite.getxCoord() + "," + nodeToWrite.getyCoord());
			writer.write(System.lineSeparator());
		}
		writer.flush();
		writer.close();	
	
	}
	
	

	

	
	
	
	

}
