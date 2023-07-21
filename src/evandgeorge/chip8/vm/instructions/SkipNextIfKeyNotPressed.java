package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.OneRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * ExA1 - SKNP Vx
 * Skip next instruction if key with the value of Vx is not pressed.
 *
 * Checks the keyboard, and if the key corresponding to the value
 * of Vx is currently in the up position, PC is increased by 2.
 **----------------------------------------------------------------**/

public class SkipNextIfKeyNotPressed extends OneRegisterInstruction {

	public SkipNextIfKeyNotPressed(Unsigned8Bit register) {
		super(register);
	}

	@Override
	public String getDisassembly() {
		return String.format("SKNP V%s", register.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Skip the next instruction if the key with the value of V%s is not pressed.", register.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var keyCode = vm.getProcessor().getMainRegister(register.asInt());
		var nextProgramCounter = vm.getProcessor().getProgramCounter().incrementedTwice();

		if(!keyboard.isKeyPressed(keyCode))
			nextProgramCounter = nextProgramCounter.incrementedTwice();

		return vm.createTransformer()
				.setProcessor(vm.getProcessor().createTransformer()
						.setProgramCounter((Unsigned16Bit) nextProgramCounter)
						.transform())
				.transform();
	}
}
