package MKAgent;

import static MKAgent.MiniMax.makeMove;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.util.*;

public class AlphaBeta extends AbstractAlgorithm
{
  
  private Board root;
  private EvaluationFunction evalFunc;
  private int initialDepth;
  private Side ourSide;
  
  // a random jframe i made so that popups will work
  private static Component frame = new JFrame("TITLE");

  public AlphaBeta(Board root)
  {
    this.root = root;
    evalFunc = new EvaluationFunction();
  }

  public void setRootBoard(Board b)
  {
    root = b;
  }

  private static void display(String message)
  {
    JOptionPane.showMessageDialog(frame, message);
  }
  
  
  /*
   *  Start the recursive alpha beta minimax algorithm
   */
  @Override
  public int start(int depth, Side side)
  {
    this.initialDepth = depth;
    ourSide = side;
    MoveEvalScore alpha = new MoveEvalScore(0, Integer.MIN_VALUE);
    MoveEvalScore beta = new MoveEvalScore(0, Integer.MAX_VALUE);
    BoardMove bm = new BoardMove(root, side, side);       
    //int move = this.alphabeta(bm, depth, 0, 0, alpha, beta).getMove();      
    //display("" + move);   
    return this.alphabeta(bm, depth, 0, alpha, beta).getMove();
  }
        
        
  /*
   *  Recursive alpha beta minimax algorithm for finding best move to make next, given a starting board (node).
   *    Alpha - maximising players most favouring game state
   *    Beta - maximising players least favouring game state
   */      
/*
  private MoveEvalScore alphabeta(BoardMove node,int depth, int hole, MoveEvalScore alpha, MoveEvalScore beta)
  {
    //if depth = 0 or node is a terminal node
    //display("AlphaBeta called \ndepth: " + depth);
    if(depth == 0 || Kalah.gameOver(node.getBoard()))
    {  
      //heuristic value of node
      return new MoveEvalScore(hole,evalFunc.compareScoringWells(node, ourSide, hole));
    }
      

      //for each child node
      int numHoles = node.getBoard().getNoOfHoles();
      
      if(node.getNextSide().equals(ourSide))
      {
        for(int i = 1; i <= numHoles; i++)    //wells start a 1 (0 = scoring well)
        {
          //if there are seeds in well, then make a clone
          if(node.getBoard().getSeeds(node.getNextSide(), i) > 0)
          {
            try
            {
              Board child = node.getBoard().clone();
              //make the move
              Move move = new Move(node.getNextSide(), i);
                
              BoardMove boardMove = makeMove(child, move);
                     
              alpha = max(alpha, alphabeta(boardMove, depth - 1, i, alpha, beta));
              if(beta.getScore() <= alpha.getScore())
                break;
            }
            catch(Exception e)
            {
              display(e.toString());
            }
          }
        }
        if(node.getBoard().equals(root))  //if we are returning back to Main, then return best move and the hole it came from.
          return alpha;
        else
        {
          display("alpha" + depth + "\nhole: " + hole + "\nscore: " + alpha.getScore());
          return new MoveEvalScore(hole, alpha.getScore());
        }
      }
      else
      {
        for(int i = 1; i <= numHoles; i++)    //wells start a 1 (0 = scoring well)
        {
          //if there are seeds in well, then make a clone
          if(node.getBoard().getSeeds(node.getNextSide(), i) > 0)
          {
            try
            {
              Board child = node.getBoard().clone();
              //make the move
              Move move = new Move(node.getNextSide(), i);
                
              BoardMove boardMove = makeMove(child, move);
            
              beta = min(beta, alphabeta(boardMove, depth - 1, i, alpha, beta));
              if(beta.getScore() <= alpha.getScore())
                break;
            }
            catch(Exception e)
            {
              display(e.toString());
            }
          }
        }
        if(node.getBoard().equals(root))  //if we are returning back to Main, then return best move and the hole it came from.
          return beta;
        else
        {          
          display("beta " + depth + "\nhole: " + hole + "\nscore: " + beta.getScore());
          return new MoveEvalScore(hole, beta.getScore());
        }
      }
  }
}*/


  /*
   *  Recursive alpha beta minimax algorithm for finding best move to make next, given a starting board (node).
   *    Alpha - maximising players most favouring game state
   *    Beta - maximising players least favouring game state
   */  

  private MoveEvalScore alphabeta(BoardMove node,int depth, int hole, MoveEvalScore alpha, MoveEvalScore beta)
  {
		//if depth = 0 or node is a terminal node
    //display("AlphaBeta called \ndepth: " + depth);
    if(depth == 0 || Kalah.gameOver(node.getBoard()))
    {  
      //heuristic value of node
      return new MoveEvalScore(hole, evalFunc.compareScoringWells(node, ourSide, hole));
    }

		List<BoardMove> children = createChildren(node, depth - 1);

    if (node.getNextSide().equals(ourSide)) 
    {
      for (BoardMove child : children) 
			{
        alpha = max(alpha, alphabeta(node, depth - 1, hole, alpha, beta));
        if (beta.getScore() <= alpha.getScore())
          break;
      }
      return new MoveEvalScore(hole, alpha.getScore());
    } 
    else 
    {
      for (BoardMove child : children)
      {
        beta = min(beta, alphabeta(node, depth - 1, hole, alpha, beta));
        if (beta.getScore() <= alpha.getScore())
          break;
      }
      return new MoveEvalScore(hole, beta.getScore());
    }
	}

  private List<BoardMove> createChildren(BoardMove node, int depth) 
  {
		int numHoles = node.getBoard().getNoOfHoles();

    List<BoardMove> children = new ArrayList<BoardMove>();
    for(int i = 1; i <= numHoles; i++)    //wells start at 1 (0 = scoring well)
    {
      //if there are seeds in well, then make a clone
      if(node.getBoard().getSeeds(node.getNextSide(), i) > 0)
      {
        try
        {
          Board child = node.getBoard().clone();
          //make the move
          Move move = new Move(node.getNextSide(), i); 
          BoardMove boardMove = makeMove(child, move);
					children.add(boardMove);
				}
        catch(Exception e)
        {
          display(e.toString());
        } 
			}  
	  }
	return children;
  }
}































