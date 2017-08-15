package unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import algorithms.GraphChecks;
import algorithms.GraphGeneration;
import algorithms.RandomUtil;
import model.Graph;
import model.Vertex;

public class GraphGenerationTest {

	@Test
	public void testCreateEdgelessGraph() {
		int numVertices = RandomUtil.getRandom().nextInt(50);
		assertEquals(GraphGeneration.createEdgelessGraph(numVertices).getM(), 0);
	}

	@Test
	public void testCreatePathGraph() {
		int length = RandomUtil.getRandom().nextInt(50) + 2;
		assertEquals(GraphGeneration.createPathGraph(length).getM(), length - 1);
		length = 0;
		assertEquals(GraphGeneration.createPathGraph(length).getM(), length);
		length = 1;
		assertEquals(GraphGeneration.createPathGraph(length).getM(), 0);
	}

	@Test
	public void testCreateCycle() {
		int length = RandomUtil.getRandom().nextInt(50) + 2;
		assertEquals(GraphGeneration.createCycle(length).getM(), length);
		length = 0;
		assertEquals(GraphGeneration.createCycle(length).getM(), length);
		length = 1;
		assertEquals(GraphGeneration.createCycle(length).getM(), 0);
		length = 2;
		assertEquals(GraphGeneration.createCycle(length).getM(), 1);
	}

	@Test
	public void testCreatePrueferTree() {
		int numVertices = RandomUtil.getRandom().nextInt(50) + 2;
		Graph graph = GraphGeneration.createPrueferTree(numVertices, 42);
		assertEquals(graph.getM(), numVertices - 1);
		assertTrue(GraphChecks.isTree(graph));
	}

	@Test
	public void testCreateStar() {
		int numVertices = RandomUtil.getRandom().nextInt(50) + 2;
		assertEquals(GraphGeneration.createStar(numVertices).getM(), numVertices - 1);
		numVertices = 0;
		assertEquals(GraphGeneration.createStar(numVertices).getM(), 0);
		numVertices = 1;
		assertEquals(GraphGeneration.createStar(numVertices).getM(), 0);
		numVertices = 2;
		assertEquals(GraphGeneration.createStar(numVertices).getM(), 1);
	}

	@Test
	public void testCreateMaxOuterplanarGraph() {
		int numVertices = RandomUtil.getRandom().nextInt(50) + 4;
		assertEquals(GraphGeneration.createMaxOuterplanarGraph(numVertices).getM(),
				2 * numVertices - 3);
		numVertices = 0;
		assertEquals(GraphGeneration.createMaxOuterplanarGraph(numVertices).getM(), 0);
		numVertices = 1;
		assertEquals(GraphGeneration.createMaxOuterplanarGraph(numVertices).getM(), 0);
		numVertices = 2;
		assertEquals(GraphGeneration.createMaxOuterplanarGraph(numVertices).getM(), 1);
		numVertices = 3;
		assertEquals(GraphGeneration.createMaxOuterplanarGraph(numVertices).getM(), 3);
		numVertices = 4;
		assertEquals(GraphGeneration.createMaxOuterplanarGraph(numVertices).getM(), 5);
	}

	@Test
	public void testCreateApollonianNetwork() {
		int numVertices = RandomUtil.getRandom().nextInt(50) + 3;
		assertEquals(GraphGeneration.createApollonianNetwork(numVertices, 42).getM(),
				3 * numVertices - 6);
	}

	@Test
	public void testCreateMaxPlanarGraph() {
		int numVertices = RandomUtil.getRandom().nextInt(50) + 3;
		assertEquals(GraphGeneration.createMaxPlanarGraph(numVertices, 50, 42).getM(),
				3 * numVertices - 6);
	}

	@Test
	public void testCreateHamiltonianMaxPlanarGraph() {
		int numVertices = RandomUtil.getRandom().nextInt(500) + 3;
		assertEquals(GraphGeneration.createHamiltonianMaxPlanarGraph(numVertices, 42).getM(),
				3 * numVertices - 6);
	}

	@Test
	public void testCreateMaxOnePlanarGraph() {
		int numVertices = RandomUtil.getRandom().nextInt(50) + 3;
		assertTrue(GraphGeneration.createMaxOnePlanarGraph(numVertices, 50, 42)
				.getM() <= 4 * numVertices - 8);
	}

	@Test
	public void testCreateKTree() {
		int numVertices = 50;
		int k = 6;
		Graph graph = GraphGeneration.createKTree(numVertices, k, 42);
		for (Vertex v : graph.getVertices()) {
			assertTrue(v.getDegree() >= k);
		}
	}

	@Test
	public void testCreateHypercube() {
		int d = 5;
		Graph graph = GraphGeneration.createHypercube(d);

		int n = (int) Math.pow(2, d);
		int m = d * (int) Math.pow(2, d - 1);
		assertEquals("Number of nodes must be " + n, n, graph.getN());
		assertEquals("Number of edges must be " + m, m, graph.getM());
		assertTrue("Graph is not valid", graph.isValid());

		for (int i = 0; i < n; ++i) {
			Vertex u = graph.getVertexById(i);
			assertTrue(u.getDegree() == d);

			for (Vertex v : u.getNeighbors()) {
				assertEquals("Hamming distance to neighbors must be one", 1,
						Integer.bitCount(v.getId() ^ i));
			}
		}
	}

	@Test
	public void testCreateCubeConnectedCycles() {
		int dim = 5;
		Graph graph = GraphGeneration.createCubeConnectedCycles(dim);
		for (Vertex v : graph.getVertices()) {
			assertTrue(v.getDegree() == 3);
		}
	}

	@Test
	public void testCreateKAryNCube() {
		int dim = 5;
		Graph graph = GraphGeneration.createKAryNCube(3, dim);
		for (Vertex v : graph.getVertices()) {
			assertTrue(v.getDegree() == 2 * dim);
		}
	}

	@Test
	public void testCreateSquareGrid() {
		int numVerticesL = 10;
		int numVerticesH = 10;
		Graph graph = GraphGeneration.createSquareGrid(numVerticesL, numVerticesH);
		for (Vertex v : graph.getVertices()) {
			assertTrue(v.getDegree() >= 2);
			assertTrue(v.getDegree() <= 4);
		}
	}

	@Test
	public void testCreateToroidalGrid() {
		int numVerticesL = 10;
		int numVerticesH = 10;
		Graph graph = GraphGeneration.createToroidalGrid(numVerticesL, numVerticesH);
		for (Vertex v : graph.getVertices()) {
			assertTrue(v.getDegree() == 4);
		}
	}

	@Test
	public void testCreateCirculantGraph() {
		int numVertices = 50;
		int[] steps = { 1, 3, 5 };
		Graph graph = GraphGeneration.createCirculantGraph(numVertices, steps);
		assertEquals(graph.getM(), numVertices * 3);
		for (Vertex v : graph.getVertices()) {
			assertEquals(v.getDegree(), 6);
		}
	}

	@Test
	public void testCreatePermutationGraph() {
		// can't test whole creation method
		// would have to test if for given permutation, the correct graph gets
		// produces
	}

	@Test
	public void testCreateCompleteGraph() {
		int numVertices = RandomUtil.getRandom().nextInt(50) + 3;
		assertEquals(GraphGeneration.createCompleteGraph(numVertices).getM(),
				numVertices * (numVertices - 1) / 2);
	}

}
