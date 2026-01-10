
#version 150

uniform float GameTime;

in vec2 texCoord;
in vec3 worldPos;

out vec4 FragColor;

float noise(vec2 p) {
    return fract(sin(dot(p, vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    float time = GameTime * 0.02;

    float crackWidth = 0.08;
    float dist = abs(texCoord.x - 0.5);

    float wave = sin((texCoord.y * 15.0) + time * 4.0) * 0.02;
    dist += wave;

    float crackMask = smoothstep(crackWidth, crackWidth - 0.02, dist);

    float flow = sin((texCoord.y * 20.0) - time * 6.0);
    float pulse = sin (time * 3.0) * 0.2 + 0.8;

    vec3 baseColor = vec3(0.4, 0.8, 1.0);
    vec3 glowColor = vec3(0.6, 0.9, 1.0);

    vec3 color = mix(baseColor, glowColor, flow * 0.5 + 0.5);
    color *= pulse;

    float glow = smoothstep(0.25, 0.0, dist);
    color += glow * vec3(0.3, 0.6, 1.0);

    float alpha = crackMask + glow * 0.6;
    if (alpha < 0.05) discard;

    FragColor = vec4(color, alpha);
}