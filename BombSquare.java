import java.util.*;

/**
 * 
 * @author Ian Tan
 * @version 9/11/2023
 */
public class BombSquare extends GameSquare
{
	private boolean thisSquareHasBomb = false;
	public static final int MINE_PROBABILITY = 10;

	BombSquare adjGameSquare;
	Set<BombSquare> visitedSquares = new HashSet<BombSquare>();
	
	/**
	 * 
	 * @param x The horizontal position on the board
	 * @param y The vertical position on the board
	 * @param board The gameboard to play the game
	 */
	public BombSquare(int x, int y, GameBoard board)
	{
		super(x, y, "images/blank.png", board);

		Random r = new Random();
		thisSquareHasBomb = (r.nextInt(MINE_PROBABILITY) == 0);
	}
	
	/**
	 * This method is called when the player clicks a game square
	 * This method checks if the clicked square contains a bomb or not
	 */
	public void clicked()
	{
		if (thisSquareHasBomb)
		{
			setImage("images/bomb.png");
		}
		else
		{	
			countBombs();
		}
	}
	
	/**
	 * This method is called when there is no bomb in the clicked square
	 * This method checks how many bombs are in the adjacent squares
	 */
	public void countBombs()
	{
		int bombCount = 0;
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				adjGameSquare = (BombSquare) board.getSquareAt(xLocation + i-1, yLocation + j-1);

				if (adjGameSquare != null && adjGameSquare != this && adjGameSquare.thisSquareHasBomb)
				{
					bombCount++;
				}
			}
		}

		setImage("images/" + bombCount + ".png");

		if (bombCount == 0)
		{
			clickAdjSquares(bombCount, visitedSquares);
		}
	}
	
	/**
	 * @param bombCount This variable counts the number of bombs in a game square
	 * @param visitedSquares
	 * This method is called when an adjacent square contains no bomb
	 * This method is a recursion method that checks for squares that contain no bombs and reveal
	 */
	public void clickAdjSquares(int bombCount, Set<BombSquare> visitedSquares)
	{
		if (bombCount != 0 || visitedSquares.contains(this)) {return;}

		visitedSquares.add(this);

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				adjGameSquare = (BombSquare) board.getSquareAt(xLocation + i-1, yLocation + j-1);

				if(adjGameSquare != null && adjGameSquare != this && !visitedSquares.contains(adjGameSquare))
				{
					adjGameSquare.clicked();
					adjGameSquare.clickAdjSquares(getBombCount(adjGameSquare), visitedSquares);
				}
			}
		}
	}
	
	/**
	 * @param square This variable is the position of the bomb square object
	 * @return This returns bombCount of an adjacent square
	 * This method is called to find the number of bombs in an adjacent square's adjacent square
	 */
	public int getBombCount(BombSquare square) 
	{
		int bombCount = 0;
	
		for (int i = 0; i < 3; i++) 
		{
			for (int j = 0; j < 3; j++) 
			{
				adjGameSquare = (BombSquare) board.getSquareAt(square.xLocation + i - 1, square.yLocation + j - 1);
	
				if (adjGameSquare != null && adjGameSquare != square && adjGameSquare.thisSquareHasBomb) 
				{
					bombCount++;
				}
			}
		}
	
		return bombCount;
	}
}
