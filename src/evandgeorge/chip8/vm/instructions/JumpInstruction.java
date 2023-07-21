package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.AddressInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;

/**----------------------------------------------------------------**
 * 1nnn - JP addr
 * Jump to location nnn.
 *
 * The interpreter sets the program counter to nnn.
 **----------------------------------------------------------------**/

public class JumpInstruction extends AddressInstruction {

	public JumpInstruction(Unsigned16Bit address) {
		super(address);
	}

	@Override
	public String getDisassembly() {
		return "JP $" + address.toHexString();
	}

	@Override
	public String getDescription() {
		return "Jump to location 0x" + address.toHexString();
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		return vm.createTransformer().setProcessor(
				vm.getProcessor().createTransformer().setProgramCounter(address).transform())
				.transform();
	}
}
