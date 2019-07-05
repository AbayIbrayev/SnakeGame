import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import static java.lang.String.format;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

public class GameField extends JPanel implements ActionListener{
	
	private final int SIZE = 560;
	private final int DOT_SIZE = 16;
	private final int ALL_DOTS = 1225;
	private Image dot;
	private Image apple;
	private int appleX;
	private int appleY;
	private int[] x = new int[ALL_DOTS];
	private int[] y = new int[ALL_DOTS];
	private int dots;
	private Timer timer;
	private static int speed = 100;
	private boolean left = false;
	private boolean right = true;
	private boolean up = false;
	private boolean down = false;
	private boolean inGame = false;
	int score, hiScore;
	Font smallFont;

	public GameField(){
		setBackground(Color.LIGHT_GRAY);
		loadImages();
		initGame();
		addKeyListener(new FieldKeyListener());
		setFocusable(true);
		timer.stop();
	}



	public void initGame(){
		dots = 3;
		for (int i = 0; i < dots; i++) {
			x[i] = 48 - i * DOT_SIZE;
			y[i] = 48;
		}
		timer = new Timer(speed,this);
		timer.start();
		createApple();
		if (score > hiScore)
			hiScore = score;
		score = 0;
	}

	public void createApple(){
		appleX = new Random().nextInt(20)*DOT_SIZE;
		appleY = new Random().nextInt(20)*DOT_SIZE;
	}

	public void loadImages(){
		ImageIcon iia = new ImageIcon("apple.png");
		apple = iia.getImage();
		ImageIcon iid = new ImageIcon("dot.png");
		dot = iid.getImage();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gg = (Graphics2D) g;
		if(inGame){
			g.drawImage(apple,appleX,appleY,this);
			for (int i = 0; i < dots; i++) {
				g.drawImage(dot,x[i],y[i],this);
			}
			drawScore(g);
		}
		else {
			String str = "SNAKE GAME";
			String str2 = "Press ENTER to start the game";
			g.setFont(new Font("SansSerif", Font.BOLD, 25));
			g.setColor(Color.red);
			g.drawString(str,210,SIZE/2 - 50);
			g.setColor(Color.white);
			g.drawString(str2,116,SIZE/2);
			String str3 = "Press M to start the music";
			g.drawString(str3,135,SIZE);
		}
	}

	public void move(){
		for (int i = dots; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		if(left){
			x[0] -= DOT_SIZE;
		}
		if(right){
			x[0] += DOT_SIZE;
		} if(up){
			y[0] -= DOT_SIZE;
		} if(down){
			y[0] += DOT_SIZE;
		}
	}

	public void checkApple(){
		if(x[0] == appleX && y[0] == appleY){
			dots++;
			createApple();
			score++;
		}
	}

	public void checkCollisions(){
		for (int i = dots; i > 0 ; i--) {
			if(i>4 && x[0] == x[i] && y[0] == y[i]){
				inGame = false;
				JOptionPane.showMessageDialog(this,"Congratulations! You lost."); 
			}
		}

		if(x[0]>SIZE){
			inGame = false;
			JOptionPane.showMessageDialog(this,"Congratulations! You lost.");
		}
		if(x[0]<0){
			inGame = false;
			JOptionPane.showMessageDialog(this,"Congratulations! You lost.");
		}
		if(y[0]>SIZE){
			inGame = false;
			JOptionPane.showMessageDialog(this,"Congratulations! You lost.");
		}
		if(y[0]<0){
			inGame = false;
			JOptionPane.showMessageDialog(this,"Congratulations! You lost.");
		}
		if (!inGame) {
			timer.stop();
		}
	}
	void drawScore(Graphics gg) {
		int h = getHeight();
		gg.setColor(Color.red);
		String s = format("high score: %d    score: %d", hiScore, score);
		gg.setFont(new Font("SansSerif", Font.BOLD, 20));
		gg.drawString(s, 30, h - 30);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(inGame){
			checkApple();
			checkCollisions();
			move();
		}
		repaint();
	}

	public void music() {
		try {
			File file = new File("funk.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			//Thread.sleep(30000);
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	class FieldKeyListener extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_LEFT && !right){
				left = true;
				up = false;
				down = false;
			}
			if(key == KeyEvent.VK_RIGHT && !left){
				right = true;
				up = false;
				down = false;
			}

			if(key == KeyEvent.VK_UP && !down){
				right = false;
				up = true;
				left = false;
			}
			if(key == KeyEvent.VK_DOWN && !up){
				right = false;
				down = true;
				left = false;
			}
			if ((key == KeyEvent.VK_ENTER) && (inGame == false)) {
				inGame = true;
				left = false;
				right = true;
				up = false;
				down = false;
				initGame();
			}
			if(key == KeyEvent.VK_M){
				music();
			}
		}
	}
}
