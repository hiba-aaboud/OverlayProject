package graph;

import java.util.*;
import node.*;
import java.io.*;

public class Graph implements IGraph {

    private ArrayList<ArrayList<Node>> nodeList;
    private int nodes;

    Graph(int nodes) {
        this.nodes = nodes;
        nodeList = new ArrayList<ArrayList<Node>>();
        for (int i = 0; i < nodes; ++i)
            nodeList.add(new ArrayList<Node>());
    }

    public void build() throws IOException {
        getGraph();
        calculateSP();
        buildRing();

    }

    public void getGraph() throws IOException {
        String fileName = "files/graph.txt";

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));

        for (int u = 0; u < nodes; u++) {
            writer.append(u + "");
            for (int i = 0; i < nodeList.get(u).size(); i++) {
                Node v = nodeList.get(u).get(i);
                writer.append(v.getId() + "");
            }
            writer.append("\n");
        }

        writer.close();
    }

    @Override
    public void addEdge(Node edge, Node neighbor) throws Exception {
        nodeList.get(edge.getId()).add(neighbor);
        nodeList.get(neighbor.getId()).add(edge);
    }

    public void buildRing() throws IOException {
        int nb_node = 0;
        File file = new File("files/graph.txt");
        FileReader fr = new FileReader(file);
        String fileName = "files/ring.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));

        try (BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                nb_node++;
            }
        }
        Node firstNode = new Node(0);
        Node currentNode = firstNode;
        Node previousNode = firstNode;
        List<Integer> path;

        for (int i = 1; i < nb_node; i++) {
            Node nextNode = new Node(i);
            path = searchPath(currentNode.getId(), nextNode.getId());
            currentNode.next = nextNode;
            previousNode = currentNode;
            currentNode = nextNode;
            for (int v : path) {
                writer.append(v + "");
            }
            writer.append("\n");
        }

        currentNode.next = firstNode;
        path = searchPath(currentNode.getId(), currentNode.next.getId());
        for (int v : path) {
            writer.append(v + "");
        }
        System.out.println("Ring successfully created : ");
        System.out.print(firstNode.getId() + " -> ");
        Node node = firstNode.next;
        while (node != firstNode) {
            System.out.print(node.getId() + " -> ");
            node = node.next;
        }
        System.out.println(firstNode.getId());
        writer.close();
    }

    public void calculateSP() throws IOException {
        // file in which the paths between the nodes are
        String fileName = "files/paths.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
        List<Integer> path;

        for (int u = 0; u < nodes; u++) {
            for (int i = 0; i < nodes; i++) {

                if (i == u) {
                } else {
                    path = searchPath(u, i);
                    for (int d : path) {
                        writer.append(d + "");
                    }
                    writer.append("\n");
                }
            }
        }
        writer.close();
    }

    /*--------------------for path search--------------------*/
    public boolean DFS(int start, int end, boolean[] visited, List<Integer> path) {
        visited[start] = true;
        path.add(start);

        if (start == end) {
            return true;
        }
        Iterator<Node> it = nodeList.get(start).iterator();
        while (it.hasNext()) {
            Node n = it.next();
            if (!visited[n.getId()]) {
                if (DFS(n.getId(), end, visited, path)) {
                    return true;
                }
            }
        }
        path.remove(path.size() - 1);
        return false;
    }

    public List<Integer> searchPath(int start, int end) {
        boolean visited[] = new boolean[nodes];
        List<Integer> path = new ArrayList<Integer>();
        DFS(start, end, visited, path);
        if (path.size() == 0) {
            System.out.println("No path was found.");
        }
        return path;
    }

}
