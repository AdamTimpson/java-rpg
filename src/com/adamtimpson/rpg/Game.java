package com.adamtimpson.rpg;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.adamtimpson.rpg.graphics.Screen;
import com.adamtimpson.rpg.input.Keyboard;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	private final int SCALE = 1;
    private final int HEIGHT = 480;
    private final int WIDTH = HEIGHT * SCALE;

    private JFrame frame; 
    private Thread thread;
    private Keyboard key; 
    
    private boolean running = false;
    
    private Screen screen;
    
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

    public Game() {
        Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        setPreferredSize(size);
        
        screen = new Screen(WIDTH, HEIGHT);
        
        frame = new JFrame();
        key = new Keyboard();
        
        addKeyListener(key);
    }

    public static void main(String[] args) {
    	Game game = new Game();
    	game.frame.setResizable(false);
    	game.frame.setTitle("RPG");
    	game.frame.add(game);
    	game.frame.pack();
    	game.frame.setLocationRelativeTo(null);
    	game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	game.frame.setVisible(true);
    	
    	game.start();
    }

    public synchronized void start() {
        thread = new Thread(this, "Display");
        thread.start();

        running = true;
    }

    public void stop() {
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    int x = 0;
    int y = 0;
    public void update() {
    	x++;
//    	y++;
    	
    	key.update();
    }
    
    public void render() {
    	BufferStrategy bs = getBufferStrategy();
    	if(bs == null) {
    		createBufferStrategy(3);
    		return;
    	}
    	
    	screen.clear();
    	
    	screen.render(x, y);
    	
    	for(int i = 0; i < pixels.length; i++) {
    		pixels[i] = screen.pixels[i];
    	}
    	
    	Graphics g = bs.getDrawGraphics();
    	g.fillRect(0, 0, WIDTH, HEIGHT);
    	g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
    	g.dispose();
    	bs.show();
    }
    
    public void run() {
    	long lastTime = System.nanoTime();
    	long timer = System.currentTimeMillis();
    	double ns = 1000000000.0 / 60.0;
    	double delta = 0;
    	int frames = 0;
    	int updates = 0;
    	
        while(running) {
        	long now = System.nanoTime();
        	delta += (now - lastTime) / ns;
        	lastTime = now;
        	while(delta >= 1) {
        		update();
        		updates++;
        		delta--;
        	}
        	
        	render();
        	frames++;
        	
        	if(System.currentTimeMillis() - timer > 1000) {
        		timer += 1000;
        		String fpsString = "RPG  |  " + updates + "ups" + ", " + frames + "fps";
        		System.out.println(fpsString);
        		frame.setTitle(fpsString);
        		
        		updates = 0;
        		frames = 0;
        	}
        }
        
        stop();
    }
}
