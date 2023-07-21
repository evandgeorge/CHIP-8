package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.OneRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Fx33 - LD B, Vx
 * Store BCD representation of Vx in memory locations I, I+1, and I+2.
 *
 * The interpreter takes the decimal value of Vx, and places the
 * hundreds digit in memory at location in I, the tens digit at
 * location I+1, and the ones digit at location I+2.
 **----------------------------------------------------------------**/

public class StoreBCDOfRegisterAtIndexedMemory extends OneRegisterInstruction {

	public StoreBCDOfRegisterAtIndexedMemory(Unsigned8Bit register) {
		super(register);
	}

	@Override
	public String getDisassembly() {
		return String.format("LD B, V%s", register.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Store the BCD representation of V%s in memory locations I, I+1, and I+2", register.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var value = vm.getProcessor().getMainRegister(register).asInt();

		int hundreds, tens, ones;
		hundreds = value / 100;
		tens = (value - hundreds * 100) / 10;
		ones = value - (hundreds * 100) - (tens * 10);

		return vm.createTransformer()
				.setMemory(vm.getMemory().createTransformer()
						.setBytes(vm.getProcessor().getIndexRegister(), new Unsigned8Bit[] {
								new Unsigned8Bit(hundreds),
								new Unsigned8Bit(tens),
								new Unsigned8Bit(ones)
						})
						.transform())
				.setProcessor(vm.getProcessor().createTransformer()
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
