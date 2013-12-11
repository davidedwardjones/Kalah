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
  private static boolean showMessage = false;
  
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
    if(showMessage)
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
    if(showMessage)
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
    if(showMessage)
    JOptionPane.showMessageDialog(frame, message);
  }
  
  // board to save the status of the game
  private static Board board = new Board(7, 7);
  // kalah contains methods to make making a move easier
  private static Kalah kalah;
  // int for random move
  private static int moveToMake = -1;
  // which side we are on, set as south as default to avoid exception
  // (the first message will tell the agent which side we are on)
  private static Side side = Side.SOUTH;
  // strings for debug use and storing the message (received)
  private static String before = null, after = null, received = null;
  // dont know what this is for yet
  // info about the move, if game is over, if its your turn again
  // and what move the opposite side just made
  private static Protocol.MoveTurn moveTurn = new Protocol.MoveTurn();
  private static boolean gameOver = false;
  
  private static int halfWayPoint = 49;
  
  private static AbstractAlgorithm alg;
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
    display("Was side " + side.toString());
    
    //if we are first
    if (side.equals(Side.SOUTH))
    {
      //always make move 2
      kalah.makeMove(new Move(side, 2));
      sendMsg(Protocol.createMoveMsg(2));
      
      //display("MoveMade");
      displayInfo(side, received, 2, moveTurn, before, kalah.getBoard().toString(), after);

      // read reply from server
      readMessage();
      
      // if opposide side swap
      readMessage();
      if (moveTurn.move == -1)
      {
        // they swapped
        side = side.opposite(); // we swap too
        display("They swapped");
      }
    }
    else //if second, always swap
    {
      readMessage();
      
      //send swap message
      sendMsg(Protocol.createSwapMsg());
      side = side.opposite();
      readMessage();
    }
    
    //display("Now side " + side.toString());
    while (!gameOver)
    {
      //save board
      before = kalah.getBoard().toString();
      
      //while not our turn
      while (!moveTurn.again)
      {
        readMessage();
        //display("WHILE MoveTurn.again = false");
        displayInfo(side, received, moveToMake, moveTurn, before, kalah.getBoard().toString(), after);
      }
      
      //make a move
      makeMove(); 
      // read reply from server
      readMessage();
    }
  }
  
  /* 
   * Make a move, using the MiniMax algorithm
   */ 
  private static void makeMove() throws IOException, InvalidMessageException
  {
    alg = new AlphaBeta(kalah.getBoard());
    //alg = new MiniMax(kalah.getBoard());
    moveToMake = alg.start(3, side);
    
    display("MAKE MOVE " + moveToMake);
    
    //make the move on our local board
    kalah.makeMove(new Move(side, moveToMake));

    //send move to engine
    sendMsg(Protocol.createMoveMsg(moveToMake));
    //display("MoveMade");
    displayInfo(side, received, moveToMake, moveTurn, before, kalah.getBoard().toString(), after);
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
//      sideAfter = kalah.makeMove(new Move(side, moveTurn.move));
    }
    else if (Protocol.getMessageType(received).equals(MsgType.END))
      gameOver = true;
    displayMessage(received, moveTurn);
  }

}
/* copy and paste code from here to start the agent
north
javac MKAgent/*.java &&java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java MKAgent/Main"
* 
javac MKAgent/*.java &&java -jar ManKalah.jar "java -jar JimmyPlayer.jar" "java MKAgent/Main"
*
south
javac MKAgent/*.java &&java -jar ManKalah.jar "java MKAgent/Main" "java -jar MKRefAgent.jar"
* 
javac MKAgent/*.java &&java -jar ManKalah.jar "java MKAgent/Main" "java -jar JimmyPlayer.jar"
*
git add * && git commit -m "new eval func" && git push
*/
