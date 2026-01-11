#version 150

uniform sampler2D Sampler0;
uniform float GameTime;

in vec2 texCoord;

out vec4 FragColor;

void main() {
    // Use the sampler and GameTime to prevent optimization
    vec4 texColor = texture(Sampler0, texCoord);

    // Simple animated effect using GameTime
    float pulse = sin(GameTime * 2.0) * 0.5 + 0.5;

    // Blend texture with debug color
    FragColor = mix(texColor, vec4(1.0, 0.0, 1.0, 1.0), pulse);
}