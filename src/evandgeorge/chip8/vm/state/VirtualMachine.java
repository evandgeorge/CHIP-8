package evandgeorge.chip8.vm.state;

import evandgeorge.chip8.vm.input.Keyboard;
import evandgeorge.chip8.vm.instructions.Instruction;
import evandgeorge.chip8.vm.types.Program;
import evandgeorge.chip8.vm.types.Unsigned16Bit;
import evandgeorge.chip8.vm.types.Unsigned8Bit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VirtualMachine {

	private static final int DEFAULT_SEED = 0;

	private final Memory memory;
	private final Display display;
	private final RandomNumberGenerator randomNumberGenerator;
	private final Processor processor;

	private static final int TIMER_TICK_MILLISECONDS = 17;
	private int millisecondsUntilTimerUpdate = TIMER_TICK_MILLISECONDS;

	public static VirtualMachine nullMachine = new VirtualMachine(Memory.nullMemory, Display.blankDisplay, new RandomNumberGenerator(DEFAULT_SEED), Processor.nullProcessor);

	public static VirtualMachine createFromProgram(Program program) {
		var memoryTransformer = nullMachine.memory.createTransformer();
		memoryTransformer.setBytes(new Unsigned16Bit(Memory.ROM_BEGIN_OFFSET), program.romData);

		return nullMachine.createTransformer()
				.setMemory(memoryTransformer.transform())
				.transform();
	}

	protected VirtualMachine(Memory memory, Display display, RandomNumberGenerator randomNumberGenerator, Processor processor) {
		this.memory = memory;
		this.display = display;
		this.randomNumberGenerator = randomNumberGenerator;
		this.processor = processor;
	}

	public Memory getMemory() {
		return memory;
	}

	public Display getDisplay() {
		return display;
	}

	public RandomNumberGenerator getRandomNumberGenerator() {
		return randomNumberGenerator;
	}

	public Processor getProcessor() {
		return processor;
	}

	public Transformer createTransformer() {
		return new Transformer(this);
	}

	public VirtualMachine step(Keyboard keyboard, int millisecondsElapsed) {
		millisecondsUntilTimerUpdate -= millisecondsElapsed;
		VirtualMachine executionVM = this;

		//update timers if needed
		if(millisecondsElapsed > millisecondsUntilTimerUpdate) {
			millisecondsUntilTimerUpdate += TIMER_TICK_MILLISECONDS;

			executionVM = this.createTransformer()
					.setProcessor(processor.createTransformer()
							.tickTimers()
							.transform())
					.transform();
		}

		return getNextInstruction().executeOn(executionVM, keyboard);
	}

	public Instruction getNextInstruction() {
		var pc = processor.getProgramCounter();
		var byte1 = memory.getByte(pc);
		var byte2 = memory.getByte(pc.incremented());
		return Instruction.parseInstruction(new Unsigned16Bit(byte1, byte2));
	}

	public static class Transformer {
		private Memory memory;
		private Display display;
		private RandomNumberGenerator randomNumberGenerator;
		private Processor processor;

		private Transformer(VirtualMachine original) {
			this.memory = original.memory;
			this.display = original.display;
			this.randomNumberGenerator = original.randomNumberGenerator;
			this.processor = original.processor;
		}

		public Transformer setMemory(Memory memory) {
			this.memory = memory;
			return this;
		}

		public Transformer setDisplay(Display display) {
			this.display = display;
			return this;
		}

		public Transformer setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
			this.randomNumberGenerator = randomNumberGenerator;
			return this;
		}

		public Transformer setRandomNumberGeneratorToNext() {
			this.randomNumberGenerator = randomNumberGenerator.nextState();
			return this;
		}

		public Transformer setProcessor(Processor processor) {
			this.processor = processor;
			return this;
		}

		public VirtualMachine transform() {
			return new VirtualMachine(memory, display, randomNumberGenerator, processor);
		}
	}
}
