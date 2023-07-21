package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.RegisterConstantInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * 6xkk - LD Vx, byte
 * Set Vx = kk.
 *
 * The interpreter puts the value kk into register Vx.
 **----------------------------------------------------------------**/

public class SetRegisterToConstant extends RegisterConstantInstruction {

	public SetRegisterToConstant(Unsigned8Bit register, Unsigned8Bit constant) {
		super(register, constant);
	}

	@Override
	public String getDisassembly() {
		return String.format("LD V%s, $%s", register.toString(), constant.toHexString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s = $%s.", register.toString(), constant);
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		return vm.createTransformer()
				.setProcessor(vm.getProcessor().createTransformer()
						.setMainRegister(register.asInt(), constant)
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
