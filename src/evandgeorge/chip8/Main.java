package evandgeorge.chip8;


import evandgeorge.chip8.vm.audio.AudioOutput;
import evandgeorge.chip8.vm.audio.SawtoothWaveTone;
import evandgeorge.chip8.vm.audio.SquareWaveTone;
import evandgeorge.chip8.vm.audio.Sound;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Program;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, InterruptedException, LineUnavailableException {
		Program program = Program.loadFromROM(Paths.get("Pong [Paul Vervalin, 1990].ch8"));
		VirtualMachine vm = VirtualMachine.createFromProgram(program);
	}

}
