package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.AddressInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;

/**----------------------------------------------------------------**
 * Annn - LD I, addr
 * Set I = nnn.
 *
 * The value of register I is set to nnn.
 **----------------------------------------------------------------**/

public class SetIndexRegisterToAddress extends AddressInstruction {

	public SetIndexRegisterToAddress(Unsigned16Bit address) {
		super(address);
	}

	@Override
	public String getDisassembly() {
		return "LD I, $" + address.toHexString();
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
