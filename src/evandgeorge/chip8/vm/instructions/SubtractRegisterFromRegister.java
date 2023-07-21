package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.TwoRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 8xy5 - SUB Vx, Vy
 * Set Vx = Vx - Vy, set VF = NOT borrow.
 *
 * If Vx > Vy, then VF is set to 1, otherwise 0. Then Vy is subtracted from Vx, and the results stored in Vx.
 **----------------------------------------------------------------**/

public class SubtractRegisterFromRegister extends TwoRegisterInstruction {

	public SubtractRegisterFromRegister(Unsigned8Bit registerOne, Unsigned8Bit registerTwo) {
		super(registerOne, registerTwo);
	}

	@Override
	public String getDisassembly() {
		return String.format("SUB V%s, V%s", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s = V%s - V%s, set VF = NOT borrow", registerOne.toString(), registerTwo.toString(), registerTwo.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var vx = vm.getProcessor().getMainRegister(registerOne.asInt());
		var vy = vm.getProcessor().getMainRegister(registerTwo.asInt());
		var difference = (Unsigned8Bit) vx.minus(vy);
		boolean borrow = vx.asInt() < vy.asInt();

		return vm.createTransformer().setProcessor(vm.getProcessor().createTransformer()
				.setMainRegister(registerOne.asInt(), difference)
				.setFlag(!borrow)
				.advanceProgramCounter()
				.transform()).transform();
	}
}
