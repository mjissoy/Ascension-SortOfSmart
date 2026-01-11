#version 150

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float GameTime;

out vec2 texCoord0;
out vec4 vertexColor;
out vec3 normal;
out vec3 worldPos;
out float time;

void main() {
    // Calculate world position for fragment shader
    worldPos = Position;

    // Pass through texture coordinates and color
    texCoord0 = UV0;
    vertexColor = Color;
    normal = Normal;
    time = GameTime;

    // Standard position transformation
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}