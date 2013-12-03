package MKAgent;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
  
  // a random jframe i made so that popups will work
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
  return message.toString();
  }
  
  private static void displayInfo(Side side, String received, int randomInt, Protocol.MoveTurn moveTurn, String before,String moved, String after)
  {
    JOptionPane.showMessageDialog(frame, 
            // which side are we?
            "We are: " + side + 
            // the message received
            "\n\n" + received + 
            // what is our move
            "\nMy Move: " + (randomInt+1) + 
            // did gae just end?
            "\n\nmoveTurn end " + (moveTurn.end?"True":"False") + 
            // our turn again?
            "\nmoveTurn again " +  (moveTurn.again?"True":"False") + 
            // what move opposide side just made
            "\nmoveTurn move " +  moveTurn.move + 
            // before anything changed
            "\n\nBefore\n" + before + 
            // after opposide side made a move
            "\nMove: " + side.opposite() + " - " + moveTurn.move + "\n" + after +
            // after we made a move
            "\nMove: " + side + " - " + (randomInt+1) + "\n" + moved);
  }
  
  private static void displayMessage(String message, Protocol.MoveTurn moveTurn)
  {
    JOptionPane.showMessageDialog(frame, message +
            // did gae just end?
            "\n\nmoveTurn end " + (moveTurn.end?"True":"False") + 
            // our turn again?
            "\nmoveTurn again " +  (moveTurn.again?"True":"False") + 
            // what move opposide side just made
            "\nmoveTurn move " +  moveTurn.move);
  }

  private static void display(String message)
  {
    JOptionPane.showMessageDialog(frame, message);
  }
  
  // board to save the status of the game
  private static Board board = new Board(7, 7);
  // kalah contains methods to make making a move easier
  private static Kalah kalah;
  // int for random move
  private static int randomInt = -1;
  // which side we are on, set as south as default to avoid exception
  // (the first message will tell the agent which side we are on)
  private static Side side = Side.SOUTH;
  // strings for debug use and storing the message (received)
  private static String before = null, after = null, moved = null, received = null;
  // dont know what this is for yet
  private static Side sideAfter;
  // info about the move, if game is over, if its your turn again
  // and what move the opposite side just made
  private static Protocol.MoveTurn moveTurn = new Protocol.MoveTurn();
  private static boolean gameOver = false;
  /**
   * The main method, invoked when the program is started.
   * @param args Command line arguments.
   */
  public static void main(String[] args) throws IOException, InvalidMessageException, Exception
  {
    kalah = new Kalah(board);
    before = kalah.getBoard().toString();
    // read first message to find out which side we are on
    readMessage();
    if (side.equals(Side.SOUTH))
    {
      makeMove();
      // if opposide side swap
      readMessage();
      if (moveTurn.move == -1)
        // they swapped
        side = side.opposite(); // we swap too
    }
    else
    {
      readMessage();
      sendMsg(Protocol.createSwapMsg());
      side = side.opposite();
      readMessage();
    }
    
    while (!gameOver)
    {
      before = kalah.getBoard().toString();
      
      while (!moveTurn.again)
      {
        readMessage();
        displayInfo(side, received, randomInt, moveTurn, before, moved, after);
      }
      // make random move
      makeMove(); 
    }
  }
  
  private static void readMessage() throws IOException, InvalidMessageException
  {
    received = recvMsg();
    if (Protocol.getMessageType(received).equals(MsgType.START))
    {
      side = Protocol.interpretStartMsg(received) ? Side.SOUTH : Side.NORTH;
    }
    else if (Protocol.getMessageType(received).equals(MsgType.STATE))
    {
      moveTurn = Protocol.interpretStateMsg(received, kalah.getBoard());
      after = kalah.getBoard().toString();
//        sideAfter = kalah.makeMove(new Move(side, moveTurn.move));
    }
    else if (Protocol.getMessageType(received).equals(MsgType.END))
      gameOver = true;
    displayMessage(received, moveTurn);
  }
  
  // make a random move
  // for simplicity and predictability the next random move is always this random + 1
  private static void makeMove() throws IOException, InvalidMessageException
  {
    do
    {
      randomInt = (randomInt + 1) % 7;
    } while (!kalah.isLegalMove(new Move(side, randomInt+1)));
    
    kalah.makeMove(new Move(side, randomInt+1));
    // record the state of the moved board this agent believes it is in,
    // and then compare it later to see if it is corrent
    moved = kalah.getBoard().toString();
    sendMsg(Protocol.createMoveMsg(randomInt+1));
    displayInfo(side, received, randomInt, moveTurn, before, moved, after);
    
    // read reply from server
    readMessage();
  }
}
/* copy and paste code from here to start the agent
north
javac MKAgent/*.java &&java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java MKAgent/Main"
south
javac MKAgent/*.java &&java -jar ManKalah.jar "java MKAgent/Main" "java -jar MKRefAgent.jar"
* 
git add * && git commit -m "push" && git push
*/