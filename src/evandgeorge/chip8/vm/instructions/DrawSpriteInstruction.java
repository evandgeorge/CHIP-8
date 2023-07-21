package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.types.TwoRegisterConstantInstruction;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Sprite;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

/**----------------------------------------------------------------**
 * Dxyn - DRW Vx, Vy, nibble
 * Display n-byte sprite starting at memory location I at (Vx, Vy),
 * set VF = collision.
 *
 * The interpreter reads n bytes from memory, starting at the
 * address stored in I. These bytes are then displayed as sprites
 * on screen at coordinates (Vx, Vy). Sprites are XORed onto the
 * existing screen. If this causes any pixels to be erased, VF is
 * set to 1, otherwise it is set to 0. If the sprite is positioned
 * so part of it is outside the coordinates of the display, it wraps
 * around to the opposite side of the screen. See instruction 8xy3
 * for more information on XOR, and section 2.4, Display, for more
 * information on the Chip-8 screen and sprites.
 **----------------------------------------------------------------**/

public class DrawSpriteInstruction extends TwoRegisterConstantInstruction {
	public DrawSpriteInstruction(Unsigned8Bit registerOne, Unsigned8Bit registerTwo, Unsigned8Bit constant) {
		super(registerOne, registerTwo, constant);
	}

	@Override
	public String getDisassembly() {
		return String.format("DRW V%s, V%s, %s", registerOne.toString(), registerTwo.toString(), constant.toHexString());
	}

	@Override
	public String getDescription() {
		return String.format("Display %s-byte (%s lines) sprite starting at memory location I at (V%s, V%s), set VF = collision", constant.toHexString(), constant.toHexString(), registerOne.toString(), registerTwo.toString());
	}

	@Override
	public VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard) {
		var x = vm.getProcessor().getMainRegister(registerOne.asInt());
		var y = vm.getProcessor().getMainRegister(registerTwo.asInt());
		var spriteAddress = vm.getProcessor().getIndexRegister();
		var sprite = new Sprite(vm.getMemory().getBytes(spriteAddress, constant));

		var newDisplay = vm.getDisplay().drawSprite(x, y, sprite);
		return vm.createTransformer().setDisplay(newDisplay)
				.setProcessor(vm.getProcessor().createTransformer()
						.advanceProgramCounter()
						.setFlag(newDisplay.getPixelErasedFlag())
						.transform()).transform();
	}
}
