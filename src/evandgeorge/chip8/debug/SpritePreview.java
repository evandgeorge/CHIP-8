package evandgeorge.chip8.debug;

import evandgeorge.chip8.vm.types.Sprite;

import javax.swing.*;
import java.awt.*;

public class SpritePreview extends JPanel {

	private Sprite sprite;

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void clearSprite() {
		setSprite(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if(sprite != null) {
			int pixelWidth = Math.min(this.getWidth() / 8, this.getHeight() / sprite.getHeight());
			int x0 = (this.getWidth() - 8 * pixelWidth) / 2;
			int y0 = (this.getHeight() - sprite.getHeight() * pixelWidth) / 2;

			for(int spriteY = 0; spriteY < sprite.getHeight(); spriteY++) {
				for(int spriteX = 0; spriteX < 8; spriteX++) {
					g.setColor(sprite.getPixel(spriteX, spriteY) ? Color.WHITE : Color.BLACK);
					g.fillRect(x0 + spriteX * pixelWidth, y0 + spriteY * pixelWidth, pixelWidth, pixelWidth);
				}
			}
		}
	}
}
