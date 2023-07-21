package evandgeorge.chip8.vm.state;

import evandgeorge.chip8.vm.types.Sprite;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

public class Display {

	public static final int WIDTH = 64;
	public static final int HEIGHT = 32;

	public static final Display blankDisplay = new Display();

	private final boolean[][] raster;
	private final boolean pixelsErased;

	private Display() {
		this.raster = new boolean[WIDTH][HEIGHT];
		this.pixelsErased = false;
	}

	protected Display(boolean[][] raster, boolean pixelsErased) {
		this.raster = raster;
		this.pixelsErased = pixelsErased;
	}

	public boolean getPixel(int x, int y) {
		return raster[x][y];
	}

	public boolean getPixelErasedFlag() {
		return pixelsErased;
	}

	public Display drawSprite(Unsigned8Bit x0, Unsigned8Bit y0, Sprite sprite) {
		boolean[][] newRaster = copyRaster();
		boolean pixelsErasedDrawingSprite = false;

		for(int spriteY = 0; spriteY < sprite.getHeight(); spriteY++) {
			for(int spriteX = 0; spriteX < Sprite.SPRITE_WIDTH; spriteX++) {
				WrappedCoordinate wrappedCoords = new WrappedCoordinate(x0.asInt() + spriteX, y0.asInt() + spriteY);

				if(newRaster[wrappedCoords.x][wrappedCoords.y] && sprite.getPixel(spriteX, spriteY))
					pixelsErasedDrawingSprite = true;

				newRaster[wrappedCoords.x][wrappedCoords.y] = newRaster[wrappedCoords.x][wrappedCoords.y] ^ sprite.getPixel(spriteX, spriteY);
			}
		}

		return new Display(newRaster, pixelsErasedDrawingSprite);
	}

	private boolean[][] copyRaster() {
		boolean[][] copy = new boolean[WIDTH][HEIGHT];

		for(int x = 0; x < WIDTH; x++)
			System.arraycopy(raster[x], 0, copy[x], 0, HEIGHT);

		return copy;
	}

	private static class WrappedCoordinate {
		public final int x, y;

		private WrappedCoordinate(int x, int y) {
			this.x = x % WIDTH;
			this.y = y % HEIGHT;
		}
	}
}
