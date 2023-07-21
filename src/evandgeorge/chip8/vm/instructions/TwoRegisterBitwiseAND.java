package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.TwoRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 8xy2 - AND Vx, Vy
 * Set Vx = Vx AND Vy.
 *
 * Performs a bitwise AND on the values of Vx and Vy, then stores
 * the result in Vx. A bitwise AND compares the corrseponding bits
 * from two values, and if both bits are 1, then the same bit in the
 * result is also 1. Otherwise, it is 0.
 **----------------------------------------------------------------**/

public class TwoRegisterBitwiseAND extends TwoRegisterInstruction {

	public TwoRegisterBitwiseAND(Unsigned8Bit registerOne, Unsigned8Bit registerTwo) {
		super(registerOne, registerTwo);
	}

	@Override
	public String getDisassembly() {
		return String.format("AND V%s, V%s", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s = V%s & V%s", registerOne.toString(), registerTwo.toString(), registerTwo.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var vx = vm.getProcessor().getMainRegister(registerOne.asInt());
		var vy = vm.getProcessor().getMainRegister(registerOne.asInt());

		return vm.createTransformer().setProcessor(vm.getProcessor().createTransformer()
				.setMainRegister(registerOne.asInt(), vx.bitwiseAND(vy))
				.advanceProgramCounter()
				.transform()).transform();
	}
}
