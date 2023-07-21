package evandgeorge.chip8.vm.instructions.types;

import evandgeorge.chip8.vm.instructions.Instruction;
import evandgeorge.chip8.vm.types.Unsigned16Bit;

public abstract class AddressInstruction implements Instruction {

	public final Unsigned16Bit address;

	public AddressInstruction(Unsigned16Bit address) {
		this.address = address;
	}
}
