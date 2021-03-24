package renderer;

import algorithm.Config;
import bitstorage.BitStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class FrameRenderer extends JFrame implements Renderer{
	public static final int INTER_PIXEL = Config.PIXEL_SIZE + Config.PIXEL_DISTANCE;

	protected BitStorage storage;
	protected BufferStrategy bs;

	protected long time = 0L;
	protected long runs = 0L;
	protected final Object dataMonitor = new Object();

	public FrameRenderer(){
		super("Game of life");
		setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
		setBackground(Config.BG_COLOR);
		setVisible(true);
		createBufferStrategy(2);
		bs = getBufferStrategy();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void render(BitStorage storage){
		synchronized(this){
			this.storage = storage;
		}
		//revalidate();

		repaint();
	}

	@Override
	public long getAvgTime(){
		synchronized(dataMonitor){
			return time / runs;
		}
	}

	@Override
	public void paint(Graphics g){
		//super.paint(g);
		try{
			long start = System.nanoTime();

			do{
				Graphics g2 = bs.getDrawGraphics();
				try{
					draw(g2);
					bs.show();
				}finally{
					g2.dispose();
				}

			}while(bs.contentsLost());

			long end = System.nanoTime();
			synchronized(dataMonitor){
				time += (end - start);
				runs++;
			}

		}catch(NullPointerException ignored){
		}
	}

	void draw(Graphics g){
		int hOffset = (getWidth() - INTER_PIXEL * Config.MATRIX_WIDTH) / 2;
		int vOffset = (getHeight() - INTER_PIXEL * Config.MATRIX_HEIGHT) / 2;
		synchronized(this){
			for(int i = 0; i < Config.MATRIX_HEIGHT; i++){
				int offset = hOffset;
				for(int j = 0; j < Config.MATRIX_WIDTH; j++){
					g.setColor(storage.get(i, j)? Config.LIVE_COLOR : Config.DEAD_COLOR);
					g.fillRect(offset, vOffset, Config.PIXEL_SIZE, Config.PIXEL_SIZE);

					offset += INTER_PIXEL;
				}
				vOffset += INTER_PIXEL;
			}
		}
	}
}
