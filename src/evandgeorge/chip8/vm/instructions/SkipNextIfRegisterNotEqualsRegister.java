package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.TwoRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 9xy0 - SNE Vx, Vy
 * Skip next instruction if Vx != Vy.
 *
 * The values of Vx and Vy are compared, and if they are not equal,
 * the program counter is increased by 2.
 **----------------------------------------------------------------**/

public class SkipNextIfRegisterNotEqualsRegister extends TwoRegisterInstruction {

	public SkipNextIfRegisterNotEqualsRegister(Unsigned8Bit registerOne, Unsigned8Bit registerTwo) {
		super(registerOne, registerTwo);
	}

	@Override
	public String getDisassembly() {
		return String.format("SNE V%s , V%s", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Skip the next instruction if V%s != V%s", registerOne.toString(), registerTwo.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var vx = vm.getProcessor().getMainRegister(registerOne.asInt());
		var vy = vm.getProcessor().getMainRegister(registerTwo.asInt());
		var nextProgramCounter = (Unsigned16Bit) vm.getProcessor().getProgramCounter().incrementedTwice();

		if(!vx.equals(vy))
			nextProgramCounter = (Unsigned16Bit) nextProgramCounter.incrementedTwice();

		var nextProcessor = vm.getProcessor().createTransformer().setProgramCounter(nextProgramCounter).transform();
		return vm.createTransformer().setProcessor(nextProcessor).transform();
	}
}
