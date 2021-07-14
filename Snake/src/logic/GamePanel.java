package logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import entities.Direction;
import entities.Obstacle;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 1800;
	static final int SCREEN_HEIGHT = 1000;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE);
	static final int DELAY = 50;
	static final int OBSTACLE_GENERETION_DISTANCE = 500;
	static final String GAME_OVER = "Game Over";
	static final String SCORE = "Score: ";
	private int difficulty = 1;
	private Color scoreColor = Color.green;
	private Color snakeColor = new Color(45, 180, 0);
	private Color obstacleColor = Color.gray;

	int x[] = new int[GAME_UNITS];
	int y[] = new int[GAME_UNITS];
	ArrayList<Obstacle> obstacles = new ArrayList<>();

	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	Direction direction = Direction.RIGHT;
	Direction lastStepDirection = Direction.RIGHT;
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void restart() {
		x = new int[GAME_UNITS];
		y = new int[GAME_UNITS];
		obstacles.clear();
		difficulty = 1;
		scoreColor = Color.green;

		bodyParts = 6;
		applesEaten = 0;
		appleX = 0;
		appleY = 0;
		direction = Direction.RIGHT;
		newApple();
		running = true;
		repaint();
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public boolean isObstaclePossible(Obstacle obstacle) {
		if (getDistanceToSnakeHead(obstacle) < OBSTACLE_GENERETION_DISTANCE) {
			return false;
		}
		return isAppleOnObstacle(obstacle);
	}

	public boolean isAppleOnObstacle(Obstacle obstacle) {
		return !(appleX >= obstacle.getX() && appleY >= obstacle.getY()
				&& appleX <= obstacle.getX() + obstacle.getWidth() && appleY <= obstacle.getY() + obstacle.getHeight());
	}

	public boolean isTouchingObstacle() {
		
		return false;
	}

	public int getDistanceToSnakeHead(Obstacle obstacle) {
		int xDistance = obstacle.getX() - x[0] < 0 ? (obstacle.getX() - x[0]) * (-1) : obstacle.getX() - x[0];
		int yDistance = obstacle.getY() - y[0] < 0 ? (obstacle.getY() - y[0]) * (-1) : obstacle.getY() - y[0];
		return Math.max(xDistance, yDistance);
	}

	public void newApple() {
		appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
		appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
	}

	public void newObstacle() {
		int positionX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
		int positionY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
		int width = (random.nextInt(3) + difficulty) * UNIT_SIZE;
		int height = (random.nextInt(3) + difficulty) * UNIT_SIZE;
		Obstacle obstacle = new Obstacle(positionX, positionY, width, height);
		if (isObstaclePossible(obstacle)) {
			if (obstacles.size() < 5) {
				obstacles.add(obstacle);
			} else {
				obstacles.set(random.nextInt(4), obstacle);
			}
		}
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		lastStepDirection = direction;
		switch (direction) {
		case UP:
			y[0] = y[0] - UNIT_SIZE;
			break;
		case DOWN:
			y[0] = y[0] + UNIT_SIZE;
			break;
		case LEFT:
			x[0] = x[0] - UNIT_SIZE;
			break;
		case RIGHT:
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
			newObstacle();
		}
	}

	public void checkCollisions() {
		// checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// check if head touches left border
		if (x[0] < 0) {
			running = false;
		}
		// checks if head touches right border
		if (x[0] > SCREEN_WIDTH - 1) {
			running = false;
		}
		// checks if head touches top border
		if (y[0] < 0) {
			running = false;
		}
		// checks if head touches down border
		if (y[0] > SCREEN_HEIGHT - 1) {
			running = false;
		}
		// checks if head touches an obstacle
		if (isTouchingObstacle()) {
			running = false;
		}

		if (!running) {
			timer.stop();
		}
	}

	public void checkDifficulty() {
		if (applesEaten > 0) {// 5
			timer.setDelay(50);
			difficulty = 2;
			scoreColor = new Color(128, 255, 0);
		}
		if (applesEaten > 1) {// 25
			timer.setDelay(50);
			difficulty = 3;
			scoreColor = new Color(213, 255, 0);
		}
		if (applesEaten > 2) {// 50
			timer.setDelay(47);
			difficulty = 4;
			scoreColor = new Color(255, 192, 0);
		}
		if (applesEaten > 3) {// 80
			timer.setDelay(43);
			difficulty = 5;
			scoreColor = new Color(255, 85, 0);
			obstacleColor = new Color(40, 40, 40);
		}
		if (applesEaten > 4) {// 100
			timer.setDelay(40);
			difficulty = 5;
			scoreColor = new Color(255, 0, 0);
			snakeColor = new Color(0, 45, 0);
			obstacleColor = new Color(20, 20, 20);
		}
	}

	public void gameOver(Graphics g) {
		timer.stop();
		// Score text
		drawScore(g);
		// Game Over text
		drawGameOver(g);
	}

	public void draw(Graphics g) {
		if (running) {
			drawSnake(g);
			drawApple(g);
			drawObstacles(g);
			drawScore(g);
		} else {
			gameOver(g);
		}
	}

	public void drawSnake(Graphics g) {
		for (int i = 0; i < bodyParts; i++) {
			if (i == 0) {
				g.setColor(Color.green);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			} else {
				g.setColor(snakeColor);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
	}

	public void drawApple(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
	}

	public void drawObstacles(Graphics g) {
		for (Obstacle obstacle : obstacles) {
			g.setColor(obstacleColor);
			g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
		}
	}

	public void drawScore(Graphics g) {
		g.setColor(scoreColor);
		g.setFont(new Font("Consolas", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString(SCORE + applesEaten, (SCREEN_WIDTH - metrics.stringWidth(SCORE + applesEaten)) / 2,
				g.getFont().getSize());
	}

	public void drawGameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Consolas", Font.BOLD, 120));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString(GAME_OVER, (SCREEN_WIDTH - metrics.stringWidth(GAME_OVER)) / 2, SCREEN_HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log();
		if (running) {
			move();
			checkApple();
			checkCollisions();
			checkDifficulty();
		}
		repaint();
	}

	public void log() {
		System.out.println("HEAD [" + x[0] / UNIT_SIZE + "] [" + y[0] / UNIT_SIZE + "], direction = " + direction
				+ ", bodyParts = " + bodyParts + ", applesEaten =" + applesEaten + ", APPLE [" + appleX / UNIT_SIZE
				+ "] [" + appleY / UNIT_SIZE + "], ");
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (lastStepDirection != Direction.RIGHT) {
					direction = Direction.LEFT;
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (lastStepDirection != Direction.LEFT) {
					direction = Direction.RIGHT;
				}
				break;
			case KeyEvent.VK_UP:
				if (lastStepDirection != Direction.DOWN) {
					direction = Direction.UP;
				}
				break;
			case KeyEvent.VK_DOWN:
				if (lastStepDirection != Direction.UP) {
					direction = Direction.DOWN;
				}
				break;
			case KeyEvent.VK_SPACE:
				restart();
				break;
			}
		}
	}
}
