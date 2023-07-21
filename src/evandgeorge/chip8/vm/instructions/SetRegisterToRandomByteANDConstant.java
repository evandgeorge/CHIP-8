package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.RegisterConstantInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Cxkk - RND Vx, byte
 * Set Vx = random byte AND kk.
 *
 * The interpreter generates a random number from 0 to 255, which
 * is then ANDed with the value kk. The results are stored in Vx.
 * See instruction 8xy2 for more information on AND.
 **----------------------------------------------------------------**/

public class SetRegisterToRandomByteANDConstant extends RegisterConstantInstruction {

	public SetRegisterToRandomByteANDConstant(Unsigned8Bit register, Unsigned8Bit constant) {
		super(register, constant);
	}

	@Override
	public String getDisassembly() {
		return String.format("RND V%s, %s", register.toString(), constant.toHexString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s = [random byte] & %s.", register.toString(), constant.toHexString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var randomByte = vm.getRandomNumberGenerator().getRandomByte();
		return vm.createTransformer().setProcessor(vm.getProcessor().createTransformer()
				.setMainRegister(register.asInt(), constant.bitwiseAND(randomByte))
				.advanceProgramCounter()
				.transform()).setRandomNumberGeneratorToNext()
				.transform();
	}
}
