package evandgeorge.chip8.vm.state;

import evandgeorge.chip8.vm.types.Unsigned8Bit;

import java.util.Random;

public class RandomNumberGenerator {
	private final Unsigned8Bit currentNumber;

	public RandomNumberGenerator(int seed) {
		Random random = new Random(seed);
		currentNumber = new Unsigned8Bit(random.nextInt(256));
	}

	public RandomNumberGenerator nextState() {
		return new RandomNumberGenerator(currentNumber.asInt());
	}

	public Unsigned8Bit getRandomByte() {
		return currentNumber;
	}
}
