#version 120

varying float v_height;

void main() {
    if (v_height < 0.0) {
        gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0); // Blue for below 0
    } else if (v_height >= 0.0 && v_height < 0.5) {
        gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0); // Green for between 0 and 0.5
    } else {
        gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0); // Red for above 0.5
    }
}
