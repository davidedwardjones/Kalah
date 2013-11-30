package MKAgent;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The main application class. It also provides methods for communication
 * with the game engine.
 */
public class Main
{
  /**
   * Input from the game engine.
   */
  private static Reader input = new BufferedReader(new InputStreamReader(System.in));
  private static Component frame = new JFrame("TITLE");

  /**
   * Sends a message to the game engine.
   * @param msg The message.
   */
  public static void sendMsg (String msg)
  {
    System.out.print(msg);
    System.out.flush();
  }

  /**
   * Receives a message from the game engine. Messages are terminated by
   * a '\n' character.
   * @return The message.
   * @throws IOException if there has been an I/O error.
   */
  public static String recvMsg() throws IOException
  {
    StringBuilder message = new StringBuilder();
    int newCharacter;

    do
    {
      newCharacter = input.read();
      if (newCharacter == -1)
        throw new EOFException("Input ended unexpectedly.");
      message.append((char)newCharacter);
    } while((char)newCharacter != '\n');
//    JOptionPane.showMessageDialog(frame, message.toString());
  return message.toString();
  }

  /**
   * The main method, invoked when the program is started.
   * @param args Command line arguments.
   */
  public static void main(String[] args) throws IOException, InvalidMessageException, Exception
  {
    frame.setSize(new Dimension(300, 400));
    frame.setVisible(true);
    // TODO: implement
    String message = recvMsg();
    MsgType startMsg = Protocol.getMessageType(message);
    Side side = Side.SOUTH;
    if (startMsg.equals(MsgType.START))
    {
      side = Protocol.interpretStartMsg(message) ? Side.SOUTH : Side.NORTH;
    }
    else throw new Exception();
//    JOptionPane.showMessageDialog(frame,side);
    Random random = new Random();
    Board board = new Board(7, 7);
    Kalah kalah = new Kalah(board);
    int randomInt = -1;
    while (true)
    {
      do
      {
        randomInt = (randomInt + 1) % 7;
      } while (!kalah.isLegalMove(new Move(side, randomInt+1)));
        
      
      sendMsg(Protocol.createMoveMsg(randomInt+1));
      kalah.makeMove(new Move(side, randomInt+1));
      String received = recvMsg();
      if (Protocol.getMessageType(received).equals(MsgType.STATE))
      {
        Protocol.MoveTurn moveTurn = Protocol.interpretStateMsg(received, kalah.getBoard());
        if (kalah.gameOver()) break;
//        JOptionPane.showMessageDialog(frame, kalah.getBoard().toString());
//        Side sideAfter = kalah.makeMove(new Move(side, moveTurn.move));
      }
//      JOptionPane.showMessageDialog(frame, received + "\n" + randomInt + "\n" + kalah.getBoard().toString());
    }
    
//    sendMsg(Protocol.createMoveMsg(2));
//    MsgType message = Protocol.getMessageType(test);
//    if (message.equals(MsgType.START))
//    {
//      Protocol.interpretStartMsg(test);
//    }
  }
}
/*
javac MKAgent/*.java &&java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java MKAgent/Main"
javac MKAgent/*.java &&java -jar ManKalah.jar "java MKAgent/Main" "java -jar MKRefAgent.jar"
*/