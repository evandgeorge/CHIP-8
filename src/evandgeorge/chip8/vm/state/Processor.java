package evandgeorge.chip8.vm.state;

import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

import java.util.Arrays;

public class Processor {

	private final Unsigned8Bit[] mainRegisters;
	private final Unsigned16Bit indexRegister;
	private final Unsigned16Bit programCounter;
	private final Unsigned8Bit delayTimer;
	private final Unsigned8Bit soundTimer;
	private final Unsigned8Bit stackPointer;
	private final Unsigned16Bit[] stackValues;

	public static final Processor nullProcessor =
			new Processor(new Unsigned8Bit[] {
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,
			},
					Unsigned16Bit.zero,
					new Unsigned16Bit(Memory.ROM_BEGIN_OFFSET),
					Unsigned8Bit.zero,
					Unsigned8Bit.zero,

					new Unsigned16Bit[] {
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
							Unsigned16Bit.zero,
					},

					Unsigned8Bit.zero);

	public Processor(Unsigned8Bit[] mainRegisters, Unsigned16Bit indexRegister, Unsigned16Bit programCounter, Unsigned8Bit delayTimer, Unsigned8Bit soundTimer, Unsigned16Bit[] stackValues, Unsigned8Bit stackPointer) {
		this.mainRegisters = mainRegisters;
		this.indexRegister = indexRegister;
		this.programCounter = programCounter;
		this.delayTimer = delayTimer;
		this.soundTimer = soundTimer;
		this.stackValues = stackValues;
		this.stackPointer = stackPointer;
	}

	public Transformer createTransformer() {
		return new Transformer(this);
	}

	public Unsigned8Bit getMainRegister(int i) {
		return mainRegisters[i];
	}

	public Unsigned8Bit getMainRegister(Unsigned8Bit i) {
		return getMainRegister(i.asInt());
	}

	public Unsigned16Bit getIndexRegister() {
		return indexRegister;
	}

	public Unsigned16Bit getProgramCounter() {
		return programCounter;
	}

	public Unsigned8Bit getDelayTimer() {
		return delayTimer;
	}

	public Unsigned8Bit getSoundTimer() {
		return soundTimer;
	}

	public Unsigned8Bit getStackPointer() {
		return stackPointer;
	}

	public Unsigned16Bit getStackValue(int i) {
		return stackValues[i];
	}

	public Unsigned16Bit topOfStack() {
		return stackValues[stackPointer.asInt() - 1];
	}

	public Unsigned8Bit[] getRegisterRange(Unsigned8Bit lastRegister) {
		return Arrays.copyOf(mainRegisters, lastRegister.asInt() + 1);
	}

	public static class Transformer {
		private Unsigned8Bit[] mainRegisters;
		private Unsigned16Bit indexRegister;
		private Unsigned16Bit programCounter;
		private Unsigned8Bit delayTimer;
		private Unsigned8Bit soundTimer;
		private Unsigned16Bit[] stackValues;
		private Unsigned8Bit stackPointer;

		private Transformer(Processor original) {
			this.mainRegisters = Arrays.copyOf(original.mainRegisters, original.mainRegisters.length);
			this.indexRegister = original.indexRegister;
			this.programCounter = original.programCounter;
			this.delayTimer = original.delayTimer;
			this.soundTimer = original.soundTimer;
			this.stackValues = Arrays.copyOf(original.stackValues, original.stackValues.length);
			this.stackPointer = original.stackPointer;
		}

		public Transformer setMainRegister(int i, Unsigned8Bit newValue) {
			this.mainRegisters[i] = newValue;
			return this;
		}

		public Transformer setFlag(boolean flag) {
			this.setMainRegister(0xF, flag ? Unsigned8Bit.one : new Unsigned8Bit(0));
			return this;
		}

		public Transformer setIndexRegister(Unsigned16Bit indexRegister) {
			this.indexRegister = indexRegister;
			return this;
		}

		public Transformer setProgramCounter(Unsigned16Bit programCounter) {
			this.programCounter = programCounter;
			return this;
		}

		public Transformer advanceProgramCounter() {
			this.programCounter = (Unsigned16Bit) programCounter.incrementedTwice();
			return this;
		}

		public Transformer setDelayTimer(Unsigned8Bit delayTimer) {
			this.delayTimer = delayTimer;
			return this;
		}

		public Transformer setSoundTimer(Unsigned8Bit soundTimer) {
			this.soundTimer = soundTimer;
			return this;
		}

		public Transformer tickTimers() {
			if(delayTimer.asInt() > 0)
				delayTimer = delayTimer.minus(Unsigned8Bit.one);

			if(soundTimer.asInt() > 0)
				soundTimer = delayTimer.minus(Unsigned8Bit.one);

			return this;
		}

		public Transformer pushToStack(Unsigned16Bit value) {
			this.stackValues[this.stackPointer.asInt()] = value;
			this.stackPointer = this.stackPointer.incremented();
			return this;
		}

		public Transformer setStackValue(int i, Unsigned16Bit newValue) {
			this.stackValues[i] = stackValues[i];
			return this;
		}

		public Transformer setStackPointer(Unsigned8Bit stackPointer) {
			this.stackPointer = stackPointer;
			return this;
		}

		public Transformer popStack() {
			this.stackValues[stackPointer.asInt() - 1] = null;
			this.stackPointer = (Unsigned8Bit) this.stackPointer.decremented();
			return this;
		}

		public Processor transform() {
			return new Processor(mainRegisters, indexRegister, programCounter, delayTimer, soundTimer, stackValues, stackPointer);
		}
	}
}
