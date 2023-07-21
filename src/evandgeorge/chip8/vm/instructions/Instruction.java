package evandgeorge.chip8.vm.instructions;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.state.VirtualMachine;
import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;
import jdk.jfr.Unsigned;

public interface Instruction {

	String getDisassembly();
	String getDescription();

	VirtualMachine executeOn(VirtualMachine vm, Keyboard keyboard);

	/* Parses a two byte CHIP-8 instruction and returns its Instruction object representation */
	static Instruction parseInstruction(Unsigned16Bit instruction) {
		int opCode = instruction.getValueOfBitRange(0, 4);
		int lastDigitOpCode = instruction.getValueOfBitRange(12, 4);
		Unsigned8Bit register;
		Unsigned8Bit register1 = register = new Unsigned8Bit(instruction.getValueOfBitRange(4, 4));
		Unsigned8Bit register2 = new Unsigned8Bit(instruction.getValueOfBitRange(8, 4));
		Unsigned8Bit nibbleConstant = new Unsigned8Bit(instruction.getValueOfBitRange(12, 4));
		Unsigned8Bit oneByteConstant = new Unsigned8Bit(instruction.getValueOfBitRange(8, 8));
		Unsigned16Bit address = new Unsigned16Bit(instruction.getValueOfBitRange(4, 12));

		//special operations
		if(instruction.asInt() == 0x00E0) {
			return new ClearInstruction();
		} else if(instruction.asInt() == 0X00EE) {
			return new ReturnInstruction();
		} else if(opCode == 0) {
			return new SysInstruction(address);
		}

		switch(opCode) {
			case 0x1:
				return new JumpInstruction(address);
			case 0x2:
				return new CallInstruction(address);
			case 0x3:
				return new SkipNextIfRegisterEqualsConstant(register, oneByteConstant);
			case 0x4:
				return new SkipNextIfRegisterNotEqualsConstant(register, oneByteConstant);
			case 0x5:
				return new SkipNextIfRegisterEqualsRegister(register1, register2);
			case 0x6:
				return new SetRegisterToConstant(register, oneByteConstant);
			case 0x7:
				return new AddConstantToRegister(register, oneByteConstant);
			case 0x8:
				//8XY_ instructions
				switch(lastDigitOpCode) {
					case 0x0:
						return new SetRegisterToRegister(register1, register2);
					case 0x1:
						return new TwoRegisterBitwiseOR(register1, register2);
					case 0x2:
						return new TwoRegisterBitwiseAND(register1, register2);
					case 0x3:
						return new TwoRegisterBitwiseXOR(register1, register2);
					case 0x4:
						return new AddRegisterToRegister(register1, register2);
					case 0x5:
						return new SubtractRegisterFromRegister(register1, register2);
					case 0x6:
						return new ShiftRegisterRight(register1, register2);
					case 0x7:
						return new SubtractRegisterFromRegister(register2, register1);
					case 0xE:
						return new ShiftRegisterLeft(register1, register2);
				}
			case 0x9:
				return new SkipNextIfRegisterNotEqualsRegister(register1, register2);
			case 0xA:
				return new SetIndexRegisterToAddress(address);
			case 0xB:
				return new RelativeJumpInstruction(address);
			case 0xC:
				return new SetRegisterToRandomByteANDConstant(register, oneByteConstant);
			case 0xD:
				return new DrawSpriteInstruction(register1, register2, nibbleConstant);
			case 0xE:
				switch(oneByteConstant.asInt()) {
					case 0x9E:
						return new SkipNextIfKeyPressed(register);
					case 0xA1:
						return new SkipNextIfKeyNotPressed(register);
				}
			case 0xF:
				switch(oneByteConstant.asInt()) {
					case 0x07:
						return new SetRegisterToDelayTimer(register);
					case 0x0A:
						return new WaitForAndStoreNextKey(register);
					case 0x15:
						return new SetDelayTimerToRegister(register);
					case 0x18:
						return new SetSoundTimerToRegister(register);
					case 0x1E:
						return new AddRegisterToIndex(register);
					case 0x29:
						return new SetIndexToCharacterSpriteAddress(register);
					case 0x33:
						return new StoreBCDOfRegisterAtIndexedMemory(register);
					case 0x55:
						return new StoreRegistersInIndexedMemory(register);
					case 0x65:
						return new ReadRegistersFromIndexedMemory(register);
				}
		}

		return new MalformedInstruction();
	}
}
