package evandgeorge.chip8.debug;

import evandgeorge.chip8.vm.state.Memory;
import evandgeorge.chip8.vm.state.VirtualMachine;

public class MemoryFieldWatch implements FieldWatch {

	private MemoryFieldExtractor fieldExtractor;
	private Object value;
	private String name;

	public MemoryFieldWatch(String name, Memory memory, MemoryFieldExtractor fieldExtractor) {
		this.name = name;
		this.fieldExtractor = fieldExtractor;
		this.value = fieldExtractor.extractFieldFromMemory(memory);
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
		boolean change = !value.equals(fieldExtractor.extractFieldFromMemory(vm.getMemory()));
		this.value = fieldExtractor.extractFieldFromMemory(vm.getMemory());

		return change;
	}

	public interface MemoryFieldExtractor {
		Object extractFieldFromMemory(Memory memory);
	}
}
