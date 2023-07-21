package evandgeorge.chip8.vm.input;

import evandgeorge.chip8.vm.types.Unsigned8Bit;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Keyboard {

	private static final int KEYS = 16;
	private boolean[] keyStates = new boolean[KEYS];

	ReentrantLock keyStateLock = new ReentrantLock();
	Condition keyPress = keyStateLock.newCondition();
	private Unsigned8Bit lastKeyPressed;

	private Listener listener = null;

	public boolean isKeyPressed(Unsigned8Bit keyCode) {
		keyStateLock.lock();
		var keyState = keyStates[keyCode.asInt()];
		keyStateLock.unlock();

		return keyState;
	}

	public Unsigned8Bit waitForNextKey() {
		var key = new Unsigned8Bit(0xFF);

		try {
			keyStateLock.lock();
			listener.onAwaitKey();
			keyPress.await();
			key = lastKeyPressed;
			keyStateLock.unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return key;
	}

	public void setKeyState(int virtualKey, boolean pressed) {
		keyStateLock.lock();
		keyStates[virtualKey] = pressed;

		if(pressed) {
			lastKeyPressed = new Unsigned8Bit(virtualKey);
			keyPress.signal();
		}

		keyStateLock.unlock();

		if(listener != null) {
			listener.onKeyEvent(virtualKey, pressed);
		}
	}

	public void setKeyListener(Listener listener) {
		this.listener = listener;
	}

	public interface Listener {
		void onKeyEvent(int virtualKey, boolean pressed);
		void onAwaitKey();
	}
}
