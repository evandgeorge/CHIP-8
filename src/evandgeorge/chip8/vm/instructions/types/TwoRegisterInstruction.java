package evandgeorge.chip8.vm.instructions.types;

import evandgeorge.chip8.vm.instructions.Instruction;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

public abstract class TwoRegisterInstruction implements Instruction {

	public final Unsigned8Bit registerOne, registerTwo;

	public TwoRegisterInstruction(Unsigned8Bit registerOne, Unsigned8Bit registerTwo) {
		this.registerOne = registerOne;
		this.registerTwo = registerTwo;
	}
}
