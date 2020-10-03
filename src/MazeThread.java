public class MazeThread implements Runnable {
	int mazeSize, solveSpeed, generateSpeed, selectedAlgorithm;
	Thread runner;

	@Override
	public void run() {
		Maze maze = new Maze(mazeSize, solveSpeed);
		Canvas canvas = new Canvas(maze);
		canvas.plot(generateSpeed);
		maze.setCanvas(canvas);// added
		if (selectedAlgorithm == 0)
			maze.start_Rec_DFS();// added
		else if (selectedAlgorithm == 1)
			maze.BFS();// added
		else if (selectedAlgorithm == 2)
			maze.bi_directional();// added
	}

	public MazeThread(int mazeSize, int solveSpeed, int generateSpeed, int selectedAlgorithm) {
		this.mazeSize = mazeSize;
		this.solveSpeed = solveSpeed;
		this.generateSpeed = generateSpeed;
		this.selectedAlgorithm = selectedAlgorithm;
		runner = new Thread(this);
		runner.start();
	}
}