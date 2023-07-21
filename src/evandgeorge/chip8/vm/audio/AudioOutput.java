package evandgeorge.chip8.vm.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioOutput implements Closeable {
	//audio format to be used for source data lines
	static final AudioFormat AUDIO_FORMAT = new AudioFormat(4410, 8, 1, true, false);

	//source data line sound data will be written to
	private SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(AUDIO_FORMAT);

	//all source streams of playing sounds
	private Map<Sound, Sound.SourceStream> playingSoundsStreams = new HashMap<>();

	//audio playback routine and thread
	private AudioPlayer audioPlaybackRoutine = new AudioPlayer();
	private Thread audioPlaybackThread = new Thread(audioPlaybackRoutine);

	//factor all samples are multiplied by
	private double volumeFactor = 1;

	//constructs an AudioOutput
	public AudioOutput() throws LineUnavailableException { }

	//if the source data line is closed, create a new one and start it, otherwise resume the existing data line
	public synchronized void startAudio() throws LineUnavailableException {
		//if the source data line is closed create a new one and open it
		if(!sourceDataLine.isOpen()) {
			sourceDataLine = AudioSystem.getSourceDataLine(AUDIO_FORMAT);
			sourceDataLine.open();
		}

		//if audio playback has stopped create a new audio playback thread
		if(audioPlaybackRoutine.isStopped) {
			audioPlaybackRoutine = new AudioPlayer();
			audioPlaybackThread = new Thread(audioPlaybackRoutine);
		}

		//start source data line and audio playback thread
		sourceDataLine.start();
		audioPlaybackThread.start();
	}

	//stops the source data line and audio playback
	public synchronized void pauseAudio() throws InterruptedException {
		sourceDataLine.stop();
		audioPlaybackRoutine.stop();
	}

	//stops audio playback and closes the source data line
	public synchronized void stopAudio() {
		audioPlaybackRoutine.stop();
		sourceDataLine.close();
	}

	//begin playing a sound by creating a source stream of it and putting it into playingSoundsStreams
	public synchronized void startSound(Sound sound) {
		var newPlayingSoundsStreams = new HashMap<>(playingSoundsStreams);
		newPlayingSoundsStreams.put(sound, sound.createSource(sourceDataLine.getBufferSize()));
		playingSoundsStreams = newPlayingSoundsStreams;
	}

	//if the sound is playing, removing it from playingSoundsStreams, otherwise do nothing
	public synchronized void stopSound(Sound sound) {
		if(playingSoundsStreams.containsKey(sound)) {
			var newPlayingSoundsStreams = new HashMap<>(playingSoundsStreams);
			newPlayingSoundsStreams.remove(sound);
			playingSoundsStreams = newPlayingSoundsStreams;
		}
	}

	//set the volume factor
	public void setVolume(double volumeFactor) {
		this.volumeFactor = volumeFactor;
	}

	//applies the volume factor to each sample in the buffer
	private byte[] adjustVolume(byte[] buffer) {
		for(int i = 0; i < buffer.length; i++) {
			buffer[i] *= volumeFactor;
		}

		return buffer;
	}

	//close the sourceDataLine
	@Override
	public void close() throws IOException {
		sourceDataLine.close();
	}

	private class AudioPlayer implements Runnable {
		//if audio playback has been stopped by a call to stop()
		private volatile boolean isStopped = false;

		//write playing sounds' data to source data line until end() is called
		@Override
		public void run() {
			while(!isStopped) {
				for(var stream : playingSoundsStreams.values()) {
					sourceDataLine.write(adjustVolume(stream.getNextBuffer()), 0, stream.getBufferSize());
				}
			}
		}

		//stop audio player routine
		public void stop() {
			isStopped = true;
		}
	}
}
