package ui_component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Canvas {

	public final Color BLACK = Color.BLACK;
	public final Color BLUE = Color.BLUE;
	public final Color CYAN = Color.CYAN;
	public final Color DARK_GRAY = Color.DARK_GRAY;
	public final Color GRAY = Color.GRAY;
	public final Color GREEN = Color.GREEN;
	public final Color LIGHT_GRAY = Color.LIGHT_GRAY;
	public final Color MAGENTA = Color.MAGENTA;
	public final Color ORANGE = Color.ORANGE;
	public final Color PINK = Color.PINK;
	public final Color RED = Color.RED;
	public final Color WHITE = Color.WHITE;
	public final Color YELLOW = Color.YELLOW;
	public final Color BOOK_BLUE = new Color(9, 90, 166);
	public final Color BOOK_LIGHT_BLUE = new Color(103, 198, 243);
	public final Color BOOK_RED = new Color(150, 35, 31);
	private final Color DEFAULT_PEN_COLOR = BLACK;
	private final Color DEFAULT_CLEAR_COLOR = WHITE;
	private Color penColor;
	private final int DEFAULT_SIZE = 512;
	private int width = DEFAULT_SIZE;
	private int height = DEFAULT_SIZE;
	private final double DEFAULT_PEN_RADIUS = 0.002;
	private double penRadius;
	private boolean defer = false;
	private final double BORDER = 0.00;
	private final double DEFAULT_XMIN = 0.0;
	private final double DEFAULT_XMAX = 1.0;
	private final double DEFAULT_YMIN = 0.0;
	private final double DEFAULT_YMAX = 1.0;
	private double xmin, ymin, xmax, ymax;
	private Object mouseLock = new Object();
	private BufferedImage offscreenImage, onscreenImage;
	private Graphics2D offscreen, onscreen;
	private JFrame frame;
	private Maze maze;

	public Canvas() {
		init();
	}

	public Canvas(Maze maze) {
		init();
		this.maze = maze;
		setXscale(0, maze.size + 2);
		setYscale(0, maze.size + 2);
		frame.setTitle("Maze Generator & Solver - " + maze.size + " x " + maze.size);
	}

	public void setCanvasSize(int w, int h) {
		if (w < 1 || h < 1)
			throw new IllegalArgumentException("width and height must be positive");
		width = w;
		height = h;
		init();
	}

	private void init() {
		if (frame != null)
			frame.setVisible(false);
		frame = new JFrame();
		offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		onscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		offscreen = offscreenImage.createGraphics();
		onscreen = onscreenImage.createGraphics();
		setXscale();
		setYscale();
		offscreen.setColor(DEFAULT_CLEAR_COLOR);
		offscreen.fillRect(0, 0, width, height);
		setPenColor();
		setPenRadius();
		clear();

		// add antialiasing
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		offscreen.addRenderingHints(hints);

		// frame stuff
		ImageIcon icon = new ImageIcon(onscreenImage);
		JLabel draw = new JLabel(icon);

		frame.setContentPane(draw);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Maze Generator & Solver");
		frame.pack();
		frame.requestFocusInWindow();
		frame.setVisible(true);
	}

	public void setXscale() {
		setXscale(DEFAULT_XMIN, DEFAULT_XMAX);
	}

	public void setYscale() {
		setYscale(DEFAULT_YMIN, DEFAULT_YMAX);
	}

	public void setXscale(double min, double max) {
		double size = max - min;
		synchronized (mouseLock) {
			xmin = min - BORDER * size;
			xmax = max + BORDER * size;
		}
	}

	public void setYscale(double min, double max) {
		double size = max - min;
		synchronized (mouseLock) {
			ymin = min - BORDER * size;
			ymax = max + BORDER * size;
		}
	}

	public void setScale(double min, double max) {
		double size = max - min;
		synchronized (mouseLock) {
			xmin = min - BORDER * size;
			xmax = max + BORDER * size;
			ymin = min - BORDER * size;
			ymax = max + BORDER * size;
		}
	}

	private double scaleX(double x) {
		return width * (x - xmin) / (xmax - xmin);
	}

	private double scaleY(double y) {
		return height * (ymax - y) / (ymax - ymin);
	}

	private double factorX(double w) {
		return w * width / Math.abs(xmax - xmin);
	}

	private double factorY(double h) {
		return h * height / Math.abs(ymax - ymin);
	}

	public void clear() {
		clear(DEFAULT_CLEAR_COLOR);
	}

	public void clear(Color color) {
		offscreen.setColor(color);
		offscreen.fillRect(0, 0, width, height);
		offscreen.setColor(penColor);
		draw();
	}

	public double getPenRadius() {
		return penRadius;
	}

	public void setPenRadius() {
		setPenRadius(DEFAULT_PEN_RADIUS);
	}

	public void setPenRadius(double r) {
		if (r < 0)
			throw new IllegalArgumentException("pen radius must be nonnegative");
		penRadius = r;
		float scaledPenRadius = (float) (r * DEFAULT_SIZE);
		BasicStroke stroke = new BasicStroke(scaledPenRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		offscreen.setStroke(stroke);
	}

	public Color getPenColor() {
		return penColor;
	}

	public void setPenColor() {
		setPenColor(DEFAULT_PEN_COLOR);
	}

	public void setPenColor(Color color) {
		penColor = color;
		offscreen.setColor(penColor);
	}

	public void setPenColor(int red, int green, int blue) {
		if (red < 0 || red >= 256)
			throw new IllegalArgumentException("amount of red must be between 0 and 255");
		if (green < 0 || green >= 256)
			throw new IllegalArgumentException("amount of green must be between 0 and 255");
		if (blue < 0 || blue >= 256)
			throw new IllegalArgumentException("amount of blue must be between 0 and 255");
		setPenColor(new Color(red, green, blue));
	}

	public void line(double x0, double y0, double x1, double y1) {
		offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
		draw();
	}

	private void pixel(double x, double y) {
		offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
	}

	public void circle(double x, double y, double r) {
		if (r < 0)
			throw new IllegalArgumentException("circle radius must be nonnegative");
		double xs = scaleX(x);
		double ys = scaleY(y);
		double ws = factorX(2 * r);
		double hs = factorY(2 * r);
		if (ws <= 1 && hs <= 1)
			pixel(x, y);
		else
			offscreen.draw(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
		draw();
	}

	public void filledCircle(double x, double y, double r) {
		if (r < 0)
			throw new IllegalArgumentException("circle radius must be nonnegative");
		double xs = scaleX(x);
		double ys = scaleY(y);
		double ws = factorX(2 * r);
		double hs = factorY(2 * r);
		if (ws <= 1 && hs <= 1)
			pixel(x, y);
		else
			offscreen.fill(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
		draw();
	}

	public void show(int t) {
		defer = false;
		draw();
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			System.out.println("Error sleeping");
		}
		defer = true;
	}

	public void show() {
		defer = false;
		draw();
	}

	private void draw() {
		if (defer)
			return;
		onscreen.drawImage(offscreenImage, 0, 0, null);
		frame.repaint();
	}

	public void plot(int solveSpeed) {
		setPenColor(RED);
		filledCircle(maze.size + 0.5, maze.size + 0.5, 0.375);
		filledCircle(1.5, 1.5, 0.375);
		setPenColor(BLACK);
		for (int i = 0; i < maze.size; i++) {
			for (int j = 0; j < maze.size; j++) {
				if (!maze.cells[i][j].downBoolean)
					line(i + 1, j + 1, i + 2, j + 1);
				show(solveSpeed);
				if (!maze.cells[i][j].upBoolean)
					line(i + 1, j + 2, i + 2, j + 2);
				show(solveSpeed);
				if (!maze.cells[i][j].leftBoolean)
					line(i + 1, j + 1, i + 1, j + 2);
				show(solveSpeed);
				if (!maze.cells[i][j].rightBoolean)
					line(i + 2, j + 1, i + 2, j + 2);
				show(solveSpeed);
			}
		}
	}
}