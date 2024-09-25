package graph;

import java.io.*;
import node.*;

public class Init {
    public static void main(String[] args) throws Exception {

        System.out.println("Overlay Project !");

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            int lineCount = 0;
            while (reader.readLine() != null) {
                lineCount++;
            }
            Graph graph = new Graph(lineCount);
            String line;
            int lineNumber = 0;
            BufferedReader rr = new BufferedReader(new FileReader(args[0]));
            while ((line = rr.readLine()) != null) {
                String[] elements = line.split("\\s+");
                int columnNumber = 0;
                for (String element : elements) {
                    if (element.equals("1")) {
                        Node node1 = new Node(lineNumber);
                        Node node2 = new Node(columnNumber);
                        graph.addEdge(node1, node2);
                    }
                    columnNumber++;
                }

                lineNumber++;
            }
            graph.build();
        }
    }
}
