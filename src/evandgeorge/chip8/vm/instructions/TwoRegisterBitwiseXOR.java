package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.TwoRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 8xy3 - XOR Vx, Vy
 * Set Vx = Vx XOR Vy.
 *
 * Performs a bitwise exclusive OR on the values of Vx and Vy, then
 * stores the result in Vx. An exclusive OR compares the
 * corresponding bits from two values, and if the bits are not both
 * the same, then the corresponding bit in the result is set to 1.
 * Otherwise, it is 0.
 **----------------------------------------------------------------**/

public class TwoRegisterBitwiseXOR extends TwoRegisterInstruction {

	public TwoRegisterBitwiseXOR(Unsigned8Bit registerOne, Unsigned8Bit registerTwo) {
		super(registerOne, registerTwo);
	}

	@Override
	public String getDisassembly() {
		return String.format("XOR V%s, V%s", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s = V%s ^ V%s", registerOne.toString(), registerTwo.toString(), registerTwo.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var vx = vm.getProcessor().getMainRegister(registerOne.asInt());
		var vy = vm.getProcessor().getMainRegister(registerOne.asInt());

		return vm.createTransformer().setProcessor(vm.getProcessor().createTransformer()
				.setMainRegister(registerOne.asInt(), vx.bitwiseXOR(vy))
				.advanceProgramCounter().transform())
				.transform();
	}
}
