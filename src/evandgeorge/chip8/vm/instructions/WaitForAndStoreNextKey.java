package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.OneRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Fx0A - LD Vx, K
 * Wait for a key press, store the value of the key in Vx.
 *
 * All execution stops until a key is pressed, then the value of
 * that key is stored in Vx.
 **----------------------------------------------------------------**/

public class WaitForAndStoreNextKey extends OneRegisterInstruction {

	public WaitForAndStoreNextKey(Unsigned8Bit register) {
		super(register);
	}

	@Override
	public String getDisassembly() {
		return String.format("LD V%s, K", register.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Wait for a key press, store the value of the key in V%s.", register.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var nextKey = keyboard.waitForNextKey();

		return vm.createTransformer()
				.setProcessor(vm.getProcessor().createTransformer()
						.setMainRegister(register.asInt(), nextKey)
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
