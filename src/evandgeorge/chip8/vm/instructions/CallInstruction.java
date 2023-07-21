package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.AddressInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;

/**----------------------------------------------------------------**
 * 2nnn - CALL addr
 * Call subroutine at nnn.
 *
 * The interpreter increments the stack pointer, then puts the current PC on the top of the stack. The PC is then set to nnn.
 **----------------------------------------------------------------**/

public class CallInstruction extends AddressInstruction {

	public CallInstruction(Unsigned16Bit address) {
		super(address);
	}

	@Override
	public String getDisassembly() {
		return "CALL $" + address.toHexString();
	}

	@Override
	public String getDescription() {
		return "The interpreter increments the stack pointer, then puts the current PC on the top of the stack. The PC is then set to 0x" + address.toHexString();
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
