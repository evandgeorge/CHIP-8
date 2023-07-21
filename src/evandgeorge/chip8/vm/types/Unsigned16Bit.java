package evandgeorge.chip8.vm.types;

public class Unsigned16Bit {
	/* Number of bits in this type */
	private static final int BITS = 16;

	/* Max value that can be stored in 16 bits */
	private static final int MAX_VALUE = (1 << BITS) - 1;

	/* Unsigned16Bit 0 literal */
	public static final Unsigned16Bit zero = new Unsigned16Bit(0);

	/* Numeric value this Unsigned16Bit represents */
	private final int value;

	/* Create an Unsigned16Bit with a given value */
	/* Throw IllegalArgumentException if the value cannot be properly represented with 16 bits */
	public Unsigned16Bit(int value) {
		this.value = value & bitMask(BITS);

		if(this.value != value)
			throw new IllegalArgumentException(String.format("Cannot represent value %d in a 16-bit unsigned integer", value));
	}

	/* Create an Unsigned16Bit from two bytes */
	public Unsigned16Bit(Unsigned8Bit left, Unsigned8Bit right) {
		this.value = (left.asInt() << 8) + right.asInt();

		assert this.value <= MAX_VALUE;
	}

	/* Return new Unsigned16Bit represented the sum of this and the addend */
	/* Will overflow as intended to match CHIP-8 behavior */
	public Unsigned16Bit plus(Unsigned16Bit addend) {
		int trueSum = this.asInt() + addend.asInt();
		int truncatedSum = trueSum & bitMask(BITS);

		return new Unsigned16Bit(truncatedSum);
	}

	/* Returns value of this as an int */
	public int asInt() {
		return value;
	}

	/* Return new Unsigned16Bit represented the difference of this and the subtrahend */
	/* Will underflow as intended to match CHIP-8 behavior */
	public Unsigned16Bit minus(Unsigned16Bit subtrahend) {
		int trueDifference = this.asInt() - subtrahend.asInt();
		int wrappingDifference = trueDifference < 0 ? (MAX_VALUE + trueDifference + 1) : trueDifference;

		return new Unsigned16Bit(wrappingDifference);
	}

	/* Return new Unsigned16Bit represented the sum of this and the addend */
	/* Will overflow as intended to match CHIP-8 behavior */
	public Unsigned16Bit plus(Unsigned8Bit addend) {
		int trueSum = this.asInt() + addend.asInt();
		int truncatedSum = trueSum & bitMask(BITS);

		return new Unsigned16Bit(truncatedSum);
	}

	/* Return new Unsigned16Bit represented the difference of this and the subtrahend */
	/* Will underflow as intended to match CHIP-8 behavior */
	public Unsigned16Bit minus(Unsigned8Bit subtrahend) {
		int trueDifference = this.asInt() - subtrahend.asInt();
		int wrappingDifference = trueDifference < 0 ? (MAX_VALUE + trueDifference) : trueDifference;

		return new Unsigned16Bit(wrappingDifference);
	}
	
	/* Creates and returns a new Unsigned16Bit value representing this value plus one */
	public Unsigned16Bit incremented() {
		return this.plus(new Unsigned16Bit(1));
	}

	/* Creates and returns a new Unsigned16Bit representing this value plus two */
	public Unsigned16Bit incrementedTwice() {
		return this.plus(new Unsigned16Bit(2));
	}

	/* Creates and returns a new Unsigned16Bit representing this value minus one */
	public Unsigned16Bit decremented() {
		return this.minus(new Unsigned16Bit(1));
	}

	/* Return the integer value of bits in the range [start, start + numBits) */
	public int getValueOfBitRange(int start, int numBits) {
		int valueOfBits = value >> (BITS - start - numBits);
		return valueOfBits & bitMask(numBits);
	}

	/* Returns the 4-digit hex representation of this Unsigned16Bit */
	public String toHexString() {
		String hexStringManyLeadingZeros = "000" + Integer.toHexString(this.asInt()).toUpperCase();

		return hexStringManyLeadingZeros.substring(hexStringManyLeadingZeros.length() - 4);
	}

	/* Returns the 16-bit binary representation of this Unsigned16Bit */
	public String toBinaryString() {
		String binStringManyZeros = "000000000000000" + Integer.toBinaryString(this.asInt());

		return binStringManyZeros.substring(binStringManyZeros.length() - 16);
	}

	/* Creates a bit mask for the last numBits bits of an int */
	private static int bitMask(int numBits) {
		int mask = 0;

		for(int i = 0; i < numBits; i++) {
			mask <<= 1;
			mask++;
		}

		return mask;
	}


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Unsigned8Bit) {
			System.out.println("!!!");
			return this.asInt() == ((Unsigned8Bit) obj).asInt();
		} else if (obj instanceof Unsigned16Bit) {
			return this.asInt() == ((Unsigned16Bit) obj).asInt();
		} else {
			return false;
		}
	}
}
