package node;

import com.rabbitmq.client.*;
import java.io.*;

public class Node implements INode, Comparable<Node> {

    private static final String EXCHANGE_NAME = "node-ring";
    ConnectionFactory factory = new ConnectionFactory();

    public int nodeId;
    public Node next;

    public Node(int nodeId) throws IOException {
        this.nodeId = nodeId;
    }

    public int compareTo(Node n) {
        return Integer.compare(1, n.getId());
    }

    public int getId() {
        return this.nodeId;
    }

    @Override
    public void sendRight(String message) throws Exception {

        factory.setHost("localhost");
        int rightNode = getRightNode();
        int nextNode = getNextNode(rightNode);
        String node = "node" + nextNode;
        String msg = message + rightNode;

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        channel.basicPublish(EXCHANGE_NAME, node, null,
                msg.getBytes("UTF-8"));
        System.out.println(" [node" + getId() + "] Send right to '" + node + "':'" + message + "'");
    }

    public void forward(String message, int id) throws Exception {

        int nextNode = getNextNode(id);
        String node = "node" + nextNode;

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        factory.setHost("localhost");
        channel.basicPublish(EXCHANGE_NAME, node, null,
                message.getBytes("UTF-8"));

        System.out.println(
                " [node" + getId() + "] Forward to '" + node + "':'" + message.substring(0, message.length() - 1)
                        + "'");
    }

    @Override
    public void sendLeft(String message) throws Exception {

        int leftNode = getLeftNode();
        int nextNode = getNextNode(leftNode);
        String node = "node" + nextNode;
        String msg = message + leftNode; // the msg to publish plus the node dest id (for the next node to know the node
                                         // id dest if its not it)
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.basicPublish(EXCHANGE_NAME, node, null,
                msg.getBytes("UTF-8"));

        System.out.println(" [node" + getId() + "] Send left to '" + node + "':'" + message + "'");
    }

    @Override
    public void recv() throws Exception {

        String node = "node" + getId();

        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, node);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = "";
            msg = new String(delivery.getBody(), "UTF-8");
            // get the destination node id
            int nextNode = Character.getNumericValue(msg.charAt(msg.length() - 1));
            if (nextNode == getId()) {
                synchronized (System.out) {
                    System.out.println(" [node" + getId() + "] Received :'" + msg.substring(0,
                            msg.length() - 1) + "'");
                }
            } else {
                try {
                    forward(msg, nextNode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private int getLeftNode() throws IOException {
        int idNode = 0;

        File ringFile = new File("files/ring.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(ringFile))) {
            String line = reader.readLine();
            while (line != null) {
                int Id = Character.getNumericValue(line.charAt(line.length() - 1));
                if (getId() == Id) {
                    idNode = Character.getNumericValue(line.charAt(0));
                    break;
                }
                line = reader.readLine();
            }
        }
        return idNode;
    }

    private int getRightNode() throws IOException {
        int idNode = 0;

        File ringFile = new File("files/ring.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(ringFile))) {
            String line = reader.readLine();
            while (line != null) {
                int Id = Character.getNumericValue(line.charAt(0));
                if (getId() == Id) {
                    idNode = Character.getNumericValue(line.charAt(line.length() - 1));
                    break;
                }
                line = reader.readLine();
            }
        }
        return idNode;
    }

    private int getNextNode(int nodeId) throws IOException {
        int nextNode = 0;

        File pathsFile = new File("files/paths.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(pathsFile))) {
            String line = br.readLine();
            while (line != null) {
                int firstNode = Character.getNumericValue(line.charAt(0));
                int lastNode = Character.getNumericValue(line.charAt(line.length() - 1));
                if (getId() == firstNode && nodeId == lastNode) {
                    nextNode = Character.getNumericValue(line.charAt(1));
                    break;
                }
                line = br.readLine();
            }
        }
        return nextNode;
    }

}