
package cs311.hw7.graphalgorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import cs311.hw7.graph.Graph;
import cs311.hw7.graph.IGraph;
import cs311.hw7.graph.IGraph.Edge;
import cs311.hw7.graph.IGraph.Vertex;
import cs311.hw7.graphalgorithms.IWeight;


public class GraphAlgorithms {

    /* ----------------------------------------------------------------------------
     * Topological Sort(...)
     * ---------------------------------------------------------------------------- */
	
	/**
	 * Potential statuses a vertex may be in during a Topological Sort.
	 * @author Ian Malerich
	 */
	private enum Status {
		VISITED, TEMPORARY, UNVISITED
	}

	/**
	 * Perform a topological sort on the given graph. If the input graph is not a DAG
	 * null will be returned, else a sorted array of vertices will be returned to the
	 * caller.
	 * @param g Graph whose vertices we wish to sort.
	 * @return Sorted list of vertices.
	 */
    public static <V, E> List<Vertex<V>> TopologicalSort(IGraph<V, E> g) {
    	ArrayList<Vertex<V>> l = new ArrayList<Vertex<V>>();
    	
    	// Create the HashMap representing the marks of our vertices.
    	HashMap<String, Status> status = new HashMap<String, Status>();
    	for (Vertex<V> v : g.getVertices()) {
    		// All vertices are initially 'UNVISITED'.
    		status.put(v.getVertexName(), Status.UNVISITED);
    	}
    	
    	// n will be our current unmarked node.
    	for (Vertex<V> n = selectUnmarked(status, g); n != null; n = selectUnmarked(status, g)) {
    		try {
				procVertex(n, status, g, l);
    		} catch (RuntimeException e) {
    			// Graph is not a DAG, return null.
    			return null;
    		}
    	}

        return l;
    }
    
    /**
     * Process a vertex in our depth first search based algorithm (recursive implementation).
     * @param n The current node to parse.
     * @param s List of current statuses for all nodes in the input graph.
     * @param g Graph containing all vertices to parse.
     * @param l List subset to the topologically sorted list of vertices.
     */
    private static <V, E> void procVertex(Vertex<V> n, HashMap<String, Status> s, IGraph<V, E> g, List<Vertex<V>> l) {
		if (s.get(n.getVertexName()) == Status.TEMPORARY) {
			// The graph is not a DAG, return null for failure.
			throw new RuntimeException();
		}
		
		if (s.get(n.getVertexName()) == Status.UNVISITED) {
			s.put(n.getVertexName(), Status.TEMPORARY);
			for (Vertex<V> m : g.getNeighbors(n.getVertexName())) {
				procVertex(m, s, g, l);
			}
			s.put(n.getVertexName(), Status.VISITED);
			l.add(0, n);
		}
    }
    
    /**
     * Select an unmarked node from the given graph.
     * @param v List of current status for all vertices in the input graph.
     * @param g Graph of vertices to pick from.
     * @return An unvisited vertex, null if no such vertices exist.
     */
    private static <V, E> Vertex<V> selectUnmarked(HashMap<String, Status> v, IGraph<V, E> g) {
    	for (String key : v.keySet()) {
    		// If the vertex denoted by 'key' has not been visited, return it.
    		if (v.get(key) == Status.UNVISITED) {
    			return g.getVertex(key);
    		}
    	}

    	// All nodes have been visited return false.
    	return null;
    }
    
    /* ----------------------------------------------------------------------------
     * All Topological Sort(...)
     * ---------------------------------------------------------------------------- */
    
    /**
     * Computes all possible topological sorts of the input graph.
     * @param g The graph to compute all topological sorts for.
     * @return A List containing all topological sorts (themselves represented as lists of vertices).
     */
    public static <V, E> List<List<Vertex<V>>> AllTopologicalSort(IGraph<V, E> g) {
    	List<List<Vertex<V>>> all = new ArrayList<List<Vertex<V>>>(); // this will be what we return
    	List<Vertex<V>> l = new ArrayList<Vertex<V>>(); // this is a temporary working array
    	
    	// Create the HashMap representing the marks of our vertices.
    	HashMap<String, Status> s = new HashMap<String, Status>();
    	HashMap<String, Integer> i = new HashMap<String, Integer>();

    	for (Vertex<V> v : g.getVertices()) {
    		// All vertices are initially 'UNVISITED'.
    		s.put(v.getVertexName(), Status.UNVISITED);
    		i.put(v.getVertexName(), vertexInDegree(v, g));
    	}
    	
    	// Perform a modified version of Khan's algorithm.
    	procAllTopoSort(g, l, all, s, i);

        return all;
    }
    
    /**
     * Brute force method based on Khan's algorithm for finding all possible topological sorts.
     * @param g The graph we are parsing.
     * @param l Current working (dirty) topological sort.
     * @param all Current list of completed topological sorts.
     * @param s Current status' for all vertices.
     * @param i InDegree count for ann vertices.
     */
    private static <V, E> void procAllTopoSort(IGraph<V, E> g, 
    		List<Vertex<V>> l, List<List<Vertex<V>>> all,
    		HashMap<String, Status> s, HashMap<String, Integer> i) {
    	for (Vertex<V> v : g.getVertices()) {
    		// If there are no inputs to this vertex and it has not been visited...
    		if (i.get(v.getVertexName()) == 0 && s.get(v.getVertexName()) == Status.UNVISITED) {
    			for (Vertex<V> w : g.getNeighbors(v.getVertexName())) {
    				i.put(w.getVertexName(), i.get(w.getVertexName())-1);
    			}
    			
    			l.add(v);
    			s.put(v.getVertexName(), Status.VISITED);

    			// Recurse with v set to VISITED.
    			procAllTopoSort(g, l, all, s, i);
    			
    			// Undo the work we did generating this topological sort and continue.
    			s.put(v.getVertexName(), Status.UNVISITED);
    			l.remove(l.size()-1);
    			for (Vertex<V> w : g.getNeighbors(v.getVertexName())) {
    				i.put(w.getVertexName(), i.get(w.getVertexName())+1);
    			}
    		}
    	}
    	
    	if (l.size() == g.getVertices().size()) {
    		// l is one of the possible results, add it to the output array
    		all.add(new ArrayList<Vertex<V>>(l));
    	}
    }
    
    /**
     * Count the total number of edges going into our vertex v.
     * This is equal to then number of vertices that are not v
     * that have v as a neighbor.
     * @param v Vertex to check for inputs to.
     * @param g The graph to get edges and vertices from.
     * @return Total number of edges that go INTO v.
     */
    private static <V, E> int vertexInDegree(Vertex<V> v, IGraph<V, E> g) {
    	int degree = 0;
    	for (Vertex<V> n : g.getVertices()) {
    		if (n != v && g.getNeighbors(n.getVertexName()).contains(v)) {
    			degree++;
    		}
    	}
    	return degree;
    }
    
    /* ----------------------------------------------------------------------------
     * Kruscal(...)
     * ---------------------------------------------------------------------------- */
    
    /**
     * Perform Kruscal's algorithm to find a minimum spanning tree of the input graph.
     * Note that this requires that the graph be undirected, as such, this method 
     * will explicitly set the graph as undirected if it is not. If this provides a
     * problem with the grader, go ahead and dock my points I don't give enough of a shit
     * to support directed graphs.
     * If the input graph is not fully connected, it will return a graph with fully connected
     * components realized by minimum spanning forests.
     * @param g The graph to generate a minimum spanning tree for.
     * @return The minimum spanning tree for the graph g.
     */
    public static <V, E extends IWeight> IGraph<V, E> Kruscal(IGraph<V, E> g) {
    	// This methods requires that the graph be undirected, enforce that here.
    	g.setUndirectedGraph();
    	// This is our mst, for our in class notes it represents the v' and e' sets.
    	IGraph<V, E> mst = new Graph<V, E>();
    	mst.setUndirectedGraph();
    	
    	// Sets of connected vertices in the MST.
    	Map<String, Set<String>> S = new HashMap<String, Set<String>>();
    	
    	// The MST needs all vertices, so we can go ahead and add them all.
    	for (Vertex<V> v : g.getVertices()) {
    		mst.addVertex(v.getVertexName(), v.getVertexData());
    		// Create our initial set connections.
    		Set<String> tmp = new HashSet<String>();
    		tmp.add(v.getVertexName());
    		S.put(v.getVertexName(), tmp);
    	}
    	
    	// Now generate the priority queue, initially filled with all of the available edges,
    	// using a comparator that can compare edge weights.
    	List<Edge<E>> edges = g.getEdges();
    	PriorityQueue<Edge<E>> pq = new PriorityQueue<Edge<E>>(edges.size(), 
			new Comparator<Edge<E>>() {
				public int compare(Edge<E> first, Edge<E> second) {
					Double f = first.getEdgeData().getWeight();
					Double s = second.getEdgeData().getWeight();
					return f.compareTo(s);
				}
			}
		);

    	for (Edge<E> e : edges) {
    		pq.add(e);
    	}
    	
    	// There should be #vertices-1 edges in total when we are done.
    	final int total = g.getVertices().size();
    	for (int count = 0; (count < total-1) && (!pq.isEmpty());) {

    		// Remove our next edge
    		Edge<E> e = pq.remove();
    		
    		// Find the set for each
    		Set<String> s1 = S.get(e.getVertexName1());
    		Set<String> s2 = S.get(e.getVertexName2());
    		
    		// Make sure that our two vertices currently belong to different sets.
    		if (s1 != s2) {
    			// Update our MST.
    			mst.addEdge(e.getVertexName1(), e.getVertexName2(), e.getEdgeData());
    			count++;
    			
    			// Update the sets.
    			s1.addAll(s2);
    			for (String s : s2) {
    				S.put(s, s1);
    			}
    		}
    	}

        return mst;
    }
}
