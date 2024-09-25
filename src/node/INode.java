package node;

public interface INode {
   void sendLeft(String message) throws Exception;

   void sendRight(String message) throws Exception;

   void forward(String message, int id) throws Exception;

   void recv() throws Exception;
}
