#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in vec2 UV1;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float GameTime;

out vec2 texCoord;
out vec4 vertexColor;
out vec2 lightmapCoord;
out float time;

void main() {
    texCoord = UV0;
    vertexColor = Color;
    lightmapCoord = UV1;
    time = GameTime;

    vec3 pos = Position;
    float distortion = sin(GameTime * 4.0 + pos.y * 8.0) * 0.03;
    pos.x += distortion;

    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);
}