package com.commondrum.terrainrenderer;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.lwjgl.BufferUtils;
import java.nio.*;





public class App {

    private int width = 800;
    private int height = 600;

    private long window;
    private Mesh mesh;
    private Shader shader;

    public void windowInit() throws Exception {
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }


        window = glfwCreateWindow(width, height, "Simple Cube", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        glfwMakeContextCurrent(window);

    }

    public void glInit(){
        GL.createCapabilities();


        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);

        shader = new Shader("src/main/shaders/vertex.glsl", "src/main/shaders/fragment.glsl");

    }

    public void setMesh(){
        TerrainGenerator terrainGenerator = new TerrainGenerator();
        mesh = new Mesh(terrainGenerator.getVertices(512, 512), terrainGenerator.getIndices(512, 512));
    }

    

    public void loop() throws Exception {


        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            shader.use();
            glUseProgram(shader.getId());

            drawMesh();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void drawMesh() throws Exception{
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

    public static void main(String[] args) throws Exception {
        App renderer = new App();
        renderer.windowInit();
        renderer.glInit();
        renderer.setMesh();
        renderer.loop();
        renderer.cleanup();
    }
}
