package test;

import org.junit.Test;
import sol.TravelController;
import sol.TravelGraph;
import src.Transport;
import src.TransportType;
import test.simple.SimpleEdge;
import test.simple.SimpleGraph;
import test.simple.SimpleVertex;
import src.City;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Your Graph method tests should all go in this class!
 * The test we've given you will pass, but we still expect you to write more tests using the
 * City and Transport classes.
 * You are welcome to write more tests using the Simple classes, but you will not be graded on
 * those.
 *
 */
public class GraphTest {
    private SimpleGraph graph;

    private SimpleVertex a;
    private SimpleVertex b;
    private SimpleVertex c;

    private SimpleEdge edgeAB;
    private SimpleEdge edgeBC;
    private SimpleEdge edgeCA;
    private SimpleEdge edgeAC;

    private TravelGraph travelGraph;
    private Transport transportBostonToProvidence;
    private Transport transportProvidenceToNYC;

    /**
     * Creates a simple graph.
     * You'll find a similar method in each of the Test files.
     * Normally, we'd like to use @Before, but because each test may require a different setup,
     * we manually call the setup method at the top of the test.
     *
     *
     */
    private void createSimpleGraph() {
        this.graph = new SimpleGraph();

        this.a = new SimpleVertex("A");
        this.b = new SimpleVertex("B");
        this.c = new SimpleVertex("C");

        this.graph.addVertex(this.a);
        this.graph.addVertex(this.b);
        this.graph.addVertex(this.c);

        // create and insert edges
        this.edgeAB = new SimpleEdge(1, this.a, this.b);
        this.edgeBC = new SimpleEdge(1, this.b, this.c);
        this.edgeCA = new SimpleEdge(1, this.c, this.a);
        this.edgeAC = new SimpleEdge(1, this.a, this.c);

        this.graph.addEdge(this.a, this.edgeAB);
        this.graph.addEdge(this.b, this.edgeBC);
        this.graph.addEdge(this.c, this.edgeCA);
        this.graph.addEdge(this.a, this.edgeAC);
    }

    /**
     * Creating a basic graph to test TravelGraph methods on
     */
    private void createGraph() {
        this.travelGraph = new TravelGraph();
        this.travelGraph.addVertex(new City("Boston"));
        this.travelGraph.addVertex(new City("Providence"));
        this.travelGraph.addVertex(new City("New York City"));

        //adding Train transport from Boston to Providence
        this.transportBostonToProvidence = new Transport(this.travelGraph.getCityByName("Boston"),
                this.travelGraph.getCityByName("Providence"), TransportType.TRAIN, 30, 60);
        this.travelGraph.addEdge(this.travelGraph.getCityByName("Boston"), this.transportBostonToProvidence);

        //adding Train transport from Providence to NYC
        this.transportProvidenceToNYC = new Transport(this.travelGraph.getCityByName("Providence"),
                this.travelGraph.getCityByName("New York City"), TransportType.TRAIN, 100, 100);
        this.travelGraph.addEdge(this.travelGraph.getCityByName("Providence"), this.transportProvidenceToNYC);

        //adding Bus transport from Boston to NYC
        Transport transportBostonToNYC = new Transport(this.travelGraph.getCityByName("Boston"),
                this.travelGraph.getCityByName("New York City"), TransportType.BUS, 50, 250);
        this.travelGraph.addEdge(this.travelGraph.getCityByName("Boston"), transportBostonToNYC);

        //adding Plane transport from NYC to Boston
        Transport transportNYCToBoston = new Transport(this.travelGraph.getCityByName("New York City"),
                this.travelGraph.getCityByName("Boston"), TransportType.PLANE, 250, 50);
        this.travelGraph.addEdge(this.travelGraph.getCityByName("New York City"), transportNYCToBoston);
    }

    /**
     * Testing getVertices() for the basic graph
     */
    @Test
    public void testGetVertices() {
        this.createSimpleGraph();

        // test getVertices to check this method AND insertVertex
        assertEquals(this.graph.getVertices().size(), 3);
        assertTrue(this.graph.getVertices().contains(this.a));
        assertTrue(this.graph.getVertices().contains(this.b));
        assertTrue(this.graph.getVertices().contains(this.c));
    }

    /**
     * Testing different TravelGraph methods on basic graph
     */
    @Test
    public void testBasicGraph() {
        this.createGraph();
        //checks if Boston has two outgoing routes
        assertEquals(this.travelGraph.getOutgoingEdges(this.travelGraph.getCityByName("Boston")).size(), 2);
        //checks if New York City has one outgoing route
        assertEquals(this.travelGraph.getOutgoingEdges(this.travelGraph.getCityByName("New York City")).size(),
                1);
        //checks if Providence has one outgoing route
        assertEquals(this.travelGraph.getOutgoingEdges(this.travelGraph.getCityByName("Providence")).size(),
                1);
        //checks if the size of the vertices of the travel graph is 3 (Boston, Providence, New York City)
        assertEquals(this.travelGraph.getVertices().size(), 3);
        assertTrue(this.travelGraph.getVertices().contains(this.travelGraph.getCityByName("Boston")));
        assertTrue(this.travelGraph.getVertices().contains(this.travelGraph.getCityByName("Providence")));
        assertTrue(this.travelGraph.getVertices().contains(this.travelGraph.getCityByName("New York City")));

        //checking getEdgeSource() and getEdgeTarget() for the Transport from Boston to Providence
        assertEquals(this.travelGraph.getEdgeSource(this.transportBostonToProvidence).toString(), "Boston");
        assertEquals(this.travelGraph.getEdgeTarget(this.transportBostonToProvidence).toString(), "Providence");
        //checking getEdgeSource() and getEdgeTarget() for the Transport from Providence to New York City
        assertEquals(this.travelGraph.getEdgeSource(this.transportProvidenceToNYC).toString(), "Providence");
        assertEquals(this.travelGraph.getEdgeTarget(this.transportProvidenceToNYC).toString(), "New York City");
    }

}
