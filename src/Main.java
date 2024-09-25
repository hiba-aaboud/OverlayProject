import java.io.*;
import java.util.*;
import node.*;

public class Main {
    private static int getNodes() throws FileNotFoundException, IOException {
        int number_nodes = 0;
        File pathsFile = new File("matrix.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(pathsFile))) {
            while (br.readLine() != null) {
                number_nodes++;
            }
        }
        return number_nodes;
    }

    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            int id = -1; // Initialize id to an invalid value
            int num = getNodes() - 1;

            while (id < 0 || id > num) { // Ensure id is within valid range
                try {
                    System.out.println("Enter the ID of the node you want to send from (0-" + num + "):");
                    id = scanner.nextInt();
                    if (id < 0 || id > num) {
                        System.out.println("ID must be between 0 and " + num + ". Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                    // Clear the buffer
                    scanner.nextLine();
                }
            }

            Node nod = new Node(id); // Create a new Node instance with the new ID

            Node[] nodes = new Node[getNodes()]; // Number of nodes
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new Node(i);
            }

            for (Node node : nodes) {
                Thread thread = new Thread(() -> {
                    try {
                        node.recv();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }

            System.out.println("Nodes started!");

            while (true) {
                System.out
                        .println("- Send left ( l )\n- Send Right ( r )\n- Choose node id again ( id )\n- Quit ( q )");

                String command = scanner.next();

                switch (command) {
                    case "r":
                        System.out.println("Sending message to the right...");
                        final Node finalNodRight = nod;
                        new Thread(() -> {
                            try {
                                finalNodRight.sendRight("Hey from node" + finalNodRight.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                        break;
                    case "l":
                        System.out.println("Sending message to the left...");
                        final Node finalNodLeft = nod;
                        new Thread(() -> {
                            try {
                                finalNodLeft.sendLeft("Hey from node" + finalNodLeft.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                        break;

                    case "id":
                        id = -1; // Reset id to an invalid value
                        while (id < 0 || id > num) { // Ensure id is within valid range
                            try {
                                System.out.println("Enter the ID of the node you want to send from (0-" + num + "):");
                                id = scanner.nextInt();
                                if (id < 0 || id > num) {
                                    System.out.println("ID must be between 0 and " + num + ". Please try again.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please enter an integer.");
                                // Clear the buffer
                                scanner.nextLine();
                            }
                        }
                        nod = new Node(id); // Assign a new Node instance with the new ID
                        break;
                    case "q":
                        System.out.println("Exiting...");
                        System.exit(0); // Exit the program
                    default:
                        System.out.println("Invalid command!");
                        continue; // Continue the loop after invalid command
                }
            }
        }
    }

}
