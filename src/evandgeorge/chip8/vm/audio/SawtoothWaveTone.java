package evandgeorge.chip8.vm.audio;

public class SawtoothWaveTone extends Sound {

	//construct a saw tooth wave tone
	public SawtoothWaveTone(float frequency) {
		super(generateBuzzerData(frequency));
	}

	//create the saw tooth wave data of a given frequency
	private static byte[] generateBuzzerData(float frequency) {
		byte[] data = new byte[samplesPerCycle(frequency)];

		//make the first half of the cycle the max value, and the other half the min value to make square wave
		for(int i = 0; i < data.length; i++)
			data[i] = (byte) (Byte.MIN_VALUE + (Byte.MAX_VALUE - Byte.MIN_VALUE) * i / data.length);

		return data;
	}

}
