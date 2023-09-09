package com.commondrum.terrainrenderer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MeshLoader {
    private ArrayList<float[]> vertices = new ArrayList<float[]>();
    private ArrayList<int[]> faces = new ArrayList<int[]>();

    public void load(String filename) {
        BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader("src/main/resources/" + filename));
			String line = reader.readLine();

			while (line != null) {
                if (line.startsWith("v ")) {
                    vertices.add(parseVertex(line));
                } else if (line.startsWith("f ")) {
                    faces.add(parseFace(line));
                }
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private float[] parseVertex(String line) {
        String[] parts = line.split(" ");
        return new float[]{Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])};
    }

    private int[] parseFace(String line) {
        String[] parts = line.split(" ");
        // get vertex index
        String[] vertex1 = parts[1].split("/");
        String[] vertex2 = parts[2].split("/");
        String[] vertex3 = parts[3].split("/");
        return new int[]{Integer.parseInt(vertex1[0]), Integer.parseInt(vertex2[0]), Integer.parseInt(vertex3[0])};
    }

    public ArrayList<float[]> getVertices() {
        return vertices;
    }

    public ArrayList<int[]> getFaces() {
        return faces;
    }

    public void print(){
        for (float[] vertex : vertices) {
            System.out.println("v " + vertex[0] + " " + vertex[1] + " " + vertex[2]);
        }
        for (int[] face : faces) {
            System.out.println("f " + face[0] + " " + face[1] + " " + face[2]);
        }
    }

    public Mesh getMesh(){
        float[] vertices = new float[this.vertices.size() * 3];
        int[] indices = new int[faces.size() * 3];
        int i = 0;
        for (float[] vertex : this.vertices) {
            vertices[i++] = vertex[0];
            vertices[i++] = vertex[1];
            vertices[i++] = vertex[2];
        }
        i = 0;
        for (int[] face : faces) {
            indices[i++] = face[0] - 1;
            indices[i++] = face[1] - 1;
            indices[i++] = face[2] - 1;
        }
        return new Mesh(vertices, indices);
    }

    public static void main(String[] args) {
        MeshLoader loader = new MeshLoader();
        loader.load("cube.obj");
        
    }
    
}
