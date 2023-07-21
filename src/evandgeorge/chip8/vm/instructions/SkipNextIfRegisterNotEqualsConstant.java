package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.RegisterConstantInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;
import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 4xkk - SNE Vx, byte
 * Skip next instruction if Vx != kk.
 *
 * The interpreter compares register Vx to kk, and if they are not equal, increments the program counter by 2.
 **----------------------------------------------------------------**/

public class SkipNextIfRegisterNotEqualsConstant extends RegisterConstantInstruction {

	public SkipNextIfRegisterNotEqualsConstant(Unsigned8Bit register, Unsigned8Bit constant) {
		super(register, constant);
	}

	@Override
	public String getDisassembly() {
		return "SNE V" + register.toString() + ", $" + constant.toHexString();
	}

	@Override
	public String getDescription() {
		return "Skip next instruction if V" + register.toString() + " != 0x" + constant.toHexString();
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var registerValue = vm.getProcessor().getMainRegister(register.asInt());
		var nextProgramCounter = vm.getProcessor().getProgramCounter().incrementedTwice();

		if(!registerValue.equals(constant))
			nextProgramCounter = nextProgramCounter.incrementedTwice();

		var nextProcessor = vm.getProcessor().createTransformer().setProgramCounter((Unsigned16Bit) nextProgramCounter).transform();

		return vm.createTransformer().setProcessor(nextProcessor).transform();
	}
}
