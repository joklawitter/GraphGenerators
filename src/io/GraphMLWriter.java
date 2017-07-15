package io;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Edge;
import model.Graph;
import model.Vertex;

/**
 * 
 * This class provides a method ({@link #writeGraphToGraphMLFile(Graph, File)})
 * to output a {@link model.Graph graph} to a
 * <a href = "http://graphml.graphdrawing.org/">GraphML</a>-file.
 *
 * @author Jonathan Klawitter
 */
public class GraphMLWriter {

	private static final String GRAPHML = "graphml";
	private static final String XMLNS_NAME = "xmlns";
	private static final String XMLNS_VALUE = "http://graphml.graphdrawing.org/xmlns";
	private static final String XMLNS_XSI_NAME = "xmlns:xsi";
	private static final String XMLNS_XSI_VALUE = "http://www.w3.org/2001/XMLSchema-instance";
	private static final String XSI_SCHEMA_LOCATION_NAME = "xsi:schemaLocation";
	private static final String XSI_SCHEMA_LOCATION_VALUE = "http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd";

	private static final String ID = "id";
	private static final String GRAPH_TAG = "graph";
	private static final String GRAPH_ID_VALUE = "G";
	private static final String GRAPH_EDGE_DEFAULT = "edgedefault";
	private static final String GRAPH_EDGE_DEFAULT_UNDIRECTED = "undirected";
	private static final String GRAPH_EDGE_DEFAULT_DIRECTED = "directed";
	private static final String VERTEX_TAG = "node";
	private static final String VERTEX_ID_PRAEFIX = "n";
	private static final String EDGE_TAG = "edge";
	private static final String EDGE_ID_PRAEFIX = "e";
	private static final String EDGE_SOURCE_VERTEX = "source";
	private static final String EDGE_TARGET_VERTEX = "target";

	private GraphMLWriter() {
		throw new AssertionError();
	}

	/**
	 * Outputs a {@link Graph} to a
	 * <a href="http://graphml.graphdrawing.org/">GraphML</a>-file, following
	 * <a href =
	 * "https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/">this
	 * </a> schema.
	 * 
	 * @param graph
	 * @param file
	 */
	public static void writeGraphToGraphMLFile(Graph graph, File file) {
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.newDocument();

			// set document to be graphML
			Element rootElement = document.createElement(GRAPHML);
			rootElement.setAttribute(XMLNS_NAME, XMLNS_VALUE);
			rootElement.setAttribute(XMLNS_XSI_NAME, XMLNS_XSI_VALUE);
			rootElement.setAttribute(XSI_SCHEMA_LOCATION_NAME, XSI_SCHEMA_LOCATION_VALUE);
			document.appendChild(rootElement);

			// create graph element and add attributes
			Element graphElement = document.createElement(GRAPH_TAG);
			graphElement.setAttribute(ID, GRAPH_ID_VALUE);
			graphElement.setAttribute(GRAPH_EDGE_DEFAULT, graph.isDirected()
					? GRAPH_EDGE_DEFAULT_DIRECTED : GRAPH_EDGE_DEFAULT_UNDIRECTED);
			rootElement.appendChild(graphElement);
			// TODO: define and add graph class as attribute
			// TODO: define and add graph name as attribute

			// add vertices to graph
			for (Vertex vertex : graph.getVertices()) {
				Element nodeElement = document.createElement(VERTEX_TAG);
				nodeElement.setAttribute(ID, VERTEX_ID_PRAEFIX + vertex.getId());
				graphElement.appendChild(nodeElement);
			}

			// add edges
			for (Edge edge : graph.getEdges()) {
				Element edgeElement = document.createElement(EDGE_TAG);
				edgeElement.setAttribute(ID, EDGE_ID_PRAEFIX + edge.getId());
				edgeElement.setAttribute(EDGE_SOURCE_VERTEX,
						VERTEX_ID_PRAEFIX + edge.getStartVertex().getId());
				edgeElement.setAttribute(EDGE_TARGET_VERTEX,
						VERTEX_ID_PRAEFIX + edge.getTargetVertex().getId());
				graphElement.appendChild(edgeElement);
			}

			// write the content into xml file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(file);
			// ... or to output to console:
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
