

#version 150

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 texCoord;
out vec3 worldPos;

void main() {
    texCoord = UV0;
    worldPos = Position;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}