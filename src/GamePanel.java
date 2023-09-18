import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Double buffering
	private Image dbImage;
	private Graphics dbg;
	//JPanel variables
	static final int GWIDTH = 608, GHEIGHT = 480;   // 32*19 = 608 32*15 = 480     32 reserver a la barre
	static final Dimension gameDim = new Dimension(GWIDTH, GHEIGHT);
	//Game variables
	private Thread game;
	private volatile boolean running = false;
	private long period =6*1000000;//ms -> nano // sleeping time
	private static final int DELAYS_BEFORE_YIELD = 10;
	private static int previousY;
	private int ySpeed;
	
	//Game Objects
	World world;
	Player p1;
	
	public GamePanel(){
		world = new World();
		p1 = new Player(world);
		
		setPreferredSize(gameDim);
		setBackground(Color.WHITE);
		setFocusable(true);
		requestFocus();
		//Handle all key inputs from user
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_LEFT){
					p1.setXDirection(-1);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					p1.setXDirection(+1);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN){
//					p1.setYDirection(1);
					world.setYDirection(-1);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP){
					p1.setYDirection(-1);
				}
			}
			public void keyReleased(KeyEvent e){
				world.stopMoveMap();
				if (e.getKeyCode() == KeyEvent.VK_LEFT){
					p1.setXDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					p1.setXDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN){
					p1.setYDirection(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP){
					p1.setYDirection(0);
				}
			}
			public void keyTyped(KeyEvent e){
				
			}
		});
		addMouseListener(new  MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e){
				
			}
			@Override
			public void mousePressed(MouseEvent e){
				p1.mousePressed(e);
			}
			@Override
			public void mouseReleased(MouseEvent e){
				world.setYDirection(0);
			}
			@Override
			public void mouseClicked(MouseEvent e){
				
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e){
				p1.mouseMoved(e);
			}
			@Override
			public void mouseDragged(MouseEvent e){
				int y = e.getY();
	            if (y > previousY) {
	            	if(ySpeed<10) ySpeed++;
	            } else if (y < previousY) {
	            	if(-10<ySpeed) ySpeed--;
	            }
	            world.setYDirection(ySpeed);
	            previousY = y;
			}
			@Override
			public void mouseEntered(MouseEvent e){
			}
			@Override
			public void mouseExited(MouseEvent e){
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int notches = e.getWheelRotation();
				if (notches < 0) {
					if(ySpeed<10) ySpeed++;
				}else {
					if(-10<ySpeed) ySpeed--;
				}
				world.setYDirection(ySpeed);
			}
		});
	}
	
	public void addNotify(){
		super.addNotify();
		startGame();
	}
	
	public void startGame(){
		if(game == null || !running){
			game = new Thread(this);
			game.start();
			running = true;
		}
	}
	
	public void stopGame() {
		if (running){
			running = false;
		}
	}
	
	private void log(String s){
		System.out.println(s);
	}

	@Override
	public void run() {
		//<=> Thread.sleep(6);
		long beforeTime, afterTime, diff, sleepTime, overSleepTime = 0;
		int delays = 0;
				
		while (running){
			beforeTime =  System.nanoTime();
			
			gameUpdate();
			gameRender();
			paintScreen();
			
			afterTime = System.nanoTime();
			diff=afterTime-beforeTime;
			sleepTime = (period - diff) - overSleepTime;

			// If the sleep time is between 0 and the period, we can happily sleep
			if ( sleepTime < period && sleepTime > 0){
				try {
					game.sleep(sleepTime / 1000000L);
					overSleepTime = 0;
				} catch (InterruptedException ex) {
					Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			// The diff was greater than the period
			else if (diff > period){
				overSleepTime = diff - period;
			}
			// Accumulate the amount of delays and eventually yeild
			else if(++delays >= DELAYS_BEFORE_YIELD){
				game.yield();
				delays = 0;
				overSleepTime = 0;
			}
			// The loop took less time than expected, but we need to make up
			// for overSleepTime
			else{
				overSleepTime = 0;
			}
			// Print out game stats
			/*
			log(
			"beforeTime:     " + beforeTime + "\n" + 
			"afterTime:      " + afterTime + "\n" + 
			"diff:           " + diff + "\n" +
			"SleepTime:      " + sleepTime / 1000000L + "\n" +
			"overSleepTime:  " + overSleepTime / 1000000L + "\n" +
			"delays:         " + delays + "\n" 
			);
			*/
		}
		
	}
	
	private void gameUpdate(){
		if (running && game != null){
			world.moveMap();
			p1.update();
			
		}
	}

	private void gameRender(){
		if (dbImage == null) {// create the buffer
			dbImage = createImage(GWIDTH, GHEIGHT);
			if (dbImage == null) {
				System.err.println("dbImage is still null!");
				return;
			}
			else{
				dbg = dbImage.getGraphics();
			}
		}
		//Clear the screen
		dbg.setColor(Color.CYAN);
		dbg.fillRect(0, 0, GWIDTH, GHEIGHT);
		//Draw Game elements
		draw(dbg);
		
	}

	/* Draw all game content in this method */
	private void draw(Graphics g) {
		world.draw(g);
		p1.draw(g);
	}
	
	private void paintScreen(){
		Graphics g;
		try{
			g = this.getGraphics();
			if (dbImage != null && g != null){
				g.drawImage(dbImage, 0, 0, null);
			}
			Toolkit.getDefaultToolkit().sync(); // for some os such as linux
			g.dispose();
		}catch(Exception e){
			System.err.println(e);
		}
		
	}
	
	
}
