package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.OneRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Fx1E - ADD I, Vx
 * Set I = I + Vx.
 *
 * The values of I and Vx are added, and the results are stored in I.
 **----------------------------------------------------------------**/

public class AddRegisterToIndex extends OneRegisterInstruction {

	public AddRegisterToIndex(Unsigned8Bit register) {
		super(register);
	}

	@Override
	public String getDisassembly() {
		return String.format("ADD I, V%s", register.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Set I = I + V%s", register.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var index = vm.getProcessor().getIndexRegister();
		var increment = vm.getProcessor().getMainRegister(register.asInt());

		return vm.createTransformer()
				.setProcessor(vm.getProcessor().createTransformer()
						.setIndexRegister((Unsigned16Bit) index.plus(increment))
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
