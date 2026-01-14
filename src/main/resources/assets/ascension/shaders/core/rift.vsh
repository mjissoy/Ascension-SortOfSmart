#version 150

uniform sampler2D Sampler0;
uniform float GameTime;
uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

in vec3 Position;
in vec2 UV0;

out vec2 texCoord;

void main() {
    texCoord = UV0;

    // Use GameTime in vertex transformation to prevent optimization
    // This creates a subtle pulsing effect
    float timeOffset = sin(GameTime * 3.0) * 0.01;
    vec3 pos = Position + vec3(timeOffset, timeOffset, 0.0);

    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);
}