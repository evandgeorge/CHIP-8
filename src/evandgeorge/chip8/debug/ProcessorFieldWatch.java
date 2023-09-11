package evandgeorge.chip8.debug;

import evandgeorge.chip8.vm.state.Processor;
import evandgeorge.chip8.vm.state.VirtualMachine;

public class ProcessorFieldWatch implements FieldWatch {
	private ProcessorFieldExtractor fieldExtractor;
	private Object value;
	private String name;

	public ProcessorFieldWatch(String name, Processor processor, ProcessorFieldExtractor fieldExtractor) {
		this.name = name;
		this.fieldExtractor = fieldExtractor;
		this.value = fieldExtractor.extractFieldFromProcessor(processor);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean updateAndCheckForChange(VirtualMachine vm) {
		boolean change = !value.equals(fieldExtractor.extractFieldFromProcessor(vm.getProcessor()));
		this.value = fieldExtractor.extractFieldFromProcessor(vm.getProcessor());

		return change;
	}

	public interface ProcessorFieldExtractor {
		Object extractFieldFromProcessor(Processor processor);
	}
}
