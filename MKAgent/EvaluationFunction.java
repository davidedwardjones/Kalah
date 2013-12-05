/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MKAgent;

/**
 *
 * @author mbax9yc4
 */
public class EvaluationFunction
{
  private Board currentBoard;
  private Side ourSide;
  
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
  public int compareScorlingWells(Board board, Side side)
  {
    return currentBoard.getSeedsInStore(side) - board.getSeedsInStore(side);
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