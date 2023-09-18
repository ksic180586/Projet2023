import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

public class Player {
	private World world;
	
	private Rectangle playerRect;
	private Image playerImg;
	
	protected int xDirection, iEtage = 20;
	float yDirection, yDirection2 = -1;
	
	boolean topSpawn = false, topMonter = false;
	
	public Player(World world){
		this.world = world;
		Image playerImg16 = new ImageIcon("player.png").getImage();
		playerImg = playerImg16.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		playerRect = new Rectangle(world.blocks[iEtage].x+32,world.blocks[20].y+3*32,32,32);
	}
	
	void setXDirection(int d){
		xDirection = d;
	}
	
	public void setYDirection(int d){
		yDirection = d;
	}
	public void update(){
		move();
		checkForCollision();
	}
	
	public void print(String s) {
		System.out.println(s);
	}
	
	private void move(){
		playerRect.x += xDirection;
		yDirection2 += yDirection;
		if(Math.ceil(yDirection2) == -2) {
			playerRect.y += -1;
			yDirection2 += 1;
		}
		if(playerRect.y <= world.blocks[iEtage-1].y+3*32) yDirection = 0;
		if(playerRect.x >= world.blocks[iEtage].x+9*32) {
			xDirection = 0;
			if(!topMonter) {
				yDirection = -0.7f;
				topMonter = true;
			}
		}
	}
	
	private void checkForCollision(){
		
		
	}
	
	
	//Drawing methods
	public void draw(Graphics g){
		if(topSpawn) g.drawImage(playerImg, playerRect.x, playerRect.y, null);
	}
	
	//Mouse events
	public void mousePressed(MouseEvent e){
		if(!topSpawn) setXDirection(1);
		topSpawn = true;
	}
	public void mouseReleased(MouseEvent e){
		
	}
	public void mouseMoved(MouseEvent e){
		
	}
	public void mouseDragged(MouseEvent e){
		
	}

}
