package evandgeorge.chip8.vm.instructions.types;

import evandgeorge.chip8.vm.instructions.Instruction;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

public abstract class RegisterConstantInstruction implements Instruction {

	public final Unsigned8Bit register;
	public final Unsigned8Bit constant;

	public RegisterConstantInstruction(Unsigned8Bit register, Unsigned8Bit constant) {
		this.register = register;
		this.constant = constant;
	}
}
