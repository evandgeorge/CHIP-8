package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.TwoRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 8xy0 - LD Vx, Vy
 * Set Vx = Vy.
 *
 * Stores the value of register Vy in register Vx.
 **----------------------------------------------------------------**/

public class SetRegisterToRegister extends TwoRegisterInstruction {

	public SetRegisterToRegister(Unsigned8Bit registerOne, Unsigned8Bit registerTwo) {
		super(registerOne, registerTwo);
	}

	@Override
	public String getDisassembly() {
		return String.format("LD V%s, V%s", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s = V%s.", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		Unsigned8Bit newValue = vm.getProcessor().getMainRegister(registerTwo.asInt());

		return vm.createTransformer()
				.setProcessor(vm.getProcessor().createTransformer()
						.setMainRegister(registerOne.asInt(), newValue)
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
