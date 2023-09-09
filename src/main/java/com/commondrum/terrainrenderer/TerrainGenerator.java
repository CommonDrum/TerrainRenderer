package com.commondrum.terrainrenderer;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class TerrainGenerator {

	

    public float[] getNoise(double frequency, int WIDTH, int HEIGHT){

        float[] noise = new float[WIDTH * HEIGHT];

        long seed = (long) (Math.random() * 1000000000);

        for (int y = 0; y < HEIGHT; y++){
            for (int x = 0; x < WIDTH; x++){
                noise[y * WIDTH + x] = OpenSimplex2S.noise3_ImproveXY(seed, x * frequency, y * frequency, 0.0);
            }
        }
        return noise;
    }

	public static void main(String[] args){
        TerrainGenerator terrainGenerator = new TerrainGenerator();
		float[] noise = terrainGenerator.getNoise(1.0 / 24.0, 512, 512);

        for (int y = 0; y < 512; y++){
            for (int x = 0; x < 512; x++){
                System.out.println(noise[y * 512 + x]);
            }
            System.out.println();
        }
	}
}