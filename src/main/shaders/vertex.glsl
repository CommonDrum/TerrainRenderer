#version 120

attribute vec4 a_position;
varying float v_height;


uniform mat4 u_mvpMatrix;
const mat4 u_isometric = mat4(
            0.707f, -0.405f, 0.577f, 0.0f,
            0.708f, 0.408f, -0.577f,0.0f,
            0.0f,  0.816f, 0.577f,0.0f,
            0.0f, 0.0f, 0.0f, 1.0f 
);

void main() {
    v_height = a_position.y;
    gl_Position = a_position * u_isometric;
}
