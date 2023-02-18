package com.adamtimpson.rpg.graphics;

public class Screen {

	private int width, height; 
	public int[] pixels;
    
    private int xtime;
    private int ytime;
    private int counter;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height; 
		
		pixels = new int[width * height];
		
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void render() {
		counter++;
		
		if(counter % 100 == 0) xtime++;
		if(counter % 100 == 0) ytime++;
		
		for(int y = 0; y < height; y++) {
			if(ytime < 0 || ytime >= height) break;
			for(int x = 0; x < width; x++) {
				if(xtime < 0 || xtime >= width) break;
				pixels[ytime + xtime * width] = 0xff00ff; 
			}
		}
	}
	
}
