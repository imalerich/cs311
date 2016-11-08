package cs311.hw7.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph<V, E> implements IGraph<V, E> {
	
	/**
	 * True if this graph is currently in directed mode, False otherwise.
	 */
	private boolean directed;

	/**
	 * List of vertices given by vertex name keys.
	 */
	private HashMap<String, IGraph.Vertex<V>> vertices;
	
	/**
	 * Uses the from vertex name as a key into a list of edges extending from that vertex to another vertex.
	 */
	private HashMap<String, Set<IGraph.Edge<E>>> edges;
	
	/**
	 * Contains the same data as the edges array, except in reverse order.
	 * Thus the keys are denoted by the vertex2 name of an edge, and the edges
	 * extend from that vertex. 
	 * This will ONLY be used when directed is set to FALSE.
	 * This will make some computations easier when using this as an undirected graph.
	 * NOTE: As this is only used for the getNeighbors call which only returns vertices,
	 * we do not actually need to store edge data in this set, all data of this set 
	 * will be null to reflect that!!!
	 */
	private HashMap<String, Set<IGraph.Edge<E>>> inverseEdges;
	
	/**
	 * Constructs a new empty directed graph.
	 */
	Graph() {
		this(true);
	}
	
	/**
	 * Constructs a new empty graph.
	 * @param Directed If true the resulting graph will be directed, if false it will be undirected.
	 */
	Graph(boolean Directed) {
		vertices = new HashMap<String, IGraph.Vertex<V>>();
		edges = new HashMap<String, Set<IGraph.Edge<E>>>();
		inverseEdges = new HashMap<String, Set<IGraph.Edge<E>>>();
		directed = Directed;
	}

	@Override
	public void setDirectedGraph() {
		directed = true;
	}

	@Override
	public void setUndirectedGraph() {
		directed = false;
	}

	@Override
	public boolean isDirectedGraph() {
		return directed;
	}

	@Override
	public void addVertex(String vertexName) throws DuplicateVertexException {
		addVertex(vertexName, null);
	}

	@Override
	public void addVertex(String vertexName, V vertexData) throws DuplicateVertexException {
		if (vertices.containsKey(vertexName)) {
			throw new DuplicateVertexException();
		}
		
		// Add our vertex as well as a new set of edges extending from that vertex.
		vertices.put(vertexName, new IGraph.Vertex<V>(vertexName, vertexData));
		edges.put(vertexName, new HashSet<IGraph.Edge<E>>());
		inverseEdges.put(vertexName, new HashSet<IGraph.Edge<E>>());
	}

	@Override
	public void addEdge(String vertex1, String vertex2) throws DuplicateEdgeException, NoSuchVertexException {
		addEdge(vertex1, vertex2, null);
	}

	@Override
	public void addEdge(String vertex1, String vertex2, E edgeData)
			throws DuplicateEdgeException, NoSuchVertexException {
		// First we need to make sure each of our vertices exists.
		if (!vertices.containsKey(vertex1) || !vertices.containsKey(vertex2)) {
			throw new NoSuchVertexException();

		}

		// Get the set of edges extending from vertex1, this set must exist because vertex1 exists.
		Set<IGraph.Edge<E>> s = edges.get(vertex1);
		
		// Create the edge we are going to add.
		IGraph.Edge<E> e = new IGraph.Edge<E>(vertex1, vertex2, edgeData);
		
		// Make sure it has not yet already been added.
		if (s.contains(e)) {
			throw new DuplicateEdgeException();
		}
		
		// ... else we are free to add the edge.
		s.add(e);
		
		// ... also add it to our inverse set for use when our graph is not directed.
		Set<IGraph.Edge<E>> inverse = inverseEdges.get(vertex2);
		inverse.add(new IGraph.Edge<E>(vertex2, vertex1, null));
	}

	@Override
	public V getVertexData(String vertexName) throws NoSuchVertexException {
		if (!vertices.containsKey(vertexName)) {
			throw new NoSuchVertexException();
		}

		return vertices.get(vertexName).getVertexData();
	}

	@Override
	public void setVertexData(String vertexName, V vertexData) throws NoSuchVertexException {
		if (!vertices.containsKey(vertexName)) {
			throw new NoSuchVertexException();
		}

		// Need to overwrite the old vertex with the new data, but don't touch the edge
		// data as you would with the addVertex method.
		vertices.put(vertexName, new IGraph.Vertex<V>(vertexName, vertexData));
	}

	@Override
	public E getEdgeData(String vertex1, String vertex2) throws NoSuchVertexException, NoSuchEdgeException {
		// This will throw NoSuchVertexException for us if it does not exist.
		IGraph.Edge<E> e = getEdge(vertex1, vertex2);
		
		// Make sure we have a valid edge to work with.
		if (e == null) {
			throw new NoSuchEdgeException();
		}

		return e.getEdgeData();
	}

	@Override
	public void setEdgeData(String vertex1, String vertex2, E edgeData) throws NoSuchVertexException, NoSuchEdgeException {
		// This will throw NoSuchVertexException for us if it does not exist.
		IGraph.Edge<E> e = getEdge(vertex1, vertex2);
		
		// Make sure we have a valid edge to work with.
		if (e == null) {
			throw new NoSuchEdgeException();
		}
		
		// We found the edge, now we need to replace it with an edge containing the correct edge data.
		edges.get(vertex1).add(new IGraph.Edge<E>(vertex1, vertex2, edgeData));
	}

	@Override
	public IGraph.Vertex<V> getVertex(String VertexName) {
		if (!vertices.containsKey(VertexName)) {
			throw new NoSuchVertexException();
		}

		return vertices.get(VertexName);
	}

	@Override
	public IGraph.Edge<E> getEdge(String vertexName1, String vertexName2) {
		// First we need to make sure each of our vertices exists.
		if (!vertices.containsKey(vertexName1) || !vertices.containsKey(vertexName2)) {
			throw new NoSuchVertexException();
		}

		// Get the set of edges extending from vertex1, this set must exist because vertex1 exists.
		Set<IGraph.Edge<E>> s = edges.get(vertexName1);

		// Loop through all edges in the set, we only need to check vertex2 as vertex1
		// is guaranteed to be the same by the creation of the set s.
		for (IGraph.Edge<E> e : s) {
			if (e.getVertexName2().equals(vertexName2)) {
				// We found the edge, return it.
				return e;
			}
		}

		return null;
	}

	@Override
	public List<IGraph.Vertex<V>> getVertices() {
		return new ArrayList<IGraph.Vertex<V>>(vertices.values());
	}

	@Override
	public List<IGraph.Edge<E>> getEdges() {
		ArrayList<IGraph.Edge<E>> ret = new ArrayList<IGraph.Edge<E>>();
	
		// Iterate over all sets of edges.
		for (Set<IGraph.Edge<E>> s : edges.values()) {
			// Add all of the edges in this set to our returned List.
			ret.addAll(s);
		}
		
		return ret;
	}

	@Override
	public List<IGraph.Vertex<V>> getNeighbors(String vertex) {
		// First we need to make sure each of our vertices exists.
		if (!vertices.containsKey(vertex)) {
			throw new NoSuchVertexException();
		}

		// This will be the array that will contain  all neighbor vertices of the input vertex.
		ArrayList<IGraph.Vertex<V>> ret = new ArrayList<IGraph.Vertex<V>>();

		// Get the set of edges extending from the input vertex.
		Set<IGraph.Edge<E>> s = edges.get(vertex);
		for (IGraph.Edge<E> e : s) {
			ret.add(getVertex(e.getVertexName2()));
		}
		
		// If this is an undirected graph, include the set of vertices extending from
		// another vertex, into this vertex.
		if (!directed) {
			Set<IGraph.Edge<E>> i = inverseEdges.get(vertex);
			for (IGraph.Edge<E> e : i) {
				ret.add(getVertex(e.getVertexName2()));
			}
		}

		return ret;
	}
}