package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.RegisterConstantInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 3xkk - SE Vx, byte
 * Skip next instruction if Vx = kk.
 *
 * The interpreter compares register Vx to kk, and if they are equal, increments the program counter by 2.
 **----------------------------------------------------------------**/

public class SkipNextIfRegisterEqualsConstant extends RegisterConstantInstruction {

	public SkipNextIfRegisterEqualsConstant(Unsigned8Bit register, Unsigned8Bit constant) {
		super(register, constant);
	}

	@Override
	public String getDisassembly() {
		return "SE V" + register.toString() + ", $" + constant.toHexString();
	}

	@Override
	public String getDescription() {
		return "Skip next instruction if V" + register.toString() + " = 0x" + constant.toHexString();
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var registerValue = vm.getProcessor().getMainRegister(register.asInt());
		var nextProgramCounter = vm.getProcessor().getProgramCounter().incrementedTwice();

		if(registerValue.equals(constant))
			nextProgramCounter = nextProgramCounter.incrementedTwice();

		var nextProcessor = vm.getProcessor().createTransformer().setProgramCounter(nextProgramCounter).transform();

		return vm.createTransformer().setProcessor(nextProcessor).transform();
	}
}
