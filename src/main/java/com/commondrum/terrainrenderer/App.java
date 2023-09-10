package com.commondrum.terrainrenderer;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class App {

    private long window;
    private Mesh mesh;

    public void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        window = glfwCreateWindow(800, 600, "Simple Cube", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);

        float aspectRatio = (float) 800 / 600;
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glFrustum(-aspectRatio, aspectRatio, -1, 1, 1.5f, 10.0f);
        glMatrixMode(GL_MODELVIEW);

        TerrainGenerator terrainGenerator = new TerrainGenerator();
        mesh = new Mesh(terrainGenerator.getVertices(512, 512), terrainGenerator.getIndices(512, 512));
    }

    public void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glLoadIdentity();
            glTranslatef(0.0f, 0.0f, -5.0f); 
            glRotatef((float) (glfwGetTime() * 50), 0f, 1.0f, 0f);
            // zoom out 100 times
            glScalef(0.01f, 0.01f, 0.01f);

            drawMesh();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void drawMesh(){
        float[] vertices = mesh.getVertices();
        int[] indices = mesh.getIndices();

        glBegin(GL_TRIANGLES);

        for (int index : indices) {
            int vertexStartPos = index * 3; // Since each vertex has 3 components (x, y, z)
            glVertex3f(vertices[vertexStartPos], vertices[vertexStartPos + 1], vertices[vertexStartPos + 2]);
        }

        glEnd();

    }

    public void cleanup() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static void main(String[] args) {
        App renderer = new App();
        renderer.init();
        renderer.loop();
        renderer.cleanup();
    }
}
