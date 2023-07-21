package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.OneRegisterInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Fx15 - LD DT, Vx
 * Set delay timer = Vx.
 *
 * DT is set equal to the value of Vx.
 **----------------------------------------------------------------**/

public class SetDelayTimerToRegister extends OneRegisterInstruction {

	public SetDelayTimerToRegister(Unsigned8Bit register) {
		super(register);
	}

	@Override
	public String getDisassembly() {
		return String.format("LD DT, V%s", register.toString());
	}

	@Override
	public String getDescription() {
		return String.format("The delay timer is set to the value of V%s", register.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		return vm.createTransformer()
				.setProcessor(vm.getProcessor().createTransformer()
						.setDelayTimer(vm.getProcessor().getMainRegister(register.asInt()))
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
