package MKAgent;

//TO DO

//somehow determine if pie rule played
//if player 1 and player 2 swaps, then swap the boolean maximisingPlayer

//if second player, do minimax as player one and see if any values for player 1 are better than player 2.
//if so, then play pie rule and swap.

//alpha-beta pruning

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Component;

public class MiniMax
{
  
  private Board root;
  private EvaluationFunction evalFunc;
  private int initialDepth;
  
  // a random jframe i made so that popups will work
  private static Component frame = new JFrame("TITLE");
  
  
  
  public MiniMax(Board root)
  {
    this.root = root;
    evalFunc = new EvaluationFunction(root);
  }

  public void setRootBoard(Board b)
  {
    root = b;
  }


	private static void display(String message)
  {
    //JOptionPane.showMessageDialog(frame, message);
  }

  public int startMiniMax(int depth, Side side)
  {
  	this.initialDepth = depth;
    return this.minimax(root, depth, side, 0, 0).getMove();
  }
        
  private MoveEvalScore minimax(Board node, int depth, Side side, int hole, int prevHole)
  {
  	//display("minimax(" + node.toString() + ", " + depth + ", " + maximisingPlayer + ")");
    //if depth = 0 or node is a terminal node
    if(depth == 0 || Kalah.gameOver(node))
    {
      return new MoveEvalScore(hole,evalFunc.compareScorlingWells(node, initialDepth % 2 == 0 ?side: side.opposite(), hole));//heuristic value of node
    }
    
    
      MoveEvalScore bestValue = side.equals(Side.SOUTH)? new MoveEvalScore(hole,Integer.MIN_VALUE): new MoveEvalScore(hole,Integer.MAX_VALUE);
      
      //for each child node
      int numHoles = node.getNoOfHoles();
      
      for(int i = 1; i <= numHoles; i++)    //south side 0-6 are wells, 7 = store
      {
        //if there are seeds, then make a clone
        if(node.getSeeds(side, i) > 0)
        {
          try
          {
            Board child = node.clone();
            //make the move
            Move move = new Move(side, i);
            child = makeMove(child, move);
            
            //recursively call minimax on child
            MoveEvalScore val = minimax(child, depth - 1, side.opposite(), i, hole);
            display("depth: " + depth + "  prevHole: " + prevHole + "\n" + root.toString() + "\n" + child.toString()
            				+ "\nValue = " + val);
            
            bestValue = /*side.equals(Side.SOUTH)?*/
            max(bestValue, val);//:min(bestValue, val);
          }
          catch(Exception e)
          {
            display(e.toString());
          }
        }
      }
      return new MoveEvalScore(prevHole, bestValue.getScore());
  }
  
  public static MoveEvalScore max(MoveEvalScore a, MoveEvalScore b)
  {
    return a.getScore() > b.getScore() ? a : b; 
  }
  
  public static MoveEvalScore min(MoveEvalScore a, MoveEvalScore b)
  {
    return a.getScore() < b.getScore() ? a : b; 
  }
  
  /* MOVE SOMEWHERE ELSE  */
  
  public static Board makeMove (Board board, Move move)
    {
    /* from the documentation:
      "1. The counters are lifted from this hole and sown in anti-clockwise direction, starting
          with the next hole. The player's own kalahah is included in the sowing, but the
          opponent's kalahah is skipped.
       2. outcome:
          1. if the last counter is put into the player's kalahah, the player is allowed to
             move again (such a move is called a Kalah-move);
          2. if the last counter is put in an empty hole on the player's side of the board
             and the opposite hole is non-empty,
             a capture takes place: all stones in the opposite opponents pit and the last
             stone of the sowing are put into the player's store and the turn is over;
          3. if the last counter is put anywhere else, the turn is over directly.
       3. game end:
          The game ends whenever a move leaves no counters on one player's side, in
          which case the other player captures all remaining counters. The player who
          collects the most counters is the winner."
    */


      // pick seeds:
      int seedsToSow = board.getSeeds(move.getSide(), move.getHole());
      board.setSeeds(move.getSide(), move.getHole(), 0);

      int holes = board.getNoOfHoles();
      int receivingPits = 2*holes + 1;  // sow into: all holes + 1 store
      int rounds = seedsToSow / receivingPits;  // sowing rounds
      int extra = seedsToSow % receivingPits;  // seeds for the last partial round
      /* the first "extra" number of holes get "rounds"+1 seeds, the
         remaining ones get "rounds" seeds */

      // sow the seeds of the full rounds (if any):
      if (rounds != 0)
      {
        for (int hole = 1; hole <= holes; hole++)
        {
            board.addSeeds(Side.NORTH, hole, rounds);
            board.addSeeds(Side.SOUTH, hole, rounds);
        }
        board.addSeedsToStore(move.getSide(), rounds);
      }

      // sow the extra seeds (last round):
      Side sowSide = move.getSide();
      int sowHole = move.getHole();  // 0 means store
      for (; extra > 0; extra--)
      {
        // go to next pit:
        sowHole++;
        if (sowHole == 1)  // last pit was a store
          sowSide = sowSide.opposite();
        if (sowHole > holes)
        {
          if (sowSide == move.getSide())
          {
            sowHole = 0;  // sow to the store now
              board.addSeedsToStore(sowSide, 1);
              continue;
          }
          else
          {
              sowSide = sowSide.opposite();
            sowHole = 1;
          }
        }
        // sow to hole:
        board.addSeeds(sowSide, sowHole, 1);
      }

      // capture:
      if ( (sowSide == move.getSide())  // last seed was sown on the moving player's side ...
         && (sowHole > 0)  // ... not into the store ...
         && (board.getSeeds(sowSide, sowHole) == 1)  // ... but into an empty hole (so now there's 1 seed) ...
         && (board.getSeedsOp(sowSide, sowHole) > 0) )  // ... and the opposite hole is non-empty
      {
        board.addSeedsToStore(move.getSide(), 1 + board.getSeedsOp(move.getSide(), sowHole));
        board.setSeeds(move.getSide(), sowHole, 0);
        board.setSeedsOp(move.getSide(), sowHole, 0);
      }

      // game over?
    Side finishedSide = null;
      if (holesEmpty(board, move.getSide()))
        finishedSide = move.getSide();
      else if (holesEmpty(board, move.getSide().opposite()))
        finishedSide = move.getSide().opposite();
        /* note: it is possible that both sides are finished, but then
           there are no seeds to collect anyway */
      if (finishedSide != null)
      {
        // collect the remaining seeds:
        int seeds = 0;
        Side collectingSide = finishedSide.opposite();
        for (int hole = 1; hole <= holes; hole++)
        {
          seeds += board.getSeeds(collectingSide, hole);
          board.setSeeds(collectingSide, hole, 0);
        }
      board.addSeedsToStore(collectingSide, seeds);
      }

      return board;

    }
  
    /**
     * Checks whether all holes on a given side are empty.
     * @param board The board to check.
     * @param side The side to check.
     * @return "true" iff all holes on side "side" are empty.
     */
    protected static boolean holesEmpty (Board board, Side side)
    {
      for (int hole = 1; hole <= board.getNoOfHoles(); hole++)
        if (board.getSeeds(side, hole) != 0)
          return false;
    return true;
    }
  
}
