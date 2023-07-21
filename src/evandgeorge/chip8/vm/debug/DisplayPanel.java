package evandgeorge.chip8.vm.debug;

import evandgeorge.chip8.vm.state.Display;

import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel {

	private static final int ASPECT_RATIO = 2;
	private static final int horizontalInsets = 2, verticalInsets = 2;
	private static final String noDisplayText = "Display";

	private Color foreground = Color.WHITE, background = Color.BLACK;
	private Display display;

	public DisplayPanel() {
		super();
		this.setPreferredSize(new Dimension(640 + 2 * horizontalInsets, 320 + 2 * verticalInsets));
		this.setForeground(foreground);
		this.setBackground(background);
	}

	@Override
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	@Override
	public void setBackground(Color background) {
		this.background = background;
	}

	public void update(Display display) {
		this.display = display;
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		int usableWidth, usableHeight, width, height, x0, y0, pixelWidth, pixelHeight;

		if((this.getWidth() - 2 * horizontalInsets) < ASPECT_RATIO * (this.getHeight() - 2 * verticalInsets)) {
			usableWidth = this.getWidth() - 2 * horizontalInsets;
			usableHeight = usableWidth / ASPECT_RATIO;
		} else {
			usableHeight = this.getHeight() - 2 * verticalInsets;
			usableWidth = usableHeight * ASPECT_RATIO;
		}

		pixelWidth = usableWidth / Display.WIDTH;
		pixelHeight = usableHeight / Display.HEIGHT;
		width = Display.WIDTH * pixelWidth;
		height = Display.HEIGHT * pixelHeight;

		x0 = (this.getWidth() - width) / 2;
		y0 = (this.getHeight() - height) / 2;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		super.paint(g2d);

		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawRect(x0 - 1, y0 - 1, width + 1, height + 1);

		if(display == null) {
			g2d.setColor(new Color(51, 51, 51));

			int textWidth = g2d.getFontMetrics().stringWidth(noDisplayText);
			int textHeight = g2d.getFont().getSize();
			g2d.drawString(noDisplayText, x0 + (width - textWidth) / 2 , y0 + (height + textHeight - 3) / 2);
		} else {
			for(int display_x = 0; display_x < Display.WIDTH; display_x++) {
				for(int display_y = 0; display_y < Display.HEIGHT; display_y++) {
					Color pixelColor = display != null && display.getPixel(display_x, display_y) ? foreground : background;
					g2d.setColor(pixelColor);
					g2d.fillRect(x0 + display_x * pixelWidth, y0 + display_y * pixelHeight, pixelWidth, pixelHeight);
				}
			}
		}

		g2d.dispose();
	}

}
