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
  private Board currentBoard;
  private Side ourSide;
    
  // a random jframe i made so that popups will work
  private static Component frame = new JFrame("TITLE");
  
  private static void display(String message)
  {
    JOptionPane.showMessageDialog(frame, message);
  }
  
  public EvaluationFunction(Board board)
  {
    currentBoard = board;
  }
  
  public void setOurSide(Side side)
  {
    ourSide = side;
  }
  
  public void setCurrentBoard(Board board)
  {
    currentBoard = board;
  }
  
  // step one: higher score on our scoring well
  public int compareScorlingWells(Board board, Side side, int hole)
  {
  	//display(board.getSeedsInStore(side) + " - " + currentBoard.getSeedsInStore(side) + " = " +(board.getSeedsInStore(side) - currentBoard.getSeedsInStore(side)));
  	
  	//display(currentBoard.toString() + "\nside: " + side.toString());
  	
  	if(endsInWell(side, hole))
  	{
  		return board.getSeedsInStore(side) - currentBoard.getSeedsInStore(side) + 1;
  	}
  	else
  	{
  		return board.getSeedsInStore(side) - currentBoard.getSeedsInStore(side);
  	}
  	
    
  }
  
  
  public boolean endsInWell(Side side, int hole)
  {
  	return(board.getSeeds(side, hole) + hole == 8)? true: false;
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
*/
