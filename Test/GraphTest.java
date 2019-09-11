import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import app.model.Edge;
import app.model.Graph;
import app.model.GraphNode;
import app.model.Path;

import org.junit.jupiter.api.Test;

class GraphTest {


		GraphNode node1 = new GraphNode("1", 10,10);
		GraphNode node2= new GraphNode("2", 20,20);
		GraphNode node3= new GraphNode("3", 30,30);
		GraphNode node4= new GraphNode("4", 40,40);
		GraphNode node5= new GraphNode("5", 50,50);
		GraphNode node6 = new GraphNode("6", 60, 60);
		
		GraphNode[] testNodes = {node1,node2,node3,node4,node5,node6};
		
		Edge edgeA = new Edge(10,1.0,5.0,node1,node3);
		Edge edgeB = new Edge(11,2,10,node3,node2);
		Edge edgeC = new Edge(12,3,9,node2,node4);
		Edge edgeD = new Edge(13,4,8,node3,node5);
		Edge edgeE = new Edge(14,5,7,node1,node4);
		Edge edgeF = new Edge(15,6,5,node5,node2);
		
		Edge[] testEdges = {edgeA,edgeB,edgeC,edgeD,edgeE,edgeF};
		
		Graph testGraph = new Graph();
		
		

		@Test
		public void testAddNodeToGraph()
		{
			testGraph.getGraphOfAllNodes().add(node1);
			assertTrue(testGraph.getGraphOfAllNodes().contains(node1));
		}
		
		@Test
		public void testAssignEdges()
		{
			for(int x=0;x<6;x++)
			{
				testGraph.getGraphOfAllNodes().add(testNodes[x]);
				testGraph.getListOfAllEdges().add(testEdges[x]);
			}
			testGraph.assignEdgesToNodes();
			
			assertTrue(testGraph.getGraphOfAllNodes().get(1).getAdjacentNodesAsList().contains(node3));
		}
		
		@Test
		public void testGraphNodeGetAdjNodesAsList()
		{
			for(int x=0;x<6;x++)
			{
				testGraph.getGraphOfAllNodes().add(testNodes[x]);
				testGraph.getListOfAllEdges().add(testEdges[x]);
			}
			testGraph.assignEdgesToNodes();
			
		
			assertTrue(testGraph.getGraphOfAllNodes().get(0).getAdjacentNodesAsList().contains(node3));
		}
		
		@Test
		public void testGraphNodeGetEdgesAsListContainsEdges()
		{
			for(int x=0;x<6;x++)
			{
				testGraph.getGraphOfAllNodes().add(testNodes[x]);
				testGraph.getListOfAllEdges().add(testEdges[x]);
			}
			testGraph.assignEdgesToNodes();

			assertTrue(testGraph.getGraphOfAllNodes().get(0).getEdgesAsList().contains(edgeA));
			
			
		}

		@Test
		public void testGraphNodeGetEdgesAsListContainsNoExtraEdges()
		{
			for(int x=0;x<6;x++)
			{
				testGraph.getGraphOfAllNodes().add(testNodes[x]);
				testGraph.getListOfAllEdges().add(testEdges[x]);
			}
			testGraph.assignEdgesToNodes();

			assertFalse(testGraph.getGraphOfAllNodes().get(0).getEdgesAsList().contains(edgeF));
			
		}

		
		@Test
		public void testEdgeReturnsCorrectDistance()
		{
			assertEquals(11,edgeB.getCost(0));
		}

		@Test
		public void testEdgeReturnsCorrectDanger()
		{
			assertEquals(22,edgeB.getCost(1));	
		}
		
		@Test
		public void testEdgeReturnsCorrectDifficulty()
		{
			assertEquals(110,edgeB.getCost(2));
		}
	}
