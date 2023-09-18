import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

public class Player {
	private World world;
	
	private Rectangle playerRect;
	private Image playerImg;
	
	protected int xDirection, yDirection;
	
	//Block variables
	private int hoverX, hoverY;
	private boolean hovering = false;
	
	private Weapon weapon;
	
	public Player(World world){
		this.world = world;
		Image playerImg16 = new ImageIcon("player.png").getImage();
		playerImg = playerImg16.getScaledInstance(32, 32, Image.SCALE_SMOOTH);;
		playerRect = new Rectangle(160,352,16,16);
		weapon = new Weapon(1);
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
	
	private void move(){
		playerRect.x += xDirection;
		playerRect.y += yDirection;
//		gravity();
	}
	
	private void gravity(){
		for (int i=0; i < world.arrayNum; i++){
			if (!world.isSolid[i]){
				setYDirection(1);				
			}
			else if (world.isSolid[i] && playerRect.intersects(world.blocks[i])){
				setYDirection(0);
			}
		}
		
	}
	
	private void checkForCollision(){
		
		
	}
	
	
	//Drawing methods
	public void draw(Graphics g){
		g.drawImage(playerImg, playerRect.x, playerRect.y, null);
//		g.setColor(Color.LIGHT_GRAY);
//		g.drawRect(playerRect.x, playerRect.y, playerRect.width, playerRect.height);
		if (hovering){
			drawBlockOutline(g);
		}
	}
	
	public void drawBlockOutline(Graphics g){
		g.setColor(Color.BLACK);
		g.drawRect(hoverX, hoverY, world.blocks[0].width, world.blocks[0].height);
	}
	
	
	
	//Mouse events
	public void mousePressed(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		int px = playerRect.x;
		int py = playerRect.y;
		
		int leftClick = MouseEvent.BUTTON1;
		int rightClick = MouseEvent.BUTTON3;
		for (int i=0; i< world.arrayNum; i++){
				if (e.getButton() == leftClick){
					if(weapon.isEquipped(Weapon.PICKAXE) &&
							   x > world.blocks[i].x && x < world.blocks[i].x + world.blocks[i].width &&
							   y > world.blocks[i].y && y < world.blocks[i].y + world.blocks[i].height &&
							   world.isSolid[i] &&  
							   (world.blocks[i].x + (world.blocks[i].width/2)) <= (px + (playerRect.width/2)) + weapon.WEAPON_RADIUS && 
							   (world.blocks[i].x + (world.blocks[i].width/2)) >= (px + (playerRect.width/2)) - weapon.WEAPON_RADIUS &&
							   (world.blocks[i].y + (world.blocks[i].height/2)) <= (py + (playerRect.height/2)) + weapon.WEAPON_RADIUS &&
							   (world.blocks[i].y + (world.blocks[i].height/2)) >= (py + (playerRect.height/2)) - weapon.WEAPON_RADIUS ){
						world.destroyBlock(i);
					}
				}
			else if (e.getButton() == rightClick){
				
			}
		}
		
	}
	public void mouseReleased(MouseEvent e){
		
	}
	public void mouseMoved(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		int px = playerRect.x;
		int py = playerRect.y;
		
		for (int i = 0; i < world.arrayNum; i++){
			if(weapon.isEquipped(Weapon.PICKAXE) &&
			   x > world.blocks[i].x && x < world.blocks[i].x + world.blocks[i].width &&
			   y > world.blocks[i].y && y < world.blocks[i].y + world.blocks[i].height &&
			   world.isSolid[i] &&  
			   (world.blocks[i].x + (world.blocks[i].width/2)) <= (px + (playerRect.width/2)) + weapon.WEAPON_RADIUS && 
			   (world.blocks[i].x + (world.blocks[i].width/2)) >= (px + (playerRect.width/2)) - weapon.WEAPON_RADIUS &&
			   (world.blocks[i].y + (world.blocks[i].height/2)) <= (py + (playerRect.height/2)) + weapon.WEAPON_RADIUS &&
			   (world.blocks[i].y + (world.blocks[i].height/2)) >= (py + (playerRect.height/2)) - weapon.WEAPON_RADIUS ){
				hovering = true;
				hoverX = world.blocks[i].x;
				hoverY = world.blocks[i].y;
				break; // avoid time consuming
			}
			else {
				hovering = false;
			}
		}
	}
	public void mouseDragged(MouseEvent e){
		
	}
	
	private class Weapon {
		public static final int UNARMED = 0;
		public static final int PICKAXE = 1;
		public static final int GUN = 2;
		
		public int CURRENT_WEAPON;
		
		public int WEAPON_RADIUS;
		
		public Weapon(int w){
			switch(w){
			default: 
				System.out.println("No weapon selected!");
				break;
			case UNARMED:
				CURRENT_WEAPON = UNARMED;
				WEAPON_RADIUS = 16;
				break;
			case PICKAXE:
				CURRENT_WEAPON = PICKAXE;
				WEAPON_RADIUS = 48;
				break;
			case GUN:
				CURRENT_WEAPON = GUN;
				WEAPON_RADIUS = 80;
				break;
			}
			
		}
	
		public void selectWeapon(int w){
			switch(w){
			default: 
				System.out.println("No weapon selected!");
				break;
			case UNARMED:
				CURRENT_WEAPON = UNARMED;
				WEAPON_RADIUS = 16;
				break;
			case PICKAXE:
				CURRENT_WEAPON = PICKAXE;
				WEAPON_RADIUS = 48;
				break;
			case GUN:
				CURRENT_WEAPON = GUN;
				WEAPON_RADIUS = 80;
				break;
			}
		}
		
		public boolean isEquipped(int w){
			if (w == CURRENT_WEAPON){
				return true;				
			}
			else
			{
				return false;
			}
		}
	}

}
