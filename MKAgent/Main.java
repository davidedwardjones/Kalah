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
  
  private static void displayInfo(Side side, String received, int randomInt, Protocol.MoveTurn moveTurn, String before,String moved, String after)
  {
    JOptionPane.showMessageDialog(frame, side + "\n\n" + received + "\nMy Move: " + (randomInt+1) + 
              "\n\nmoveTurn end " + (moveTurn.end?"True":"False") + 
              "\nmoveTurn again " +  (moveTurn.again?"True":"False") + 
              "\nmoveTurn move " +  moveTurn.move + 
              "\n\nBefore\n" + before + "\nMove: " + side.opposite() + " - " + moveTurn.move + "\n" + after + "\nMove: " + side + " - " + (randomInt+1) + "\n" + moved);
  }

   private static Random random = new Random();
   private static Board board = new Board(7, 7);
   private static Kalah kalah;
   private static int randomInt = -1;
   private static Side side = Side.SOUTH;
  /**
   * The main method, invoked when the program is started.
   * @param args Command line arguments.
   */
  public static void main(String[] args) throws IOException, InvalidMessageException, Exception
  {
//    frame.setSize(new Dimension(300, 400));
//    frame.setVisible(true);
    // TODO: implement
    kalah = new Kalah(board);
    String message = recvMsg();
    MsgType startMsg = Protocol.getMessageType(message);
    if (startMsg.equals(MsgType.START))
    {
      side = Protocol.interpretStartMsg(message) ? Side.SOUTH : Side.NORTH;
    }
    else throw new Exception();
//    JOptionPane.showMessageDialog(frame,side);
    String before = null, after = null, moved = null, received = null;
    Side sideAfter;
    Protocol.MoveTurn moveTurn = null;
    if (side.equals(Side.SOUTH))
    {
      kalah.makeMove(makeMove());
      sendMsg(Protocol.createMoveMsg(randomInt+1));
      displayInfo(side, received, randomInt, moveTurn, before, moved, after);
    }
    while (true)
    {
      do{
      before = kalah.getBoard().toString();
      after = "ERROR!";
      received = recvMsg();
      if (Protocol.getMessageType(received).equals(MsgType.STATE))
      {
        moveTurn = Protocol.interpretStateMsg(received, kalah.getBoard());
        after = kalah.getBoard().toString();
        if (kalah.gameOver()) break;
//        sideAfter = kalah.makeMove(new Move(side, moveTur n.move));
      }
//      else
      
        
      
      kalah.makeMove(makeMove());
      moved = kalah.getBoard().toString();
      sendMsg(Protocol.createMoveMsg(randomInt+1));
      displayInfo(side, received, randomInt, moveTurn, before, moved, after);
      }while (moveTurn.again);
    }
    
//    sendMsg(Protocol.createMoveMsg(2));
//    MsgType message = Protocol.getMessageType(test);
//    if (message.equals(MsgType.START))
//    {
//      Protocol.interpretStartMsg(test);
//    }
  }
  
  private static Move makeMove()
  {
    do
    {
      randomInt = (randomInt + 1) % 7;
    } while (!kalah.isLegalMove(new Move(side, randomInt+1)));
    return new Move(side, randomInt+1);
  }
}
/*
north
javac MKAgent/*.java &&java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java MKAgent/Main"
south
javac MKAgent/*.java &&java -jar ManKalah.jar "java MKAgent/Main" "java -jar MKRefAgent.jar"
itself
javac MKAgent/*.java &&java -jar ManKalah.jar "java MKAgent/Main" "java MKAgent/Main"
*/