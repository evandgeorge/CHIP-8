package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.AddressInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;

/**----------------------------------------------------------------**
 * Bnnn - JP V0, addr
 * Jump to location nnn + V0.
 *
 * The program counter is set to nnn plus the value of V0.
 **----------------------------------------------------------------**/

public class RelativeJumpInstruction extends AddressInstruction {

	public RelativeJumpInstruction(Unsigned16Bit address) {
		super(address);
	}

	@Override
	public String getDisassembly() {
		return "JP I, $" + address.toHexString();
	}

	@Override
	public String getDescription() {
		return "Set I = " + address.toHexString();
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		return vm.createTransformer().setProcessor(vm.getProcessor().createTransformer()
				.setIndexRegister(address)
				.advanceProgramCounter()
				.transform()).transform();
	}
}
