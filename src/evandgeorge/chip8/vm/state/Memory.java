package evandgeorge.chip8.vm.state;

import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

import java.util.Arrays;

public class Memory {

	private static final int[] HEX_SPRITE_DATA = new int[] {
			0xF0, 0x90, 0x90, 0x90, 0xF0, 0x20, 0x60, 0x20, 0x20, 0x70,
			0xF0, 0x10, 0xF0, 0x80, 0xF0, 0xF0, 0x10, 0xF0, 0x10, 0xF0,
			0x90, 0x90, 0xF0, 0x10, 0x10, 0xF0, 0x80, 0xF0, 0x10, 0xF0,
			0xF0, 0x80, 0xF0, 0x90, 0xF0, 0xF0, 0x10, 0x20, 0x40, 0x40,
			0xF0, 0x90, 0xF0, 0x90, 0xF0, 0xF0, 0x90, 0xF0, 0x10, 0xF0,
			0xF0, 0x90, 0xF0, 0x90, 0x90, 0xE0, 0x90, 0xE0, 0x90, 0xE0,
			0xF0, 0x80, 0x80, 0x80, 0xF0, 0xE0, 0x90, 0x90, 0x90, 0xE0,
			0xF0, 0x80, 0xF0, 0x80, 0xF0, 0xF0, 0x80, 0xF0, 0x80, 0x80};

	public static final int ROM_BEGIN_OFFSET = 0x200;
	public static final int SIZE = 4096;
	private static final int PROGRAM_MEMORY_SIZE = SIZE - ROM_BEGIN_OFFSET;

	private Unsigned8Bit[] bytes;

	public static final Memory nullMemory;
	static {
		nullMemory = new Memory(new Unsigned8Bit[SIZE]);
		Arrays.fill(nullMemory.bytes, Unsigned8Bit.zero);

		for(int address = 0; address < HEX_SPRITE_DATA.length; address++)
			nullMemory.bytes[address] = new Unsigned8Bit(HEX_SPRITE_DATA[address]);
	}


	private Memory(Unsigned8Bit[] bytes) {
		this.bytes = bytes;
	}

	public Unsigned8Bit getByte(Unsigned16Bit address) {
		return bytes[address.asInt()];
	}

	public Unsigned16Bit get16BitValue(Unsigned16Bit address) {
		return new Unsigned16Bit(bytes[address.asInt()], bytes[address.asInt() + 1]);
	}

	public Unsigned8Bit[] getBytes(Unsigned16Bit startAddress, Unsigned8Bit numBytes) {
		return Arrays.copyOfRange(bytes, startAddress.asInt(), startAddress.asInt() + numBytes.asInt());
	}

	public Transformer createTransformer() {
		return new Transformer(this);
	}

	public static class Transformer {
		private Unsigned8Bit[] programMemory;

		private Transformer(Memory original) {
			this.programMemory = Arrays.copyOf(original.bytes, original.bytes.length);
		}

		public Transformer setByte(Unsigned16Bit address, Unsigned8Bit value) {
			if(address.asInt() >= ROM_BEGIN_OFFSET) {
				programMemory[address.asInt()] = value;
			} else if (address.asInt() < ROM_BEGIN_OFFSET){
				throw new IndexOutOfBoundsException(String.format(
						"Address 0x%s cannot be written to. Memory locations 0x000 - 0x511 are reserved for the virtual machine.",
						address.toHexString()
				));
			} else {
				throw new IndexOutOfBoundsException(String.format(
						"Address 0x%s out of bounds. Program address space is 0x200 - 0xFFF", address.toHexString()));
			}

			return this;
		}

		public Transformer setBytes(Unsigned16Bit beginAddress, Unsigned8Bit[] bytes) {
			Unsigned16Bit currentAddress = beginAddress;
			for(Unsigned8Bit unsignedByte : bytes) {
				setByte(currentAddress, unsignedByte);
				currentAddress = currentAddress.incremented();
			}

			return this;
		}

		public Memory transform() {
			return new Memory(programMemory);
		}
	}

	private static Unsigned8Bit[] createCharacterMemory() {
		Unsigned8Bit[] characterMemory = new Unsigned8Bit[HEX_SPRITE_DATA.length];

		for(int i = 0; i < HEX_SPRITE_DATA.length; i++)
			characterMemory[i] = new Unsigned8Bit(HEX_SPRITE_DATA[i]);

		return characterMemory;
	}

	public static Unsigned16Bit characterSpriteLocation(Unsigned8Bit character) {
		return new Unsigned16Bit(character.asInt() * HEX_SPRITE_DATA.length / 16);
	}
}
