package evandgeorge.chip8.vm.types;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnsignedTypesTests {

	@Test
	public void testUnsigned8Bit_inCapacity() {
		int value = 0xFF;
		assertDoesNotThrow(() -> new Unsigned8Bit(value));

		Unsigned8Bit u8bit = new Unsigned8Bit(value);
		assertEquals(value, u8bit.asInt());
		assertEquals("FF", u8bit.toHexString());
	}

	@Test
	public void testUnsigned8Bit_exceedsCapacity() {
		int value = 0x100;
		assertThrows(IllegalArgumentException.class, () -> new Unsigned8Bit(value));
	}

	@Test
	public void testUnsigned16Bit_inCapacity() {
		int value = 0xFFFF;
		assertDoesNotThrow(() -> new Unsigned16Bit(value));

		Unsigned16Bit u16bit = new Unsigned16Bit(value);
		assertEquals(value, u16bit.asInt());
		assertEquals("FFFF", u16bit.toHexString());
	}

	@Test
	public void testUnsigned16Bit_exceedsCapacity() {
		int value = 0x10000;
		assertThrows(IllegalArgumentException.class, () -> new Unsigned16Bit(value));
	}

	@Test
	public void testAdd_16bit_noOverflow() {
		int a = 0x7D0;
		int b = 0x805;

		Unsigned16Bit sum = new Unsigned16Bit(a).plus(new Unsigned16Bit(b));

		assertEquals(a + b, sum.asInt());
	}

	@Test
	public void testAdd_16bit_overflow() {
		int a = 0xF0F0;
		int b = 0xF1F0;
		int truncatedSum = 0xE2E0;

		Unsigned16Bit sum = new Unsigned16Bit(a).plus(new Unsigned16Bit(b));

		assertEquals(truncatedSum, sum.asInt());
	}

	@Test
	public void testSubtract_8bit_noUnderflow() {
		int a = 0xFF;
		int b = 0x30;

		Unsigned8Bit difference = new Unsigned8Bit(a).minus(new Unsigned8Bit(b));
		assertEquals(a - b, difference.asInt());
	}

	@Test
	public void testSubtract_8bit_underflow() {
		int a = 0x0;
		int b = 0x01;
		int wrappedDifference = 0xFF;

		Unsigned8Bit difference = new Unsigned8Bit(a).minus(new Unsigned8Bit(b));
		assertEquals(wrappedDifference, difference.asInt());
	}


	@Test
	public void testSubtract_16bit_noUnderflow() {
		int a = 0xFFFF;
		int b = 0x3000;

		Unsigned16Bit difference = new Unsigned16Bit(a).minus(new Unsigned16Bit(b));
		assertEquals(a - b, difference.asInt());
	}

	@Test
	public void testSubtract_16bit_underflow() {
		int a = 0x0000;
		int b = 0x0001;
		int wrappedDifference = 0xFFFF;

		Unsigned16Bit difference = new Unsigned16Bit(a).minus(new Unsigned16Bit(b));
		assertEquals(wrappedDifference, difference.asInt());
	}

	@Test
	public void testIncremented() {
		Unsigned16Bit value = new Unsigned16Bit(30);
		assertEquals(value.asInt() + 1, value.incremented().asInt());
	}

	@Test
	public void testIncrementedTwice() {
		Unsigned16Bit value = new Unsigned16Bit(30);
		assertEquals(value.asInt() + 2, value.incrementedTwice().asInt());
	}

	@Test
	public void testDecremented() {
		Unsigned16Bit value = new Unsigned16Bit(30);
		assertEquals(value.asInt() - 1, value.decremented().asInt());
	}

	@Test
	public void testValueOfBitRange() {
		Unsigned16Bit value = new Unsigned16Bit(0b1101001001011000);
		assertEquals(0b100100, value.getValueOfBitRange(3, 6));
	}
	
}