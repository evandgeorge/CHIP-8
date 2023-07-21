package evandgeorge.chip8.vm.types;

import evandgeorge.chip8.vm.instructions.CallInstruction;
import evandgeorge.chip8.vm.instructions.Instruction;
import evandgeorge.chip8.vm.instructions.JumpInstruction;
import evandgeorge.chip8.vm.instructions.ReturnInstruction;
import evandgeorge.chip8.vm.instructions.types.AddressInstruction;
import evandgeorge.chip8.vm.state.Memory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Program {

	public final Unsigned8Bit[] romData;
	public final Unsigned16Bit[] instructionCodes;
	public final Instruction[] instructions;

	public Program(Unsigned8Bit[] romData) {
		this.romData = romData;
		this.instructionCodes = new Unsigned16Bit[romData.length / 2];
		this.instructions = new Instruction[romData.length / 2];

		for(int i = 0; i < instructions.length; i++) {
			instructionCodes[i] = new Unsigned16Bit(romData[2 * i], romData[2 * i + 1]);
			instructions[i] = Instruction.parseInstruction(instructionCodes[i]);
		}
	}

	public static final Program loadFromROM(Path filePath) throws IOException {
		byte[] fileBytes = Files.readAllBytes(filePath);
		Unsigned8Bit[] romData = new Unsigned8Bit[fileBytes.length];

		for (int i = 0; i < romData.length; i++)
			romData[i] = new Unsigned8Bit(fileBytes[i]);

		return new Program(romData);
	}
}
