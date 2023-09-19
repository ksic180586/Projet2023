import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class World {
		
	public Rectangle[] blocks;
	public boolean[] isSolid;
	private BufferedImage[] blockImg;
	boolean topImage[] = new boolean[19];
	//32*25 tiles de 25*25 px
	final int arrayNum= 59;  // 19*(15-1) 
	
	//Block Images
	private BufferedImage BLOCK_SOL, BLOCK_SOL_TOP, BLOCK_SKY, BLOCK_STAGE1, BLOCK_STAGE2, BLOCK_STAGE3, BLOCK_BARRE;
	
	int x, y, yDirection;
	
	public World(){
		
		try {
			BLOCK_SOL= ImageIO.read(new File("sol_1.png"));
			BLOCK_SOL_TOP= ImageIO.read(new File("sol_top.png"));
			BLOCK_STAGE1= ImageIO.read(new File("Etage1.png")); // 352*128   32*11   32*4
			BLOCK_STAGE2= ImageIO.read(new File("Etage2.png"));
			BLOCK_STAGE3= ImageIO.read(new File("Etage3.png"));
			BLOCK_BARRE= ImageIO.read(new File("sol_top.png")); // 352*32
			
			BufferedImageOp op = new AffineTransformOp(AffineTransform.getScaleInstance(42.5, 160), null);
			BufferedImage BLOCK_SKY16= ImageIO.read(new File("tile_sky.png"));
			BLOCK_SKY = op.filter(BLOCK_SKY16, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		blocks = new Rectangle[arrayNum];
		blockImg = new BufferedImage[arrayNum]; 
		isSolid = new boolean[arrayNum];
		
		loadArrays();
	}
	
	public void print(String s) {
		System.out.println(s);
	}
	
	public void loadArrays(){
		int j=2;
		for (int i = 0; i < arrayNum; i++){
			if (x >= 19*32){
				x = 0;
				y += 32;
			}
			if(i == 21) {
				x = 0;
				y = 4*20*32;
			}
			if(i == 0) {
				blockImg[i] = BLOCK_SKY;
				isSolid[i] = false;
				blocks[i] = new Rectangle(x, y, 608, 2560);
			}else if (i <21) {
				x = 4*32;
				y = (i-1) *4*32;
				if(j%4 == 0) blockImg[i] = BLOCK_STAGE1;
				else if(j%4 == 1) blockImg[i] = BLOCK_STAGE2;
				else if(j%4 == 2) blockImg[i] = BLOCK_STAGE3;
				else blockImg[i] = BLOCK_STAGE2;
				j++;
				isSolid[i] = false;
				blocks[i] = new Rectangle(x, y, 352, 128);
				
			}else if (y <(4*20+1)*32){
				blockImg[i] = BLOCK_SOL_TOP;
				isSolid[i] = true;
				blocks[i] = new Rectangle(x, y, 32, 32);
			}else {
				blockImg[i] = BLOCK_SOL;
				isSolid[i] = true;
				blocks[i] = new Rectangle(x, y, 32, 32);
			}
			x += 32;
		}
		for (int i=0; i<19; i++) topImage[i] = true;
		setYDirection(-2176);
		moveMap();
		setYDirection(0);
	}
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		for (int i=0; i<arrayNum; i++){
			if(i>0 && i<=19) {
				RescaleOp rescaleOp;
				boolean topNum = false;
				if(topImage[i-1]) {
					float facteurEclaircissement = 1f;
					if((i-1)%5 == 0) {
						facteurEclaircissement = 1.5f;
						Font font = new Font("SansSerif", Font.BOLD, 24);
				        g2.setFont(font);
				        g2.setColor(Color.RED);
						topNum = true;
					}
					rescaleOp = new RescaleOp(facteurEclaircissement, 0, null);
				} else {
					// Créer un objet RescaleOp pour rendre l'image transparente
			        float[] scales = { 1f, 1f, 1f, 0f }; // Mettre la valeur alpha à 0
			        float[] offsets = { 0f, 0f, 0f, 0f };
			        rescaleOp = new RescaleOp(scales, offsets, null);
				}
				BufferedImage blockImg2 = rescaleOp.filter(blockImg[i], null);
				g2.drawImage(blockImg2,blocks[i].x, blocks[i].y,null);
				if(topNum) g2.drawString((21-i)+"", blocks[i].x+blocks[i].width/2, blocks[i].y+blocks[i].height/2);
				topNum = false;
			}
			else {
				g2.setComposite(AlphaComposite.SrcOver);
				g2.drawImage(blockImg[i],blocks[i].x, blocks[i].y,null);
			}
		}	
	}
	public void moveMap(){
		for (Rectangle r : blocks) r.y += yDirection;
	}
	
	public void stopMoveMap(){
		setYDirection(0);
	}
	
	public void setYDirection(int dir){
		// blocks[18] est le 3e étage en partant du bas
		if(blocks[18].y + dir >= 0) yDirection = dir;
		else yDirection = 0;
	}
	
	public void destroyBlock(int blockNum) {
		blocks[blockNum] = new Rectangle(-100, -100, 0, 0);
		isSolid[blockNum] = false;
	}
}
