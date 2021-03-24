package renderer;

import bitstorage.BitStorage;

public interface Renderer{
	void render(BitStorage storage);

	long getAvgTime();
}
