# OverlayProject

# Project Structure

    ├── bin/                 # Compiled Java classes
    ├── files/               # Text files generated during execution
    ├── lib/                 # Libraries needed for compilation
    ├── src/                 # Source code directory
    │   ├── Main.java        # Main class to launch nodes as senders and receivers
    │   ├── graph/           # Folder containing graph and ring functionalities
    │   │   ├── IGraph.java  # Interface representing a graph
    │   │   └── Graph.java   # Implementation of the graph interface
    │   └── node/            # Folder containing node-related classes
    │       ├── INode.java   # Interface representing a node
    │       └── Node.java    # Implementation of the node interface
    ├── matrix.txt           # Text file representing the physical graph matrix
    └── README.md            # Project README file
