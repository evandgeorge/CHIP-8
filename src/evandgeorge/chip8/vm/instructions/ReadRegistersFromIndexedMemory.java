package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.OneRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Fx65 - LD Vx, [I]
 * Read registers V0 through Vx from memory starting at location I.
 *
 * The interpreter reads values from memory starting at location I
 * into registers V0 through Vx.
 **----------------------------------------------------------------**/

public class ReadRegistersFromIndexedMemory extends OneRegisterInstruction {

	public ReadRegistersFromIndexedMemory(Unsigned8Bit register) {
		super(register);
	}

	@Override
	public String getDisassembly() {
		return String.format("LD V%s, [I]", register.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Read registers V0 through V%s from memory starting at location I", register.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var processorTransformer = vm.getProcessor().createTransformer().advanceProgramCounter();

		for(int i = 0; i <= register.asInt(); i++) {
			var address = vm.getProcessor().getIndexRegister().plus(new Unsigned8Bit(i));
			processorTransformer.setMainRegister(i, vm.getMemory().getByte(address));
		}

		return vm.createTransformer()
				.setProcessor(processorTransformer.transform())
				.transform();
	}
}
