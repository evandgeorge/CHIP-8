package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.TwoRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 8xy6 - SHR Vx {, Vy}
 * Set Vx = Vx SHR 1.
 *
 * If the least-significant bit of Vx is 1, then VF is set to 1, otherwise 0. Then Vx is divided by 2.
 **----------------------------------------------------------------**/

public class ShiftRegisterRight extends TwoRegisterInstruction {

	public ShiftRegisterRight(Unsigned8Bit registerOne, Unsigned8Bit registerTwo) {
		super(registerOne, registerTwo);
	}

	@Override
	public String getDisassembly() {
		return String.format("SHR V%s {, V%s}", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s = V%s >> 1", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var vx = vm.getProcessor().getMainRegister(registerOne.asInt());

		return vm.createTransformer().setProcessor(vm.getProcessor().createTransformer()
				.setMainRegister(registerOne.asInt(), vx.shiftRight())
				.setFlag(vx.leastSignificantBit())
				.advanceProgramCounter()
				.transform()).transform();
	}
}
