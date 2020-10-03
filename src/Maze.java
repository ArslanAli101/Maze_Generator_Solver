import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Maze {

	boolean left = false;// added
	Cell[][] cells;
	int size;
	int solveSpeed = 0;// added
	Canvas drawObj;// added
	boolean reachedGoal = false;// added
	Queue<Cell> queueForBFS = new LinkedList<Cell>();// added
	Queue<Cell> queueForBFS_fullpath = new LinkedList<Cell>();// added
	Stack<Cell> st_dfs_fullpath = new Stack<Cell>();// added

	public Maze(int size, int solveSpeed) {
		this.solveSpeed = solveSpeed;
		this.size = size;
		cells = new Cell[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j] = new Cell();
				cells[i][j].x = i;
				cells[i][j].y = j;
			}
		}
		generate();
	}

	public void setCanvas(Canvas can) {
		drawObj = can;
	}

	public void generate() {
		LinkedList<Cell> inCells = new LinkedList<Cell>();
		LinkedList<Cell> frontierCells = new LinkedList<Cell>();

		Random random = new Random();

		inCells.add(cells[0][0]);
		neighbors(inCells, frontierCells, inCells.peek());

		float temp;

		while (!frontierCells.isEmpty()) {
			int ind = random.nextInt(frontierCells.size());
			Cell fc = frontierCells.get(ind);
			int x = fc.x, y = fc.y;
			temp = random.nextFloat();
			if (x + 1 < size && temp < 0.25 && inCells.contains(cells[x + 1][y])) {
				cells[x + 1][y].leftCell = fc;
				cells[x + 1][y].leftBoolean = true;
				fc.rightCell = cells[x + 1][y];
				fc.rightBoolean = true;
				addCell(inCells, frontierCells, ind, fc);
			} else if (x - 1 >= 0 && temp >= 0.25 && temp < 0.50 && inCells.contains(cells[x - 1][y])) {
				cells[x - 1][y].rightCell = fc;
				cells[x - 1][y].rightBoolean = true;
				fc.leftCell = cells[x - 1][y];
				fc.leftBoolean = true;
				addCell(inCells, frontierCells, ind, fc);
			} else if (y + 1 < size && temp >= 0.5 && temp < 0.75 && inCells.contains(cells[x][y + 1])) {
				cells[x][y + 1].downCell = fc;
				cells[x][y + 1].downBoolean = true;
				fc.upCell = cells[x][y + 1];
				fc.upBoolean = true;
				addCell(inCells, frontierCells, ind, fc);
			} else if (y - 1 >= 0 && temp >= 0.75 && inCells.contains(cells[x][y - 1])) {
				cells[x][y - 1].upCell = fc;
				cells[x][y - 1].upBoolean = true;
				fc.downCell = cells[x][y - 1];
				fc.downBoolean = true;
				addCell(inCells, frontierCells, ind, fc);
			}
		}
	}

	private void addCell(LinkedList<Cell> inCells, LinkedList<Cell> frontierCells, int ind, Cell fc) {

		inCells.add(frontierCells.get(ind));
		neighbors(inCells, frontierCells, fc);
		frontierCells.remove(ind);
	}

	public void neighbors(LinkedList<Cell> inCells, LinkedList<Cell> frontierCells, Cell cell) {
		int x = cell.x;
		int y = cell.y;
		if (y - 1 >= 0 && !inCells.contains(cells[x][y - 1]) && !frontierCells.contains(cells[x][y - 1]))
			frontierCells.add(cells[x][y - 1]);
		if (y + 1 < size && !inCells.contains(cells[x][y + 1]) && !frontierCells.contains(cells[x][y + 1]))
			frontierCells.add(cells[x][y + 1]);
		if (x - 1 >= 0 && !inCells.contains(cells[x - 1][y]) && !frontierCells.contains(cells[x - 1][y]))
			frontierCells.add(cells[x - 1][y]);
		if (x + 1 < size && !inCells.contains(cells[x + 1][y]) && !frontierCells.contains(cells[x + 1][y]))
			frontierCells.add(cells[x + 1][y]);
	}

	public void start_Rec_DFS() { // added
		Rec_DFS(cells[0][0]);
	}

	private void Rec_DFS(Cell current) {// added

		if (reachedGoal || current.isVisitedbfs)
			return;
		current.isVisitedbfs = true;

		drawCircle(current.x, current.y, drawObj.BLACK, 0.2);
		drawObj.show(solveSpeed);
		if (current.x == size - 1 && current.y == size - 1)
			reachedGoal = true;
		if (new Random().nextBoolean()) {
			if (current.upBoolean)
				Rec_DFS(current.upCell);
			if (current.leftBoolean)
				Rec_DFS(current.leftCell);
			if (current.rightBoolean)
				Rec_DFS(current.rightCell);
			if (current.downBoolean)
				Rec_DFS(current.downCell);

			if (reachedGoal)
				return;
		} else {

			if (current.rightBoolean)
				Rec_DFS(current.rightCell);
			if (current.upBoolean)
				Rec_DFS(current.upCell);
			if (current.leftBoolean)
				Rec_DFS(current.leftCell);
			if (current.downBoolean)
				Rec_DFS(current.downCell);

			if (reachedGoal)
				return;
		}
		drawCircle(current.x, current.y, drawObj.WHITE, 0.2);
		drawObj.show(solveSpeed);
	}

	public void BFS() {// added
		Queue<Cell> queueForBFS = new LinkedList<Cell>();
		queueForBFS.add(cells[0][0]);
		Cell current;
		while (!queueForBFS.isEmpty()) {
			current = queueForBFS.remove();
			current.isVisitedbfs = true;
			drawCircle(current.x, current.y, drawObj.BOOK_LIGHT_BLUE, 0.2);
			if (current.upBoolean && !(current.upCell.isVisitedbfs)) {
				queueForBFS.add(current.upCell);
			}
			if (current.leftBoolean && !(current.leftCell.isVisitedbfs)) {
				queueForBFS.add(current.leftCell);
			}
			if (current.rightBoolean && !(current.rightCell.isVisitedbfs)) {
				queueForBFS.add(current.rightCell);
			}
			if (current.downBoolean && !(current.downCell.isVisitedbfs)) {
				queueForBFS.add(current.downCell);
			}
			drawObj.show(solveSpeed);
		}
	}

	private void drawCircle(int x, int y, Color penColor, double size) {// added
		drawObj.setPenColor(penColor);
		drawObj.filledCircle(x + 1.5, y + 1.5, size);
	}

	private Cell It_DFS(Cell current) {// added

		current.isVisiteddfs = true;

		drawCircle(current.x, current.y, drawObj.BLACK, 0.2);
		drawObj.show(solveSpeed);
		if (current.upBoolean && !(current.upCell.isVisiteddfs))
			return current.upCell;
		else if (current.leftBoolean && !(current.leftCell.isVisiteddfs))
			return current.leftCell;
		else if (current.rightBoolean && !(current.rightCell.isVisiteddfs))
			return current.rightCell;
		else if (current.downBoolean && !(current.downCell.isVisiteddfs))
			return current.downCell;
		return null;
	}

	private void It_BFS(Cell current) {// added
		current.isVisitedbfs = true;
		drawCircle(current.x, current.y, drawObj.BOOK_LIGHT_BLUE, 0.2);
		if (current.upBoolean && !(current.upCell.isVisitedbfs)) {
			queueForBFS.add(current.upCell);
			queueForBFS_fullpath.add(current.upCell);
		}
		if (current.leftBoolean && !(current.leftCell.isVisitedbfs)) {
			queueForBFS.add(current.leftCell);
			queueForBFS_fullpath.add(current.leftCell);
		}
		if (current.rightBoolean && !(current.rightCell.isVisitedbfs)) {
			queueForBFS.add(current.rightCell);
			queueForBFS_fullpath.add(current.rightCell);
		}
		if (current.downBoolean && !(current.downCell.isVisitedbfs)) {
			queueForBFS.add(current.downCell);
			queueForBFS_fullpath.add(current.downCell);
		}
		drawObj.show(solveSpeed);
	}

	public void bi_directional() {// added
		Cell re;
		Stack<Cell> st_dfs = new Stack<Cell>();

		queueForBFS.add(cells[size - 1][size - 1]);
		queueForBFS_fullpath.add(cells[size - 1][size - 1]);

		st_dfs.push(cells[0][0]);
		st_dfs_fullpath.push(cells[0][0]);
		while (!Compare()) {
			It_BFS(queueForBFS.remove());
			re = It_DFS(st_dfs.peek());
			if (re == null) {
				st_dfs.pop();
			} else {
				st_dfs.push(re);
				st_dfs_fullpath.push(re);
			}
		}
	}

	private boolean Compare() {// added
		for (int i = 0; i < st_dfs_fullpath.size(); i++) {
			if (queueForBFS_fullpath.contains(st_dfs_fullpath.get(i)))
				return true;
		}
		return false;
	}
}