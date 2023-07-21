package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.RegisterConstantInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 7xkk - ADD Vx, byte
 * Set Vx = Vx + kk.
 *
 * Adds the value kk to the value of register Vx, then stores the
 * result in Vx.
 **----------------------------------------------------------------**/

public class AddConstantToRegister extends RegisterConstantInstruction {

	public AddConstantToRegister(Unsigned8Bit register, Unsigned8Bit constant) {
		super(register, constant);
	}

	@Override
	public String getDisassembly() {
		return String.format("ADD V%s, $%s", register.toString(), constant.toHexString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s = V%s + %s.", register.toString(), register.toString(), constant.toHexString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		Unsigned8Bit newValue = vm.getProcessor().getMainRegister(register.asInt()).plus(constant);

		return vm.createTransformer()
				.setProcessor(vm.getProcessor()
						.createTransformer()
						.setMainRegister(register.asInt(), newValue)
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
