package graph;

import java.io.IOException;
import java.util.List;
import node.*;

public interface IGraph {
   void addEdge(Node edge, Node neighbor) throws Exception;

   void getGraph() throws IOException;

   void buildRing() throws IOException;

}
