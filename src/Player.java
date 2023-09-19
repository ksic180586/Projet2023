import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Player {
	private World world;
	
	private Rectangle playerRect;
	private Image playerImg;
	
	protected int xDirection, yDirection, iEtage = 20;
	float yAutoDirection, yAutoDirection2 = -1;
	
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
		if(world.blocks[18].y + d >= 0) yDirection = d;
		else yDirection = 0;
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
		playerRect.y += yDirection;
		
		// monte echelle bloque
		if(playerRect.y <= world.blocks[iEtage-1].y+3*32) {
			yAutoDirection = 0;
			yAutoDirection2 = -1;
			iEtage--;
			if(iEtage%2 == 1) setXDirection(-1);
			else setXDirection(1);
			topMonter = false;
		}
		
		// monte echelle
		yAutoDirection2 += yAutoDirection;
		if(Math.ceil(yAutoDirection2) == -2) {
			playerRect.y += -1;
			yAutoDirection2 += 1;
		}
		// etage bloque vers droite puis monte echelle 
		if((iEtage%2 == 0 && playerRect.x >= world.blocks[iEtage].x+9*32) ||
				(iEtage%2 == 1 && playerRect.x <= world.blocks[iEtage].x+32)){
			xDirection = 0;
			if(!topMonter) {
				yAutoDirection = -0.7f;
				topMonter = true;
			}
		}
	}
	
	private void checkForCollision(){
		
	}
	
	public void addHero() {
		if(!topSpawn) setXDirection(1);
		topSpawn = true;
	}
	
	//Drawing methods
	public void draw(Graphics g){
		if(topSpawn) g.drawImage(playerImg, playerRect.x, playerRect.y, null);
	}
}
