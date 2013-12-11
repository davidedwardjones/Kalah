/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MKAgent;

/**
 *
 * @author mbax9yc4
 */
 
 
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Component;
 
public class EvaluationFunction
{
  private Side ourSide;
    
  // a random jframe i made so that popups will work
  private static Component frame = new JFrame("TITLE");
  
  private static void display(String message)
  {
    JOptionPane.showMessageDialog(frame, message);
  }
  
  public EvaluationFunction()
  {

  }
  
  public void setOurSide(Side side)
  {
    ourSide = side;
  }
  
  //step one: return difference in scoring wells just on our side (child board and root board)
  //step two: return difference between our scoring well and opponents scoring well in same board.
  //step three:
  public int compareScoringWells(BoardMove board, Side side, int hole)
  {
    //display(board.getSeedsInStore(side) + " - " + currentBoard.getSeedsInStore(side) + " = " +(board.getSeedsInStore(side) - currentBoard.getSeedsInStore(side)));
    // (our well - opposide well) * scalling factor + (side on our side - side on opposite side)
    
    //if board.getSide().equals(side) == true then it's our go again, so add some extra value to heuristic value
    
    boolean ourGoAgain = board.getSide().equals(side);
       
    return (board.getBoard().getSeedsInStore(side) - board.getBoard().getSeedsInStore(side.opposite())) * 2 + (board.getBoard().getSeedsInPlay(side) - board.getBoard().getSeedsInPlay(side.opposite())) + (ourGoAgain?1:0);
    
  }
  
  /*
   * returns (our seeds in well + our seeds in play) - (their seeds in well + their seeds in play)
   */
  public int compareSeedsInPlay(Board board, Side side)
  {
   	return ((board.getSeedsInStore(side)+board.getSeedsInPlay(side)) - (board.getSeedsInStore(side.opposite())+board.getSeedsInPlay(side.opposite())));
  }

 
  // if boardLength+1 is returned it means the ending position is the scoring well
  public Move getEndingPos(Board board, Move toMake)
  {
    Side resultSide = toMake.getSide();
    int boardLength = board.getNoOfHoles();
    int seedsInHole = board.getSeeds(toMake.getSide(), toMake.getHole());
    int result = toMake.getHole() + seedsInHole;
    if (result > boardLength+1)
    {
      result -= (boardLength+1);
      resultSide = resultSide.opposite();
    }
    return new Move(resultSide, result);
  }
  
}

/* copy and paste code from here to start the agent
north
javac MKAgent/*.java &&java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java MKAgent/RandomAgent"
south
javac MKAgent/*.java &&java -jar ManKalah.jar "java MKAgent/RandomAgent" "java -jar MKRefAgent.jar"
* 
git add * && git commit -m "evaluation function" && git push
git fetch --all && git reset --hard origin/master
*/
