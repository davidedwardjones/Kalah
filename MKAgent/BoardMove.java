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
  
  public BoardMove(Board b, Side s1, Side s)
  {
    this.board = b;
    this.nextMove = s;
    this.lastMove = s1;
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
}
