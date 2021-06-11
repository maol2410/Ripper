import java.awt.Color;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private byte[][] boardWallsHorizontal; // 16x17 horizontal walls
	private byte[][] boardWallsVertical; // 17x16 vertical walls
	private byte[][] boardFloor; // 16x16 floors
	private int startPixelX = 10;
	private int startPixelY = 10;
	private int tileSize = 30;
	private BufferedImage[] image;
	private boolean waitingForPlayers;

	public GamePanel() {
		waitingForPlayers = false;
		boardWallsHorizontal = new byte[16][17];
		for (int x = 0; x < boardWallsHorizontal.length; x++) {
			for (int y = 0; y < boardWallsHorizontal[x].length; y++) {
				boardWallsHorizontal[x][y] = 1;
			}
		}
		boardWallsVertical = new byte[17][16];
		for (int x = 0; x < boardWallsVertical.length; x++) {
			for (int y = 0; y < boardWallsVertical[x].length; y++) {
				boardWallsVertical[x][y] = 1;
			}
		}
		boardFloor = new byte[16][16];
		for (int x = 0; x < boardFloor.length; x++) {
			for (int y = 0; y < boardFloor[x].length; y++) {
				boardFloor[x][y] = 1;

			}
		}
		initImage();
	}

	private void initImage() {
		try {
			image = new BufferedImage[255];
			// Player
			image[4] = ImageIO.read(new File("../Image/player1.png"));
			image[5] = ImageIO.read(new File("../Image/player2.png"));

			// Monsters
			image[6] = ImageIO.read(new File("../Image/monster1.png"));
			image[7] = ImageIO.read(new File("../Image/monster2.png"));
			image[8] = ImageIO.read(new File("../Image/monster3.png"));
			image[9] = ImageIO.read(new File("../Image/monster4.png"));
			image[10] = ImageIO.read(new File("../Image/monster5.png"));
			image[11] = ImageIO.read(new File("../Image/monster6.png"));

			// Monster Boss
			image[12] = ImageIO.read(new File("../Image/monsterBoss1.png"));
			image[13] = ImageIO.read(new File("../Image/monsterBoss2.png"));
			image[14] = ImageIO.read(new File("../Image/monsterBoss3.png"));

			// Items
			image[15] = ImageIO.read(new File("../Image/HealthPotion.png"));
			image[16] = ImageIO.read(new File("../Image/One-Handed-Sword.png"));
			image[17] = ImageIO.read(new File("../Image/Two-Handed-Sword.png")); 
			image[18] = ImageIO.read(new File("../Image/LightArmor.png"));
			image[19] = ImageIO.read(new File("../Image/MediumArmor.png"));
			image[20] = ImageIO.read(new File("../Image/HeavyArmor.png"));
			image[21] = ImageIO.read(new File("../Image/Shield.png"));

		} catch (IOException e) {

			System.out.println("This is relatede to Image: " + e.getMessage());
		}
	}

	private void drawFloor(Graphics g) {

		int pixelX = startPixelX + 1;
		int pixelY = startPixelY + 1;
		for (int x = 0; x < boardFloor.length; x++) {
			for (int y = 0; y < boardFloor[x].length; y++) {
				int imageType = boardFloor[x][y];
				if (image[imageType] != null) {
					g.drawImage(image[imageType], pixelX, pixelY, this);
					// System.out.println("X: " + x + " Y: " + y + " Img: " + imageType);
				}
				pixelY += tileSize;
			}
			pixelX += tileSize;
			pixelY = startPixelY + 1;
		}
	}

	private void drawWalls(Graphics g) {
		// 1 är helt tomt
		// 2 inga väggar, men har ett spelbart golv
		// 3 Vanlig vägg
		// 4 Dörr
		int pixelX = startPixelX;
		int pixelY = startPixelY;

		for (int x = 0; x < boardWallsHorizontal.length; x++) {
			for (int y = 0; y < boardWallsHorizontal[x].length; y++) {
				g.setColor(Color.WHITE);
				int wallSize = 0;
				if (boardWallsHorizontal[x][y] == CONST.WALL_EMPTY_INSIDE) {
					wallSize = 1;
				} else if (boardWallsHorizontal[x][y] == CONST.WALL)
					wallSize = 4;
				else if (boardWallsHorizontal[x][y] == CONST.WALL_DOOR) {
					g.setColor(Color.BLUE);
					wallSize = 4;
				}
				g.fillRect(pixelX, pixelY, tileSize, wallSize);
				pixelY += tileSize;
			}
			pixelX += tileSize;
			pixelY = startPixelY;
		}
		pixelX = startPixelX;
		for (int x = 0; x < boardWallsVertical.length; x++) {
			for (int y = 0; y < boardWallsVertical[x].length; y++) {
				g.setColor(Color.WHITE);
				int wallSize = 0;
				if (boardWallsVertical[x][y] == CONST.WALL_EMPTY_INSIDE) {
					wallSize = 1;
				} else if (boardWallsVertical[x][y] == CONST.WALL)
					wallSize = 4;
				else if (boardWallsVertical[x][y] == CONST.WALL_DOOR) {
					g.setColor(Color.BLUE);
					wallSize = 4;
				}
				g.fillRect(pixelX, pixelY, wallSize, tileSize);
				pixelY += tileSize;
				// System.out.println(boardWallsVertical[x][y]);
			}
			pixelX += tileSize;
			pixelY = startPixelY;
		}
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 500, 545);
		if (waitingForPlayers) {
			g.setColor(Color.GREEN);
			g.drawString("Waiting for other player. Blame it on them!", getX(), getY());
			waitingForPlayers = false;
		} else {
			drawFloor(g);
			drawWalls(g);
		}
		g.dispose();
	}

	public void waitingForPlayers() {
		waitingForPlayers = true;
		repaint();
	}

	public boolean readWalls(byte[] bytes) {
		int index = 0;
		for (int y = 0; y < boardWallsHorizontal[0].length; y++) {
			for (int x = 0; x < boardWallsHorizontal.length; x++) {
				boardWallsHorizontal[x][y] = bytes[index++];
			}
		}
		for (int y = 0; y < boardWallsVertical[0].length; y++) {
			for (int x = 0; x < boardWallsVertical.length; x++) {
				boardWallsVertical[x][y] = bytes[index++];
			}
		}
		return true;
	}

	public boolean readFloor(byte[] bytes) {
		int index = 0;
		for (int y = 0; y < boardFloor[0].length; y++) {
			for (int x = 0; x < boardFloor.length; x++) {
				boardFloor[x][y] = bytes[index++];
			}
		}
		repaint();
		return true;
	}
}
