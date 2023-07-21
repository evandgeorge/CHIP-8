package evandgeorge.chip8.vm.types;

public class Sprite {

	/**----------------------------------------------------------------**
	 * CHIP-8 Sprites in memory are represented as consecutive bytes
	 * where each byte represents a row of 8 pixels. Each bit of a
	 * byte represents pixels in the row.
	 **----------------------------------------------------------------**/

	public static final int SPRITE_WIDTH = 8;
	Unsigned8Bit[] bytes;

	/* Construct the sprite from the bytes in memory */
	public Sprite(Unsigned8Bit[] bytes) {
		this.bytes = bytes;
	}

	/* Returns true if the pixel at (x, y) is drawn */
	public boolean getPixel(int x, int y) {
		return bytes[y].getBit(x);
	}

	/* Height of the sprite */
	public int getHeight() {
		return bytes.length;
	}
}
