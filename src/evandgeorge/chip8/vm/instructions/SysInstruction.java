package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.AddressInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;

/**----------------------------------------------------------------**
 * 0nnn - SYS addr
 *
 * Jump to a machine code routine at nnn.
 *
 * This instruction is only used on the old computers on which Chip-8
 * was originally implemented. It is ignored by modern interpreters.
 **----------------------------------------------------------------**/

public class SysInstruction extends AddressInstruction {

	public SysInstruction(Unsigned16Bit address) {
		super(address);
	}

	@Override
	public String getDisassembly() {
		return "SYS $" + address.toHexString();
	}

	@Override
	public String getDescription() {
		return "Jump to the machine code routine at 0x" + address.toHexString();
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var processorTransformer = vm.getProcessor().createTransformer();

		processorTransformer.pushToStack(
				vm.getProcessor()
						.getProgramCounter());

		processorTransformer.setProgramCounter(this.address);

		return vm.createTransformer().setProcessor(processorTransformer.transform()).transform();
	}
}
