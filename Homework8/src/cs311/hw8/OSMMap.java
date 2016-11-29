package cs311.hw8;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cs311.hw8.graph.Graph;
import cs311.hw8.graph.IGraph;

public class OSMMap {
	
	/**
	 * Graph which will represent the map data.
	 * This will be generated be the LoadMap(...) method of this class.
	 */
	private IGraph<Location, Street> g;
	
	public static void main(String[] args) {
		// Load the provided map of Ames and output the approximate total distance.
		OSMMap ames = new OSMMap();
		ames.LoadMap("AmesMap.txt");
		System.out.println(ames.TotalDistance());
	}
	
	public OSMMap() {
		// Initialize to an empty graph.
		g = new Graph<Location, Street>();
	}

    /* ----------------------------------------------------------------------------
     * Required Public Methods.
     * ---------------------------------------------------------------------------- */
	
	/**
	 * Loads all the data for the given file provided.
	 * If this map already has data loaded, that data will be overridden.
	 * @param filename The file we wish to load data from.
	 */
	public void LoadMap(String filename) {
		// Wipe all current graph data.
		g = new Graph<Location, Street>();

		try {
			File f = new File(filename);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			doc.normalize();

			// Read each node representing a vertex in our graph.
			NodeList nodes = doc.getElementsByTagName("node");
			int numnodes = nodes.getLength();
			for (int i=0; i<numnodes; i++) {
				Element e = (Element)nodes.item(i);
				String id = e.getAttribute("id");
				String lat = e.getAttribute("lat");
				String lon = e.getAttribute("lon");
				g.addVertex(id, new Location(lat, lon));
			}
			
			// Read each node representing an edge in our graph.
			NodeList edges = doc.getElementsByTagName("way");
			int numedges = edges.getLength();
			for (int i=0; i<numedges; i++) {
				Element e = (Element)edges.item(i);
				
				// We will need to find these properties for each edge.
				boolean oneway = false;
				boolean highway = false; // highway attribute must be present but not stored in the graph
				String name = null; // the name attribute is also required but will be stored in the graph

				// First browse the Tags to find the state of one way and the name.
				NodeList tags = e.getElementsByTagName("tag");
				int numtags = tags.getLength();
				for (int k=0; k<numtags; k++) {
					Element tag = (Element)tags.item(k);
					if (tag.getAttribute("k").equals("oneway") && tag.getAttribute("v").equals("yes")) {
						oneway = true;
					}
					if (tag.getAttribute("k").equals("name")) {
						name = tag.getAttribute("v");
					}
					if (tag.getAttribute("k").equals("highway")) {
						highway = true;
					}
				}
				
				// Highway and Name attributes are required for a street to be added to our graph.
				if (!highway || name == null) {
					continue;
				}
			
				// Next find the list of vertices that define the edges making up this street.
				NodeList verts = e.getElementsByTagName("nd");
				int numverts = verts.getLength();
				Element prev = null;
				for (int k=0; k<numverts; k++) {
					Element cur = (Element)verts.item(k);
					// If we have two vertices available to us, we can now add the edge.
					if (prev != null) {
						String v1 = prev.getAttribute("ref");
						String v2 = cur.getAttribute("ref");
						Street s = new Street(name, GetDistance(v1, v2));
						g.addEdge(v1, v2, s);

						// If this street is NOT a one way street, add the reverse edge as well.
						if (!oneway) {
							g.addEdge(v2, v1, s);
						}
					}

					// Update the previous vertex reference for the next iteration.
					prev = cur;
				}
			}

		// Only catch exception from the Document parser, allow exceptions from the Graph class to fall through.
		} catch (SAXException e) {
			System.err.print("SAXException");
		} catch (IOException e) {
			System.err.println("IOException");
		} catch (ParserConfigurationException e) {
			System.err.println("ParserConfigurationException");
		}
	}
	
	/**
	 * Approximates the total distance for the current state of this map.
	 * This adds the length of all edges in the graph (representing this map)
	 * and dividing the total length by 2 to account for the fact that most roads
	 * travel in both directions.
	 * @return Approximate total distance of roads for the current state of the map.
	 */
	public double TotalDistance() {
		// Keep a running sum of all streets.
		double dist = 0.0;
		
		// Add up the length of each edge.
		List<IGraph.Edge<Street>> edges = g.getEdges();
		for (IGraph.Edge<Street> e : edges) {
			dist += e.getEdgeData().length;
		}

		// Divide the distance by two to disregard bi-directional roads.
		return dist / 2.0;
	}

    /* ----------------------------------------------------------------------------
     * Private Utilities.
     * ---------------------------------------------------------------------------- */
	
	/**
	 * Given the name of two nodes in the graph 'g' for this map, compute the distance in miles
	 * between those two vertices.
	 * @param v1 The unique identifier representing the first vertex.
	 * @param v2 The unique identifier representing the second vertex.
	 * @return Distance in miles between vertex v1 and vertex v2.
	 */
	private double GetDistance(String v1, String v2) {
		Location l1 = g.getVertexData(v1);
		Location l2 = g.getVertexData(v2);
		
		// Compute the distance using the Haversine formula.
		// https://en.wikipedia.org/wiki/Haversine_formula
		// Based on the answer posted here provided on stackoverflow below...
		// http://stackoverflow.com/questions/120283/how-can-i-measure-distance-and-create-a-bounding-box-based-on-two-latitudelongi#12330

		final double RADIUS = 3958.75;
		double sinlat = Math.sin(Math.toRadians(l2.lat - l1.lat) / 2.0);
		double sinlon = Math.sin(Math.toRadians(l2.lon - l1.lon) / 2.0);
		double a = Math.pow(sinlat,  2) + Math.pow(sinlon, 2)
				   * Math.cos(Math.toRadians(l1.lat)) * Math.cos(Math.toRadians(l2.lat));
		double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return RADIUS * c;
	}
	
	/**
	 * Represents a Location object as a pair of latitude and longitude double values.
	 * This will serve as vertex data for the graph.
	 * @author Ian Malerich
	 */
	public class Location {
		/**
		 * Latitude of this location as a double value.
		 */
		public double lat;
		
		/**
		 * Longitude of this location as a double value.
		 */
		public double lon;
		
		public Location(double Lat, double Lon) {
			lat = Lat;
			lon = Lon;
		}
		
		public Location(String Lat, String Lon) {
			this(new Double(Lat).doubleValue(), new Double(Lon).doubleValue());
		}
	}
	
	/**
	 * Represents a Streen on the map as a street name and the length of the street.
	 * This will serve as edge data for the graph.
	 * @author Ian Malerich
	 */
	public class Street {
		/**
		 * The name of this street.
		 */
		public String name;

		/**
		 * The length of this street.
		 */
		public double length;
		
		public Street(String Name, double Length) {
			name = Name;
			length = Length;
		}
		
		public Street(String Name, String Length) {
			this(Name, new Double(Length).doubleValue());
		}
	}
}
