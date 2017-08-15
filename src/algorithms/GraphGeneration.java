package algorithms;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import model.Edge;
import model.Graph;
import model.PlanarGraph;
import model.Vertex;

public class GraphGeneration {

	/**
	 * Creates a graph without edges, called
	 * <a href = "https://en.wikipedia.org/wiki/Null_graph#Edgeless_graph">
	 * edgeless graph</a>.
	 * 
	 * @param numVertices
	 *            number of vertices the new edgeless graph with have
	 * @return an edgeless graph of given size
	 */
	public static Graph createEdgelessGraph(int numVertices) {
		if (numVertices < 0) {
			throw new IllegalArgumentException("Graph can't have negative number of vertices");
		}

		Graph edgelessGraph = new Graph(numVertices, 0, Graph.UNDIRECTED);
		edgelessGraph.setName("\bar{K_{" + numVertices + "}}");
		return edgelessGraph;
	}

	/**
	 * Creates a <a href = "https://en.wikipedia.org/wiki/Path_graph">path
	 * graph</a> of given length (in terms of vertices).
	 * 
	 * @param length
	 *            in terms of vertices of path
	 * @return path of given length
	 */
	public static Graph createPathGraph(int length) {
		if (length < 0) {
			throw new IllegalArgumentException("Path graph can't have negative length.");
		}

		Graph pathGraph = new Graph(length, length, Graph.UNDIRECTED);
		pathGraph.setName("P_{" + length + "}");
		if (length > 1) {
			for (int i = 0; i < length - 1; i++) {
				pathGraph.addEdge(i, i + 1);
			}
		}
		return pathGraph;

	}

	/**
	 * Creates a <a href = "https://en.wikipedia.org/wiki/Cycle_graph">cycle</a>
	 * with given number of vertices.
	 * 
	 * @param numVertices
	 *            number of vertices the new cycle will have
	 * @return cycle with given number of vertices
	 */
	public static Graph createCycle(int numVertices) {
		if (numVertices < 0) {
			throw new IllegalArgumentException("Graph can't have negative number of vertices");
		}

		Graph cycle = new Graph(numVertices, numVertices, Graph.UNDIRECTED);
		cycle.setName("C_{" + numVertices + "}");
		if (numVertices > 1) {
			for (int i = 0; i < numVertices - 1; i++) {
				cycle.addEdge(i, i + 1);
			}
		}
		if (numVertices > 2) {
			cycle.addEdge(0, numVertices - 1);
		}
		return cycle;
	}

	/**
	 * Creates a random tree by converting a randomly generated <a
	 * href=https://en.wikipedia.org/wiki/Pr%C3%BCfer_sequence>Pruefer
	 * sequence </a> into a tree.
	 * 
	 * @param numVertices
	 *            the number of vertices
	 * @param seed
	 *            for randomness
	 * @return a random tree generated with Pruefer sequence
	 */
	public static Graph createPrueferTree(int numVertices, int seed) {
		if (numVertices < 0) {
			throw new IllegalArgumentException("Graph can't have negative number of vertices");
		}

		Graph tree = new Graph(numVertices, Integer.max(numVertices - 1, 0), Graph.UNDIRECTED);
		tree.setName("PrueferTree_{" + numVertices + ", seed " + seed + "}");

		Random random = new Random(seed);
		// generate pruefer sequence
		int[] prueferSequence = RandomUtil.randomPermutation(numVertices - 2, random);

		// set degrees
		int[] degrees = new int[numVertices];
		for (int i = 0; i < degrees.length; i++) {
			degrees[i] = 1;
		}
		for (int i = 0; i < prueferSequence.length; i++) {
			degrees[prueferSequence[i]]++;
		}

		// add edges
		for (int i = 0; i < prueferSequence.length; i++) {
			for (int j = 0; j < degrees.length; j++) {
				if (degrees[j] == 1) {
					tree.addEdge(j, prueferSequence[i]);
					degrees[j]--;
					degrees[prueferSequence[i]]--;
					break;
				}
			}
		}

		// add last edge
		int u = -1;
		int v = -1;
		for (int i = 0; i < degrees.length; i++) {
			if (degrees[i] == 1) {
				if (u < 0) {
					// found first vertex
					u = i;
				} else {
					// found second vertex
					v = i;
					break;
				}
			}
		}
		tree.addEdge(u, v);

		return tree;
	}

	/**
	 * Creates a
	 * <a href = "https://en.wikipedia.org/wiki/Star_(graph_theory)">star</a>
	 * with given number of vertices.
	 * 
	 * @param numVertices
	 *            number of vertices the new star will have
	 * @return star with given number of vertices
	 */
	public static Graph createStar(int numVertices) {
		if (numVertices < 0) {
			throw new IllegalArgumentException("Graph can't have negative number of vertices");
		}

		Graph star = new Graph(numVertices, numVertices, Graph.UNDIRECTED);
		star.setName("S_{" + numVertices + "}");
		for (int i = 1; i < numVertices; i++) {
			star.addEdge(0, i);
		}
		return star;
	}

	/**
	 * Creates a random maximal
	 * <a href = "https://en.wikipedia.org/wiki/Outerplanar_graph">outerplanar
	 * graph</a> with given number of vertices.
	 * 
	 * @param numVertices
	 *            number of vertices the new graph will have
	 * @return a random maximal outerplanar graph
	 */
	public static Graph createMaxOuterplanarGraph(int numVertices) {
		return createMaxOuterplanarGraph(numVertices, RandomUtil.getRandom().nextInt());
	}

	/**
	 * Creates a random maximal
	 * <a href = "https://en.wikipedia.org/wiki/Outerplanar_graph">outerplanar
	 * graph</a> with given number of vertices.
	 * 
	 * @param numVertices
	 *            number of vertices the new graph will have
	 * @param seed
	 *            seed for randomness
	 * @return a random maximal outerplanar graph
	 */
	public static Graph createMaxOuterplanarGraph(int numVertices, int seed) {
		if (numVertices < 0) {
			throw new IllegalArgumentException("Graph can't have negative number of vertices");
		}

		int numEdges = Integer.max(0, 2 * numVertices - 3);
		Graph maxOuterplanarGraph = new Graph(numVertices, numEdges, Graph.UNDIRECTED);
		maxOuterplanarGraph.setName("MaxOuterplanar_{" + numVertices + "," + seed + "}");

		// cycle
		/*
		 * NOTE: createHamiltonianMaxPlanarGraph required that vertices create a
		 * cycle in the order of their id!
		 */
		if (numVertices > 1) {
			for (int i = 0; i < numVertices - 1; i++) {
				maxOuterplanarGraph.addEdge(i, i + 1);
			}
		}
		if (numVertices > 2) {
			maxOuterplanarGraph.addEdge(0, numVertices - 1);
		}

		// recursively add edges to inner faces
		if (numVertices > 3) {
			Random random = new Random(seed);
			addEdgeToInnerFace(maxOuterplanarGraph, 0, numVertices - 1, random);
		}

		return maxOuterplanarGraph;
	}

	/**
	 * Private helper method for
	 * {@link GraphGeneration#createMaxOuterplanarGraph(int, int)}. Given a
	 * split region from vertex begin to vertex end, it picks a random other
	 * vertex and adds the triangle (begin, pick, end). It then continues
	 * recursively, ending with a triangle.
	 */
	private static void addEdgeToInnerFace(Graph maxOuterplanarGraph, int begin, int end,
			Random random) {
		int length = end - begin + 1;

		if (length < 3) {
			// split was incorrect
			throw new IllegalStateException("Inner face to small in outerplnar graph generation.");
		} else if (length == 3) {
			// inner face is only triangle, stop
			return;
		} else {
			// 1. pick a vertex between begin and end
			// 2. add triangle begin-pick-end
			// case i) pick = begin + 1
			// case ii) pick = end - 1
			// case iii) else
			// 3. recursive call on begin-pick and pick-end

			// > 1.
			int pick = begin + 1 + random.nextInt(length - 2);
			if (pick == begin + 1) { // i)
				// > 2.
				maxOuterplanarGraph.addEdge(pick, end);
				// > 3.
				addEdgeToInnerFace(maxOuterplanarGraph, pick, end, random);
			} else if (pick == end - 1) { // ii)
				// > 2.
				maxOuterplanarGraph.addEdge(begin, pick);
				// > 3.
				addEdgeToInnerFace(maxOuterplanarGraph, begin, pick, random);
			} else { // iii)
				// > 2.
				maxOuterplanarGraph.addEdge(begin, pick);
				maxOuterplanarGraph.addEdge(pick, end);
				// > 3.
				addEdgeToInnerFace(maxOuterplanarGraph, begin, pick, random);
				addEdgeToInnerFace(maxOuterplanarGraph, pick, end, random);
			}
		}
	}

	/**
	 * Creates a random
	 * <a href = "https://en.wikipedia.org/wiki/Apollonian_network">Apollonian
	 * network</a> by iteratively adding a vertex to a randomly chosen inner
	 * triangle.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @param seed
	 *            seed for randomness
	 * @return a random Apollonian network.
	 */
	public static Graph createApollonianNetwork(int numVertices, int seed) {
		if (numVertices < 3) {
			throw new IllegalArgumentException(
					"An Apollonian network can't have less than 3 vertices,"
							+ "but parameter given was " + numVertices + ".");
		}

		return PlanarGraphGeneration.convertPlanarGraphToGraph(
				PlanarGraphGeneration.createApollonianNetwork(numVertices, seed));
	}

	/**
	 * Creates a random
	 * <a href = "https://en.wikipedia.org/wiki/Planar_graph">maximal planar
	 * graph</a>, i.e. a planar triangulation, by applying a given number of
	 * flips to a random Apollonian network (see
	 * {@link #createApollonianNetwork(int, int)}. A flip deletes an edge
	 * between two triangles and adds the other diagonal in the resulting
	 * quadrangle.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @param numFlips
	 *            number of flips
	 * @param seed
	 *            seed for randomness
	 * @return a random planar triangulation
	 */
	public static Graph createMaxPlanarGraph(int numVertices, int numFlips, int seed) {
		if (numVertices < 3) {
			throw new IllegalArgumentException("Planar graph should have at least 3 vertices,"
					+ "but parameter given was " + numVertices + ".");
		} else if (numVertices < 0) {
			throw new IllegalArgumentException("Number of flips can't be negtaive.");
		}

		return PlanarGraphGeneration.convertPlanarGraphToGraph(
				PlanarGraphGeneration.createMaxPlanarGraph(numVertices, numFlips, seed));
	}

	/**
	 * Creates a random Hamiltonian maximal planar graph with given number of
	 * vertices for given seed. Graph is based on merging two outerplanar
	 * graphs.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @param seed
	 *            seed for randomness
	 * @return a random Hamiltonian maximal planar graph
	 */
	public static Graph createHamiltonianMaxPlanarGraph(int numVertices, int seed) {
		if (numVertices < 3) {
			throw new IllegalArgumentException("Graph should have at least 3 vertices.");
		}

		Random random = new Random(seed);
		Graph firstOuterplanarGraph = createMaxOuterplanarGraph(numVertices, random.nextInt());
		Graph secondOuterplanarGraph = createMaxOuterplanarGraph(numVertices, random.nextInt());
		Graph hamiltonianPlanarGraph = new Graph(numVertices, 3 * numVertices - 6,
				Graph.UNDIRECTED);
		hamiltonianPlanarGraph.setName("hamiltonianMaxPlanar(" + numVertices + ", " + seed + ")");
		ArrayList<Edge> newDualEdges = new ArrayList<Edge>(numVertices / 2);

		// merge outerplanar graphs
		for (int i = 0; i < numVertices; i++) {

			// A: add edges of g1
			Vertex currentOfFirstGraph = firstOuterplanarGraph.getVertexById(i);
			HashSet<Integer> usedNeighbours = new HashSet<Integer>(
					currentOfFirstGraph.getNeighbors().length);
			for (Vertex neighbour : currentOfFirstGraph.getNeighbors()) {
				int neighbourID = neighbour.getId();
				if (neighbourID > i) {
					hamiltonianPlanarGraph.addEdge(i, neighbourID);
					usedNeighbours.add(neighbourID);
				}
			}

			// B. add edges of g2
			Vertex currentOfSecondGraph = secondOuterplanarGraph.getVertexById(i);
			neighborsLoop: for (Vertex neighbour : currentOfSecondGraph.getNeighbors()) {
				// special case edge from first to last vertex
				if ((i == 0) && (neighbour.getId() == numVertices - 1)) {
					continue;
				} else if (neighbour.getId() > i + 1) {
					int neighbourId = neighbour.getId();

					for (Edge e : newDualEdges) {
						if (e.getStartVertex().getId() == i
								&& e.getTargetVertex().getId() == neighbour.getId()) {
							continue neighborsLoop;
						}
					}

					if (!usedNeighbours.contains(neighbourId)) {
						// edge is not contained in g1
						hamiltonianPlanarGraph.addEdge(i, neighbourId);
					} else {
						// edge already exists, so add dual
						Edge dualEdge = findDualEdge(secondOuterplanarGraph, currentOfSecondGraph,
								neighbour);
						// method findDualEdge adds edge also to the
						// secondOuterplanarGraph
						newDualEdges.add(dualEdge);
						hamiltonianPlanarGraph.addEdge(dualEdge.getStartVertex().getId(),
								dualEdge.getTargetVertex().getId());
					}
				}
			}
		}

		if (hamiltonianPlanarGraph.getM() != 3 * numVertices - 6) {
			throw new IllegalStateException(
					"Subhamiltonian max planar graph construction has wrong number of edges: 3n-6 = "
							+ (3 * numVertices - 6) + ", m = " + hamiltonianPlanarGraph.getM());
		}

		return hamiltonianPlanarGraph;
	}

	/**
	 * Finds the dual edge of the edge represented by the given two adjacent
	 * vertices in the given outerplanar graph. This is a very specific helper
	 * method for {@link #createHamiltonianMaxPlanarGraph(int, int)} and should
	 * not be used for other purposes without great care.
	 * 
	 * The dual edge is also added to the given graph and the edge connecting u
	 * and v removed.
	 * 
	 * @param outerplanarGraph
	 *            outerplanar graph in which the two given vertices are adjacent
	 *            and in which the corresponding edge gets removed and its dual
	 *            edge added
	 * @param u
	 *            first Vertex of former edge, neighbour of v
	 * @param v
	 *            second Vertex of former edge, neighbor of u
	 * @return the dual of the edge connecting u and v
	 */
	private static Edge findDualEdge(Graph outerplanarGraph, Vertex u, Vertex v) {
		// situation: u and v are neighbours via edge e,
		// we want to find dual edge of e
		// therefore we search for their two common neighbours

		Vertex[] uNeighbours = u.getNeighbors();
		Arrays.sort(uNeighbours, (Vertex x, Vertex y) -> Integer.compare(x.getId(), y.getId()));
		Vertex[] vNeighbours = v.getNeighbors();
		Arrays.sort(vNeighbours, (Vertex x, Vertex y) -> Integer.compare(x.getId(), y.getId()));

		Vertex[] commonNeighbours = new Vertex[2];

		int uIndex = 0;
		int vIndex = 0;
		int index = 0;
		while (uIndex < uNeighbours.length && vIndex < vNeighbours.length) {
			if (uNeighbours[uIndex] == vNeighbours[vIndex]) {
				commonNeighbours[index++] = uNeighbours[uIndex];
				if (index == 2) {
					outerplanarGraph.removeEdge(outerplanarGraph.getEdgeByEndvertices(u, v));
					outerplanarGraph.addEdge(commonNeighbours[0].getId(),
							commonNeighbours[1].getId());
					return new Edge(commonNeighbours[0], commonNeighbours[1],
							Edge.NON_GRAPH_EDGE_ID);
				}
				uIndex++;
				vIndex++;
			} else {
				if (uNeighbours[uIndex].getId() < vNeighbours[vIndex].getId()) {
					uIndex++;
				} else {
					vIndex++;
				}
			}
		}

		throw new IllegalStateException("u and v do not have two common neighbours.");
	}

	/**
	 * Create a random
	 * <a href = "https://en.wikipedia.org/wiki/1-planar_graph">1-planar
	 * graph</a> based on a maximal planar graph (see
	 * {@link #createMaxPlanarGraph(int, int, int)}.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @param numFlips
	 *            number of flips
	 * @param seed
	 *            seed for randomness
	 * @return a random 1-planar graph
	 */
	public static Graph createMaxOnePlanarGraph(int numVertices, int numFlips, int seed) {
		if (numVertices < 3) {
			throw new IllegalArgumentException("1-planar graph should have at least 3 vertices,"
					+ "but parameter given was " + numVertices + ".");
		} else if (numVertices < 0) {
			throw new IllegalArgumentException("Number of flips can't be negtaive.");
		}

		Random random = new Random(seed);
		PlanarGraph planarGraph = PlanarGraphGeneration.createMaxPlanarGraph(numVertices, numFlips,
				random.nextInt());
		PlanarGraph onePlanarGraph = PlanarGraphGeneration.create1PlanarGraph(planarGraph,
				random.nextInt());
		return PlanarGraphGeneration.convertPlanarGraphToGraph(onePlanarGraph);
	}

	/**
	 * Creates a random k-planar graph with given number of vertices and for
	 * given k via the construction of a random point set, which is determined
	 * randomly by the given seed. 0-planar graphs are simply planar graphs. A
	 * <a href = "https://en.wikipedia.org/wiki/1-planar_graph">1-planar
	 * graphs</a> has a drawing in the plane such that each edge has at most 1
	 * crossing.<br>
	 * The method does not guarantee that the graph is not also (k-1)-planar or
	 * even less.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @param k
	 *            the number of allowed crossings per edge in a plane drawing
	 * @param seed
	 *            seed for randomness
	 * @return a random k-planar graph
	 */
	public static Graph createKPlanarGraph(int numVertices, int k, int seed) {
		Random random = new Random(seed);
		Point[] points = new Point[numVertices];
		for (int i = 0; i < numVertices; i++) {
			points[i] = new Point(random);
		}
		Arrays.sort(points);

		int roughlyNumEdges = (3 + k) * numVertices;
		Graph kPlanarGraph = new Graph(numVertices, (3 + k) * numVertices, Graph.UNDIRECTED);
		kPlanarGraph.setName(k + "-planar_{" + numVertices + "}");

		// add edges
		HashSet<Line2D.Double> lines = new HashSet<Line2D.Double>(roughlyNumEdges);
		for (int i = 1; i < numVertices; i++) {
			pointsLoop: for (int j = i - 1; j >= 0; j--) {
				Line2D.Double newLine = new Line2D.Double(points[j].x, points[j].y, points[i].x,
						points[i].y);

				int crossings = 0;
				for (Line2D.Double line : lines) {
					if ((line.x1 == newLine.x1 && line.y1 == newLine.y1)
							|| (line.x1 == newLine.x2 && line.y1 == newLine.y2)
							|| (line.x2 == newLine.x1 && line.y2 == newLine.y1)
							|| (line.x2 == newLine.x2 && line.y2 == newLine.y2)) {
						continue;
					}
					if (line.intersectsLine(newLine)) {
						crossings++;
						if (crossings > k) {
							continue pointsLoop;
						}
					}
				}
				if (crossings <= k) {
					kPlanarGraph.addEdge(i, j);
					lines.add(newLine);
				}
			}
		}

		return kPlanarGraph;
	}

	/**
	 * This class implements a simple point in 2D for the construction of
	 * k-planar graphs via
	 * {@link GraphGeneration#createKPlanarGraph(int, int, int)}.
	 */
	private static class Point implements Comparable<Point> {
		double x;
		double y;

		public Point(Random random) {
			this.x = random.nextDouble();
			this.y = random.nextDouble();
		}

		@Override
		public int compareTo(Point o) {
			if (o.x > this.x) {
				return -1;
			}
			if (o.x < this.x) {
				return 1;
			}
			if (o.y > this.y) {
				return -1;
			}
			if (o.y < this.y) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * Creates a <a href = "https://en.wikipedia.org/wiki/K-tree">k-tree</a> by
	 * iteratively adding a vertex and connecting it to a k-clique in the
	 * already constructed graph, thus forming a new k+1-clique.
	 * 
	 * @param numVertices
	 *            number of vertices the new graph will have
	 * @param k
	 *            parameter k of k-tree
	 * @param seed
	 *            seed for randomness
	 * @return a k-tree
	 */
	public static Graph createKTree(int numVertices, int k, int seed) {
		if (numVertices < 0) {
			throw new IllegalArgumentException("Graph can't have negative number of vertices");
		}
		if (k + 1 > numVertices) {
			throw new IllegalArgumentException(k + "-tree has to have at least " + k + "vertices");
		}

		Graph kTree = new Graph(numVertices, numVertices * k, Graph.UNDIRECTED);
		kTree.setName(k + "-tree_{" + numVertices + "," + seed + "}");

		Random random = new Random(seed);

		// get first (k+1)-clique
		for (int i = 0; i < k; i++) {
			for (int j = i + 1; j < k + 1; j++) {
				kTree.addEdge(i, j);
			}
		}

		// store k-cliques
		ArrayList<int[]> cliques = new ArrayList<int[]>(numVertices * k);
		for (int i = 0; i < k + 1; i++) {
			int[] clique = new int[k];
			int index = 0;
			for (int j = 0; j < k + 1; j++) {
				if (j != i) {
					clique[index++] = j;
				}
			}
			cliques.add(clique);
		}

		// add vertices
		int nextVertex = k + 1;
		while (nextVertex < numVertices) {
			// - select k-clique randomly
			int[] clique = cliques.get(random.nextInt(cliques.size()));

			// - add vertex and edges
			for (int i = 0; i < clique.length; i++) {
				kTree.addEdge(clique[i], nextVertex);
			}

			// - add new cliques
			for (int i = 0; i < k; i++) {
				int[] newClique = Arrays.copyOf(clique, clique.length);
				newClique[i] = nextVertex;
				cliques.add(newClique);
			}
			nextVertex++;
		}

		return kTree;
	}

	/**
	 * Creates the
	 * <a href = "https://en.wikipedia.org/wiki/Hypercube">hypercube</a> of
	 * given dimension.
	 * 
	 * @param dimension
	 *            the dimension of the hypercube, i.e. it has 2^dimension many
	 *            vertices
	 * @author Jonathan Klawitter, Michael Wegener
	 */
	public static Graph createHypercube(int dimension) {
		if (dimension < 0) {
			throw new IllegalArgumentException(
					"Hypercube can't have negative dimension, but parameter for dimension is "
							+ dimension);
		}

		int numberOfVertices = (int) Math.pow(2, dimension);
		int numberOfEdges = dimension * numberOfVertices / 2;
		Graph hypercube = new Graph(numberOfVertices, numberOfEdges, Graph.UNDIRECTED);
		hypercube.setName("Q_{" + dimension + "}");

		for (int u = 0; u < numberOfVertices; ++u) {
			for (int i = 0; i < dimension; ++i) {
				// u XOR 0001 / 0010 / 0100 / 1000
				int v = u ^ (int) Math.pow(2, i);
				if (u < v) {
					hypercube.addEdge(u, v);
				}
			}
		}

		return hypercube;
	}

	/**
	 * Creates the
	 * <a href = "https://en.wikipedia.org/wiki/Cube-connected_cycles">cube
	 * connected cycle</a> graph of dimension d > 1. This graph is a hypercube
	 * where each vertex gets replaced with a cycle of length d. A vertex (x,y)
	 * is then connected to (x,y+-1) and (x XOR 2^y,y). If the dimension is two,
	 * this yields a cycle of length 8.
	 * 
	 * @param dimension
	 *            dimension > 1 of the graph, i.e. dimension of the hypercube
	 *            and length of the cycles; for dimension 2 this is cycle of
	 *            length 8
	 * @return a cube connected cycle of given dimension
	 */
	public static Graph createCubeConnectedCycles(int dimension) {
		if (dimension < 2) {
			throw new IllegalArgumentException(
					"Cube connected cycles graph can't have dimension less than 2, "
							+ "but parameter for dimension is " + dimension + ".\n");
		}
		if (dimension == 2) {
			return createCycle(8).setName("CCC_{" + dimension + "}");
		}

		int numCycles = (int) Math.pow(2, dimension);
		int numberOfVertices = dimension * numCycles;
		int numberOfEdges = numberOfVertices + numberOfVertices / 2;
		Graph cubeConnectedCycles = new Graph(numberOfVertices, numberOfEdges, Graph.UNDIRECTED);
		cubeConnectedCycles.setName("CCC_{" + dimension + "}");
		Vertex[][] cycles = new Vertex[numCycles][dimension];

		int vertexCount = 0;
		for (int i = 0; i < cycles.length; i++) {
			for (int j = 0; j < cycles[0].length; j++) {
				cycles[i][j] = cubeConnectedCycles.getVertexById(vertexCount++);
			}
		}

		// create cycles
		for (int i = 0; i < cycles.length; i++) {
			for (int j = 0; j < cycles[0].length; j++) {
				cubeConnectedCycles.addEdge(cycles[i][j], cycles[i][(j + 1) % dimension]);
			}
		}

		// connect cycles
		for (int i = 0; i < cycles.length; i++) {
			for (int j = 0; j < cycles[0].length; j++) {
				int o = i ^ (int) Math.pow(2, j);
				if (o < i) {
					continue;
				} else {
					cubeConnectedCycles.addEdge(cycles[i][j], cycles[o][j]);
				}
			}
		}

		return cubeConnectedCycles;
	}

	/**
	 * Creates the k-ary n-cube. In such a graph vertices are strings of length
	 * n over the alphabet {0,..,k-1} with vertices adjacent if Hamming distance
	 * is 1. Another way to see these graphs is as the product of cycles C_dim.
	 * <br>
	 * n = 2: torus<br>
	 * k = 2: hypercube<br>
	 * 
	 * @param k
	 *            has to be > 2, for k = 2 use hypercube
	 * @param n
	 * @return k-ary n-cube
	 */
	public static Graph createKAryNCube(int k, int n) {
		if (k <= 2) {
			throw new IllegalArgumentException(
					"k-ary n-cube needs k larger than 2, for k = 2 use hypercube.");
		} else if (n < 2) {
			throw new IllegalArgumentException(
					"k-ary n-cube needs n larger than 1, for n = 1 use cycle.");
		}

		Graph cycle = createCycle(k);
		Graph[] cycles = new Graph[n];
		for (int i = 0; i < cycles.length; i++) {
			cycles[i] = cycle;
		}

		return GraphOperators.createCartesianGraphProduct(cycles)
				.setName(k + "-ary " + n + "-cube");
	}

	/**
	 * Creates the
	 * <a href = "https://en.wikipedia.org/wiki/Lattice_graph">square grid
	 * graph</a> of given width > 0 and length > 0. Width and length are both,
	 * as for paths, measured in number of vertices. Name of graph is
	 * "P_width x P_height".
	 * 
	 * @param width
	 *            number of vertices horizontally
	 * @param height
	 *            number of vertices vertically
	 * @return the square grid of given width and height
	 */
	public static Graph createSquareGrid(int width, int height) {
		if ((width <= 0) || (height <= 0)) {
			throw new IllegalArgumentException("Grid dimensions have to be positive integers, "
					+ "but parameter given are " + width + " and " + height + ".");
		}
		Graph pathHorizontal = createPathGraph(width);
		Graph pathVertical = createPathGraph(height);
		// square grid graph is Cartesian product of two paths
		// operator also sets name for new graph, P_width x P_height
		Graph squareGrid = GraphOperators.createCartesianGraphProduct(pathHorizontal, pathVertical);

		return squareGrid;
	}

	/**
	 * Creates the toroidal grid, which is square grid with left and right sides
	 * (resp. bottom and top sides) connected. The graph is the Cartesian
	 * product and thus called "C_width x C_height".
	 * 
	 * @param width
	 *            number of vertices horizontally
	 * @param height
	 *            number of vertices vertically
	 * @return the toroidal grid with given width and height
	 */
	public static Graph createToroidalGrid(int width, int height) {
		if ((width <= 2) || (height <= 2)) {
			throw new IllegalArgumentException(
					"Toroidal grid (without multi-edges) can't have less than three vertices in "
							+ "any dimension, but parameters are " + width + " and " + height
							+ ".");
		}
		Graph cycleHorizontal = createCycle(width);
		Graph cycleVertical = createCycle(height);
		// square grid graph is Cartesian product of two paths
		// operator also sets name for new graph, P_width x P_height
		Graph toroidalGrid = GraphOperators.createCartesianGraphProduct(cycleHorizontal,
				cycleVertical);

		return toroidalGrid;
	}

	/**
	 * Creates the
	 * <a href = "https://en.wikipedia.org/wiki/Circulant_graph">circulant
	 * graph</a> C_n(a1, a2, ..., ak) where 0 < a1 < a2 < ... < ak < (n+1)/2 are
	 * the given steps and n is the given number of vertices > 0.<br>
	 * Each vertex i is connected to vertices i +- a1, i +- a2, ..., i +- ak.
	 * Thereby ak can be at most numberOfNodes/2.
	 * 
	 * @param numVertices
	 * @param steps
	 *            steps for edges
	 * @return a circulant graph with given steps
	 */
	public static Graph createCirculantGraph(int numVertices, int... steps) {
		if (numVertices < 1) {
			throw new IllegalArgumentException(
					"Circulant graph can't have a non-positive number of vertices.");
		}

		if (steps.length == 0) {
			return createEdgelessGraph(numVertices).setName("C_{" + numVertices + "}(-)");
		}

		for (int i = 0; i < steps.length; i++) {
			if (steps[i] < 1) {
				throw new IllegalArgumentException(
						"Circulant graph can't have non-positive step width, "
								+ "but given parameter is " + steps[i]);
			}
			if (steps[i] > (numVertices - 1)) {
				throw new IllegalArgumentException(
						"Circulant graph can't have step width larger than number of vertices,"
								+ " but given parameter for step is " + steps[i]);
			}
		}
		double halfNumVertices = ((double) numVertices) / 2.0;
		steps = Arrays.stream(steps).map(x -> (x > halfNumVertices) ? (numVertices - x) : x)
				.sorted().distinct().toArray();

		int numberOfEdges = numVertices * steps.length;
		if (steps[steps.length - 1] == halfNumVertices) {
			numberOfEdges -= numVertices / 2;
		}

		Graph circulantGraph = new Graph(numVertices, numberOfEdges, Graph.UNDIRECTED);
		for (int k = 0; k < steps.length; ++k) {
			for (int u = 0; u < numVertices; ++u) {
				if (steps[k] != halfNumVertices) {
					int v = (u + steps[k]) % numVertices;
					circulantGraph.addEdge(u, v);
				} else if (u >= halfNumVertices) {
					continue;
				} else {
					int v = (u + steps[k]) % numVertices;
					if (v > u) {
						circulantGraph.addEdge(u, v);
					}
				}

			}

		}

		String name = "Circulent_{" + numVertices + "}(" + steps[0];
		for (int i = 1; i < steps.length; ++i) {
			name += "," + steps[i];
		}
		name += ")";
		circulantGraph.setName(name);

		return circulantGraph;
	}

	/**
	 * Create a
	 * <a href = "https://en.wikipedia.org/wiki/Permutation_graph">permutation
	 * graph</a> with given number of vertices and permutation chosen by given
	 * seed.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @param seed
	 *            seed for randomness to generate a permutation
	 * @return a permutation graph
	 */
	public static Graph createPermutationGraph(int numVertices, int seed) {
		if (numVertices < 1) {
			throw new IllegalArgumentException(
					"Permutation graph can't have a non-positive number of vertices.");
		}

		Graph permutationGraph = new Graph(numVertices, numVertices, Graph.UNDIRECTED);
		int[] permutation = RandomUtil.randomPermutation(numVertices, new Random(seed));

		for (int start1 = 0; start1 < permutation.length - 1; start1++) {
			int target1 = permutation[start1];
			for (int start2 = start1 + 1; start2 < permutation.length; start2++) {
				int target2 = permutation[start2];
				if (target2 < target1) {
					permutationGraph.addEdge(start1, start2);
				}
			}
		}

		return permutationGraph;
	}

	/**
	 * Like {@link #createPermutationGraph(int, int)}, this creates a
	 * <a href = "https://en.wikipedia.org/wiki/Permutation_graph">permutation
	 * graph</a> with given number of vertices, with the underlying permutation
	 * chosen at random.
	 * 
	 * @param numVertices
	 *            number of vertices
	 * @return a permutation graph
	 */
	public static Graph createPermutationGraph(int numVertices) {
		return createPermutationGraph(numVertices, RandomUtil.getRandom().nextInt());
	}

	/**
	 * Creates the
	 * <a href = "https://en.wikipedia.org/wiki/Complete_graph">complete
	 * graph</a> K_n, with n the number of vertices > 0.
	 * 
	 * @param numVertices
	 *            number of vertices of the complete graph
	 * @return complete graph with given number of vertices
	 */
	public static Graph createCompleteGraph(int numVertices) {
		if (numVertices < 1) {
			throw new IllegalArgumentException(
					"Complete graph can't have a non-positive number of vertices.");
		}

		Graph completeGraph = new Graph(numVertices, numVertices * (numVertices - 1) * 2,
				Graph.UNDIRECTED);
		completeGraph.setName("K_{" + numVertices + "}");
		for (int u = 0; u < numVertices; ++u) {
			for (int v = u + 1; v < numVertices; ++v) {
				completeGraph.addEdge(u, v);
			}
		}

		return completeGraph;
	}

	/**
	 * Creates the
	 * <a href = "https://en.wikipedia.org/wiki/Tur%C3%A1n_graph">Turán
	 * graph</a> T(n,r) with n vertices and r partitions.
	 * 
	 * @param numVertices
	 *            number of vertices > 0
	 * @param numPartitions
	 *            number of partitions >= numVertices
	 * @return the Turán graph T(numVertices, numPartitions)
	 */
	public static Graph createTuranGraph(int numVertices, int numPartitions) {
		if (numVertices < 1) {
			throw new IllegalArgumentException(
					"Turan graph can't have a non-positive number of vertices.");
		} else if (numPartitions < 1) {
			throw new IllegalArgumentException(
					"Turan graph can't have a non-positive number of partitions.");
		} else if (numVertices <= numPartitions) {
			throw new IllegalArgumentException(
					"Turan graph can't have a less vertices than partitions.");
		}

		if (numPartitions == 1) {
			return createCompleteGraph(numVertices)
					.setName("T(" + numVertices + "," + numPartitions + ")");
		}

		int numBigPartitions = numVertices % numPartitions;
		int numSmallPartitions = numPartitions - numBigPartitions;
		int sizeBigPartitions = numVertices / numPartitions + 1;
		int sizeSmallPartitions = sizeBigPartitions - 1;
		int numEdges = ((numBigPartitions * sizeBigPartitions * (numVertices - sizeBigPartitions))
				+ (numSmallPartitions * sizeSmallPartitions * (numVertices - sizeSmallPartitions)))
				/ 2;
		Graph turanGraph = new Graph(numVertices, numEdges, Graph.UNDIRECTED);
		turanGraph.setName("T(" + numVertices + "," + numPartitions + ")");

		int firstVertexId = 0;
		for (int partition = 0; partition < numPartitions; partition++) {
			int currentPartitionSize = (partition < numBigPartitions) ? sizeBigPartitions
					: sizeSmallPartitions;
			int firstVertexNextPartition = firstVertexId + currentPartitionSize;
			for (int i = 0; i < currentPartitionSize; i++) {
				for (int secondVertexId = firstVertexNextPartition; secondVertexId < numVertices; secondVertexId++) {
					turanGraph.addEdge(firstVertexId, secondVertexId);
					firstVertexId++;
				}
			}
		}

		return turanGraph;
	}
}
