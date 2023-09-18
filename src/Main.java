import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GamePanel gp;
	JPanel barreInfo = new JPanel(new GridLayout(2,0));
	JLabel info[] = new JLabel[5];
	String label[] = {"nombre de pièces : 0","nombre de héros : 0","nombre de soldats : 0",
			"étage maximum : 0","dernier étage scellé : 0"};

	public Main(){
		setLayout(new BorderLayout());
		
		for(int i=0; i<5; i++) {
			info[i] = new JLabel(label[i]);
			barreInfo.add(info[i]);
		}
		
		gp = new GamePanel();
		
		add(barreInfo, BorderLayout.NORTH);
		add(gp, BorderLayout.CENTER);
		
		setSize(608,480+barreInfo.getPreferredSize().height); // 32*19 = 608 32*15 = 480
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args){
		new Main();
	}
	
}