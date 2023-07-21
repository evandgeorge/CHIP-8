package evandgeorge.chip8.vm.types;

public class Unsigned8Bit {
	/* Number of bits in this type */
	private static final int BITS = 8;

	/* Max value that can be stored in 8 bits */
	private static final int MAX_VALUE = (1 << BITS) - 1;

	/* Unsigned8Bit literals */
	public static final Unsigned8Bit zero = new Unsigned8Bit(0);
	public static final Unsigned8Bit one = new Unsigned8Bit(1);

	/* Numeric value this Unsigned8Bit represents */
	private final int value;

	/* Create an Unsigned8Bit with a given value */
	/* Throw IllegalArgumentException if the value cannot be properly represented with 8 bits */
	public Unsigned8Bit(int value) {
		this.value = value & bitMask();

		if(this.value != value)
			throw new IllegalArgumentException(String.format("Cannot represent value %d in an 8-bit unsigned integer", value));
	}

	/* Create an Unsigned8Bit with from a signed primitive byte */
	/* Throw IllegalArgumentException if the value cannot be properly represented with 8 bits */
	public Unsigned8Bit(byte signedByte) {
		this.value = Byte.toUnsignedInt(signedByte);
	}

	/* Returns value of this as an int */
	public int asInt() {
		return value;
	}

	/* Return new Unsigned16Bit represented the sum of this and the addend */
	/* Will overflow as intended to match CHIP-8 behavior */
	public Unsigned8Bit plus(Unsigned8Bit addend) {
		int trueSum = this.asInt() + addend.asInt();
		int truncatedSum = trueSum & bitMask();

		return new Unsigned8Bit(truncatedSum);
	}

	/* Return new Unsigned16Bit represented the difference of this and the subtrahend */
	/* Will underflow as intended to match CHIP-8 behavior */
	public Unsigned8Bit minus(Unsigned8Bit subtrahend) {
		int trueDifference = this.asInt() - subtrahend.asInt();
		int wrappingDifference = trueDifference < 0 ? (MAX_VALUE + trueDifference + 1) : trueDifference;

		return new Unsigned8Bit(wrappingDifference);
	}


	/* Creates and returns a new Unsigned8Bit representing this value plus one */
	public Unsigned8Bit incremented() {
		return this.plus(Unsigned8Bit.one);
	}

	/* Creates and returns a new Unsigned8Bit representing this value minus one */
	public Unsigned8Bit decremented() {
		return this.minus(Unsigned8Bit.one);
	}

	/* Return new Unsigned8Bit with value of this OR operand */
	public Unsigned8Bit bitwiseOR(Unsigned8Bit operand) {
		return new Unsigned8Bit(this.asInt() | operand.asInt());
	}

	/* Return new Unsigned8Bit with value of this AND operand */
	public Unsigned8Bit bitwiseAND(Unsigned8Bit operand) {
		return new Unsigned8Bit(this.asInt() & operand.asInt());
	}

	/* Return new Unsigned8Bit with value of this XOR operand */
	public Unsigned8Bit bitwiseXOR(Unsigned8Bit operand) {
		return new Unsigned8Bit(this.asInt() ^ operand.asInt());
	}

	/* Returns new Unsigned8Bit that represents this shifted right once */
	public Unsigned8Bit shiftRight() {
		return new Unsigned8Bit(this.asInt() / 2);
	}

	/* Returns new Unsigned8Bit that represents this shifted left once */
	public Unsigned8Bit shiftLeft() {
		//mask so the previous most significant digit vanishes as expected
		return new Unsigned8Bit((this.asInt() * 2) & bitMask());
	}

	/* Returns true if the least significant bit is 1, false if it is 0 */
	public boolean leastSignificantBit() {
		return getBit(BITS - 1);
	}

	/* Returns true if the ith bit from the left is '1', false if it is '0' */
	public boolean getBit(int i) {
		if(i < 0 || i >= BITS)
			throw new IllegalArgumentException("Cannot get bit " + BITS + " of an 8-bit value.");

		int rightHandIndex = BITS - i - 1;
		int ithBitMask = 1 << rightHandIndex;

		return (this.asInt() & ithBitMask) != 0;
	}

	/* Returns the 2-digit hex representation of this Unsigned8Bit */
	public String toHexString() {
		String hexString = Integer.toHexString(this.asInt()).toUpperCase();

		return hexString.length() < 2 ? "0" + hexString : hexString;
	}

	/* Returns the 8-bit binary representation of this Unsigned8Bit */
	public String toBinaryString() {
		String binStringManyZeros = "0000000" + Integer.toBinaryString(this.asInt()).toUpperCase();

		return binStringManyZeros.substring(binStringManyZeros.length() - 8);
	}
	
	/* Creates a bit mask for the last 8 bits of an int */
	private static int bitMask() {
		int mask = 0;

		for(int i = 0; i < Unsigned8Bit.BITS; i++) {
			mask <<= 1;
			mask++;
		}

		return mask;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Unsigned8Bit) {
			return this.asInt() == ((Unsigned8Bit) obj).asInt();
		} else if (obj instanceof Unsigned16Bit) {
			System.out.println("!!!");
			return this.asInt() == ((Unsigned16Bit) obj).asInt();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return Integer.toHexString(value).toUpperCase();
	}
}
