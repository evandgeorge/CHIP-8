package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.OneRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Fx07 - LD Vx, DT
 * Set Vx = delay timer value.
 *
 * The value of DT is placed into Vx.
 **----------------------------------------------------------------**/

public class SetRegisterToDelayTimer extends OneRegisterInstruction {

	public SetRegisterToDelayTimer(Unsigned8Bit register) {
		super(register);
	}

	@Override
	public String getDisassembly() {
		return String.format("LD V%s, DT", register.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Set V%s to the delay timer's value.", register.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		return vm.createTransformer()
				.setProcessor(vm.getProcessor().createTransformer()
						.setMainRegister(register.asInt(), vm.getProcessor().getDelayTimer())
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
