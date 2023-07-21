package evandgeorge.chip8.vm.instructions.types;

import evandgeorge.chip8.vm.instructions.Instruction;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

public abstract class TwoRegisterConstantInstruction implements Instruction {

	public final Unsigned8Bit registerOne, registerTwo, constant;

	public TwoRegisterConstantInstruction(Unsigned8Bit registerOne, Unsigned8Bit registerTwo, Unsigned8Bit constant) {
		this.registerOne = registerOne;
		this.registerTwo = registerTwo;
		this.constant = constant;
	}
}
