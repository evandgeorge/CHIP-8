package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.TwoRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 5xy0 - SE Vx, Vy
 * Skip next instruction if Vx = Vy.
 *
 * The interpreter compares register Vx to register Vy, and if they are equal, increments the program counter by 2.
 **----------------------------------------------------------------**/

public class SkipNextIfRegisterEqualsRegister extends TwoRegisterInstruction {

	public SkipNextIfRegisterEqualsRegister(Unsigned8Bit registerOne, Unsigned8Bit registerTwo) {
		super(registerOne, registerTwo);
	}

	@Override
	public String getDisassembly() {
		return "SE V" + registerOne.toString() + ", $" + registerTwo.toString();
	}

	@Override
	public String getDescription() {
		return "Skip next instruction if V" + registerOne.toString() + " = V" + registerTwo.toString();
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var vx = vm.getProcessor().getMainRegister(registerOne.asInt());
		var vy = vm.getProcessor().getMainRegister(registerTwo.asInt());
		var nextProgramCounter = vm.getProcessor().getProgramCounter().incrementedTwice();

		if(vx.equals(vy))
			nextProgramCounter = nextProgramCounter.incrementedTwice();

		var nextProcessor = vm.getProcessor().createTransformer().setProgramCounter((Unsigned16Bit) nextProgramCounter).transform();
		return vm.createTransformer().setProcessor(nextProcessor).transform();
	}
}
