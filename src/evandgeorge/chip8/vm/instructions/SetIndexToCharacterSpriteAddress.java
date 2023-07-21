package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.OneRegisterInstruction;
import evandgeorge.chip8.vm.state.Memory;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Fx29 - LD F, Vx
 * Set I = location of sprite for digit Vx.
 *
 * The value of I is set to the location for the hexadecimal sprite
 * corresponding to the value of Vx. See section 2.4, Display, for
 * more information on the Chip-8 hexadecimal font.
 **----------------------------------------------------------------**/

public class SetIndexToCharacterSpriteAddress extends OneRegisterInstruction {

	public SetIndexToCharacterSpriteAddress(Unsigned8Bit register) {
		super(register);
	}

	@Override
	public String getDisassembly() {
		return String.format("LD F, V%s", register.toString());
	}

	@Override
	public String getDescription() {
		return String.format("Set I to the location of the sprite for digit V%s", register.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var character = vm.getProcessor().getMainRegister(register.asInt());

		return vm.createTransformer()
				.setProcessor(vm.getProcessor().createTransformer()
						.setIndexRegister(Memory.characterSpriteLocation(character))
						.advanceProgramCounter()
						.transform())
				.transform();
	}
}
