package MKAgent;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mbax9ac3
 */

/*
 * Class that stores the current board and whose move it is next
 */
public class BoardMove {
  
  private Board board;
  private Side nextMove;
  private Side lastMove;
  private int holeMoved;
  
  public BoardMove(Board b, Side s1, Side s, int hole)
  {
    this.board = b;
    this.nextMove = s;
    this.lastMove = s1;
    this.holeMoved = hole;
  }
  
  public int getHoleMoved()
  {
    return this.holeMoved;
  }
  
  public Board getBoard()
  {
    return this.board;
  }
  
  public Side getNextSide()
  {
    return this.nextMove;
  }
  
  public Side getLastSide()
  {
    return this.lastMove;
  }
  
  public boolean isFreeTurn()
  {
    if (nextMove.equals(this.lastMove))
      return true;
    else
      return false;
  }
}
