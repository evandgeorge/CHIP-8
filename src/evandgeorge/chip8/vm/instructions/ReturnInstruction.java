package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.state.Processor;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;

/**----------------------------------------------------------------**
 * 00EE - RET
 *
 * Return from a subroutine.
 *
 * The interpreter sets the program counter to the address at the top of the stack, then subtracts 1 from the stack pointer.
 **----------------------------------------------------------------**/

public class ReturnInstruction implements Instruction {

	@Override
	public String getDisassembly() {
		return "RET";
	}

	@Override
	public String getDescription() {
		return "Return from a subroutine by setting the program counter to the result of popping the stack, and then advancing it";
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var returnAddress = vm.getProcessor().topOfStack();

		var processorTransformer = vm.getProcessor().createTransformer();
		processorTransformer.setProgramCounter(returnAddress.incrementedTwice());
		processorTransformer.popStack();

		Processor newProcessor = processorTransformer.transform();

		return vm.createTransformer().setProcessor(newProcessor).transform();
	}
}
