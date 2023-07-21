package evandgeorge.chip8.vm.audio;

public class SquareWaveTone extends Sound {

	//construct a square wave tone
	public SquareWaveTone(float frequency) {
		super(generateBuzzerData(frequency));
	}

	//create the square wave data of a given frequency
	private static byte[] generateBuzzerData(float frequency) {
		byte[] data = new byte[samplesPerCycle(frequency)];

		//make the first half of the cycle the max value, and the other half the min value to make square wave
		for(int i = 0; i < data.length; i++)
			data[i] = i < data.length / 2
					? Byte.MAX_VALUE
					: Byte.MIN_VALUE;

		return data;
	}

}
