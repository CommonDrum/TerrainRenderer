package com.commondrum.terrainrenderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;




public class Shader {

    public int id;

    public Shader(String vertexPath, String fragmentPath){
        int vertexShaderID = loadShader(vertexPath, GL_VERTEX_SHADER);
        int fragmentShaderID = loadShader(fragmentPath, GL_FRAGMENT_SHADER);
    
        id = glCreateProgram();
        glAttachShader(id, vertexShaderID);
        glAttachShader(id, fragmentShaderID);
        glLinkProgram(id);
        glValidateProgram(id);
    
        // Clean up
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);

        glUseProgram(0);

        System.out.println("done?");

    }

    private int loadShader(String file, int type) {
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

    public void use(){
        glUseProgram(id);
    }

    public int getId(){
        return id;
    }

    
}
