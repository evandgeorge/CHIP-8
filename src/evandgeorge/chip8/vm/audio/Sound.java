package evandgeorge.chip8.vm.audio;

public class Sound {

	//sound data
	protected byte[] data;

	//construct a sound from its data
	public Sound(byte[] data) {
		this.data = data;
	}

	//create and return a new source stream for this sound
	public SourceStream createSource(int bufferSize) {
		return new SourceStream(bufferSize);
	}

	public class SourceStream {
		//number of bytes read thus far from the stream
		private int bytesRead = 0;

		//sound buffer
		private byte[] buffer;

		//create a source stream for sound
		private SourceStream(int bufferSize) {
			buffer = new byte[bufferSize];
		}

		//returns the size of the buffer returned by getNextBuffer()
		public int getBufferSize() {
			return buffer.length;
		}

		//returns a buffer with the next bytes in it
		public synchronized byte[] getNextBuffer() {
			for(int i = 0; i < buffer.length; i++)
				buffer[i % buffer.length] = data[(i + bytesRead) % data.length];

			bytesRead += buffer.length;
			return buffer;
		}
	}

	//computes the number of samples per cycle based on the sample rate and frequency
	static int samplesPerCycle(float frequency) {
		return Math.round(AudioOutput.AUDIO_FORMAT.getSampleRate() / frequency);
	}
}
