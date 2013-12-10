/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MKAgent;

/**
 *
 * @author mbax9yc4
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
