package evandgeorge.chip8.debug;

import evandgeorge.chip8.vm.state.VirtualMachine;

public interface FieldWatch {

	String getName();
	Object getValue();
	boolean updateAndCheckForChange(VirtualMachine vm);

}
