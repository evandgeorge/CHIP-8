package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.TwoRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 8xy1 - OR Vx, Vy
 * Set Vx = Vx OR Vy.
 *
 * Performs a bitwise OR on the values of Vx and Vy, then stores the
 * result in Vx. A bitwise OR compares the corresponding bits from
 * two values, and if either bit is 1, then the same bit in the
 * result is also 1. Otherwise, it is 0.
 **----------------------------------------------------------------**/

public class TwoRegisterBitwiseOR extends TwoRegisterInstruction {

	public TwoRegisterBitwiseOR(Unsigned8Bit registerOne, Unsigned8Bit registerTwo) {
		super(registerOne, registerTwo);
	}

	@Override
	public String getDisassembly() {
		return String.format("OR V%s, V%s", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s = V%s | V%s", registerOne.toString(), registerTwo.toString(), registerTwo.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var vx = vm.getProcessor().getMainRegister(registerOne.asInt());
		var vy = vm.getProcessor().getMainRegister(registerOne.asInt());

		return vm.createTransformer().setProcessor(vm.getProcessor().createTransformer()
				.setMainRegister(registerOne.asInt(), vx.bitwiseOR(vy))
				.advanceProgramCounter().transform())
				.transform();
	}
}
