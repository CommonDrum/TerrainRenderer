package com.commondrum.terrainrenderer;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;




public class App {

    private long window;
    private Mesh mesh;
    private int shaderProgram;

    private int objectColorLoc;
    private int lightColorLoc;
    private int lightPosLoc;
    private int viewPosLoc;

    public void init() throws Exception {
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

    }

    public void setMesh(){
        TerrainGenerator terrainGenerator = new TerrainGenerator();
        mesh = new Mesh(terrainGenerator.getVertices(512, 512), terrainGenerator.getIndices(512, 512));
    }

    public void setShaderProgram() throws Exception{
        shaderProgram = createShaderProgram("src/main/shaders/vertex.glsl", "src/main/shaders/fragment.glsl");

        

    }

    public void loop() throws Exception {

     
       

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            glUseProgram(shaderProgram);
            glLoadIdentity();
            // make isometric view
            glTranslatef(0.0f, 0.0f, -5.0f);
            glRotatef(45.0f, 1.0f, 0.0f, 0.0f);
            glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
            // zoom out 10x
            glScalef(0.1f, 0.1f, 0.1f);
            // rotate around y axis
            glRotatef((float) glfwGetTime() * 50.0f, 0.0f, 1.0f, 0.0f);

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

    public int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Could not read shader file.");
            e.printStackTrace();
            System.exit(-1);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }
        return shaderID;
    }
    
    public int createShaderProgram(String vertexFile, String fragmentFile) {
        int vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
        int fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
    
        int programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);
        glValidateProgram(programID);
    
        // Clean up
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);

        // Restore context
        glUseProgram(0);
    
        return programID;
    }
    

    public static void main(String[] args) throws Exception {
        App renderer = new App();
        renderer.init();
        renderer.setMesh();
        renderer.setShaderProgram();
        renderer.loop();
        renderer.cleanup();
    }
}
