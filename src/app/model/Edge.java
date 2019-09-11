package app.model;
/**
 * Date - 22/3/19
 * @author Austin Heraughty
 * @author James Richardson
 * 
 * The Edge Class represents a connection between two adjacent nodes on a given map. 
 * It holds double values representing the distance of travel between nodes, difficulty of traversal 
 * of that path and danger inherent in that same journey
 *
 */
public class Edge {

	private GraphNode origin;
	private GraphNode destination;

	private int distance;  //length of path
	private double danger;		//danger rating of this path
	private double difficulty; 	//difficulty of terrain
	
	
	public Edge(int distance, double danger, double difficulty)
	{
		this.distance = distance;
		this.danger = danger;
		this.difficulty = difficulty;
	}
	
	public Edge(int distance, double danger,double difficulty,GraphNode origin,GraphNode destination)
	{
		this.distance=distance;
		this.danger = danger;
		this.difficulty=difficulty;
		this.origin =origin;
		this.destination=destination;	
	}
	
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

	public int getDistance() {
		return distance;
	}
	public double getDanger() {
		return danger;
	}
	public double getDifficulty() {
		return difficulty;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public void setDanger(double danger) {
		this.danger = danger;
	}
	public void setDifficulty(double difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * cost
	 * @return Returns the calculated value of the requested variable to compare paths by, eg. Distance, Danger, Difficulty
	 */
	public int getCost(int selection) {
		if(selection==1) {
		return (int) this.getDanger()*this.getDistance();
		}
		else if(selection==2)
		{
			return (int) this.getDifficulty()*this.getDistance();
		}
		else
			return this.getDistance();
	}
	

	
}
