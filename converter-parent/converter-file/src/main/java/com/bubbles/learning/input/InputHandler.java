package com.bubbles.learning.input;

import java.io.IOException;
import java.util.List;

public interface InputHandler {

	public List<String> read() throws IOException;

}
