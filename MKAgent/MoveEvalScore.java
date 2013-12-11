/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MKAgent;

/**
 *
 * @author mbax9yc4
 */

/*
 * Links a score to a move
 * Used to remember where the best score has come from when traversing back up 
 * the game tree in the minimax algorithm.
 */
public class MoveEvalScore
{
	private int move;
	private int evalScore;
	
	public MoveEvalScore(int m, int e)
	{
		this.move = m;
		this.evalScore = e;
	}
	
	public String toString()
	{	
		return "Move " + this.move + " = " + this.evalScore;
	}
	
	public int getMove()
	{
		return this.move;
	}

	public int getScore()
	{
		return this.evalScore;
	}

}
