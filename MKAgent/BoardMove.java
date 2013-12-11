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
  
  public BoardMove(Board b, Side s)
  {
    this.board = b;
    this.nextMove = s;
  }
  
  
  public Board getBoard()
  {
    return this.board;
  }
  
  public Side getNextSide()
  {
    return this.nextMove;
  }
}
