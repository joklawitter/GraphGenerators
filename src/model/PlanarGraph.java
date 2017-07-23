package model;

import java.util.ArrayList;

/**
 * This class implements a planar graph with directed edges (two per actual
 * edge), and stored faces. {@link PlanarEdge}s support standard methods to
 * navigate like {@code getNextEdge(), getReversedEdge(), getLeftFace()} etc.
 *
 * @see PlanarVertex
 * @see PlanarEdge
 * @see PlanarFace
 * @author Jonathan Klawitter
 */
public class PlanarGraph {

	/** The vertices of this planar graph. */
	private ArrayList<PlanarVertex> vertices;

	/** The (left) edges of this planar graph. */
	private ArrayList<PlanarEdge> edges;

	/** The faces of this planar graph. */
	private ArrayList<PlanarFace> faces;

	/** Name of the graph. */
	private String name;

	/**
	 * Constructor setting vertices, edges and faces.
	 * 
	 * @param vertices
	 * @param edges
	 * @param faces
	 */
	public PlanarGraph(ArrayList<PlanarVertex> vertices, ArrayList<PlanarEdge> edges,
			ArrayList<PlanarFace> faces, String name) {
		super();
		this.vertices = vertices;
		this.edges = edges;
		this.faces = faces;
		this.setName(name);
	}

	// - - - GETTERS - - -
	/**
	 * @return number of vertices
	 */
	public int getN() {
		return this.vertices.size();
	}

	/**
	 * @return number of edges
	 */
	public int getM() {
		return this.edges.size();
	}

	/**
	 * @return number of faces
	 */
	public int getF() {
		return this.faces.size();
	}

	/**
	 * @return vertices of this graph
	 */
	public ArrayList<PlanarVertex> getVertices() {
		return vertices;
	}

	/**
	 * @return edges of this graph
	 */

	public ArrayList<PlanarEdge> getEdges() {
		return edges;
	}

	/**
	 * @return faces of this graph
	 */
	public ArrayList<PlanarFace> getFaces() {
		return faces;
	}

	/**
	 * @param edgeToAdd edge to add to list in graph
	 */
	public void addEdge(PlanarEdge edgeToAdd) {
		edges.add(edgeToAdd);
	}
	
	// - - - CHECKS, MISC - - -
	/**
	 * @return whether graph is triangulated
	 */
	public boolean isTriangulated() {
		for (PlanarFace planarFace : faces) {
			if (planarFace.getSize() != 3) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return whether graph is 3-regular
	 */
	public boolean isThreeRegular() {
		for (PlanarVertex planarVertex : vertices) {
			if (planarVertex.getDegree() != 3) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Graph is considered valid if all
	 * <ul>
	 * <li>vertices,</li>
	 * <li>edges and</li>
	 * <li>faces are valid,</li>
	 * <li>n - m + f = 2</li>
	 * </ul>
	 * 
	 * @return whether this vertex is valid
	 */
	public boolean isValid() {
		// vertices
		for (PlanarVertex planarVertex : vertices) {
			if (!planarVertex.isValid()) {
				System.out.println(planarVertex);
				return false;
			}
		}

		// edges
		for (PlanarEdge planarEdge : edges) {
			if (!planarEdge.isValid()) {
				System.out.println(planarEdge);
				return false;
			}
		}

		// faces
		for (PlanarFace planarFace : faces) {
			if (!planarFace.isValid()) {
				System.out.println(planarFace);
				return false;
			}
		}

		// Euler's formula
		if (vertices.size() - edges.size() + faces.size() != 2) {
			System.out.println("[n = " + vertices.size() + ", m = " + edges.size() + ", f = "
					+ faces.size() + "]");
			return false;
		}

		return true;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String properties = "Planar graph (" + name + ") [n = " + vertices.size() + ", m = "
				+ edges.size() + ", f = " + faces.size() + "] \n";
		String verticesStr = "vertices: \n";
		for (PlanarVertex planarVertex : this.vertices) {
			verticesStr += planarVertex.toString() + "\n";
		}
		String facesStr = "faces: \n";
		for (PlanarFace planarFace : this.faces) {
			facesStr += planarFace.toString() + "\n";
		}
		return properties + verticesStr + facesStr;
	}
}
