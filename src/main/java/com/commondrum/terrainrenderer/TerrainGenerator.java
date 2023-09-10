package com.commondrum.terrainrenderer;


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

    public float[]  getVertices(int width, int height){

        float scale = 10.0f;

        float[] heightMap = getNoise(1.0 / 24.0, width, height);
        float[] vertices = new float[width * height * 3];

        //make height/2 width/2 the center of the map
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        //create vertices
        for (int z = 0; z < height; z++){
            for (int x = 0; x < width; x++){
                float y = (float) heightMap[z * width + x];
                vertices[(z * width + x) * 3] = (x - halfWidth) / scale;
                vertices[(z * width + x) * 3 + 1] = y;
                vertices[(z * width + x) * 3 + 2] = (z - halfHeight) / scale;
            }
        }


        return vertices;
    }

    public int[] getIndices(int width, int height){

        int indices[] = new int[(width - 1) * (height - 1) * 6];
        int pointer = 0;

        for (int z = 0; z < height - 1; z++){
            for (int x = 0; x < width - 1; x++){
                int topLeft = (z * width) + x;
                int topRight = topLeft + 1;
                int bottomLeft = ((z + 1) * width) + x;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return indices;
    }

	public static void main(String[] args){
        
	}
}