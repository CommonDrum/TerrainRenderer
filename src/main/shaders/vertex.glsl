#version 120

attribute vec4 a_position;
varying float v_height;

uniform mat4 u_mvpMatrix;

void main() {
    v_height = a_position.y;
    gl_Position = a_position;
}
