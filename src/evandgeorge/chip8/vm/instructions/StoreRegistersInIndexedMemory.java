package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.OneRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Fx55 - LD [I], Vx
 * Store registers V0 through Vx in memory starting at location I.
 *
 * The interpreter copies the values of registers V0 through Vx
 * into memory, starting at the address in I.
 **----------------------------------------------------------------**/

public class StoreRegistersInIndexedMemory extends OneRegisterInstruction {

	public StoreRegistersInIndexedMemory(Unsigned8Bit register) {
		super(register);
	}

	@Override
	public String getDisassembly() {
		return String.format("LD [I], V%s", register.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Store registers V0 through V%s in memory starting at location I", register.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		return vm.createTransformer()
				.setMemory(vm.getMemory().createTransformer()
						.setBytes(vm.getProcessor().getIndexRegister(), vm.getProcessor().getRegisterRange(register))
						.transform())
				.setProcessor(vm.getProcessor().createTransformer()
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
