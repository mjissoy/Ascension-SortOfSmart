#version 150

uniform float GameTime;

in vec2 texCoord;
in vec4 vertexColor;
in vec2 lightmapCoord;
in float time;

out vec4 FragColor;

void main() {
    vec2 center = vec2(0.5, 0.5);
    vec2 delta = texCoord - center;
    float dist = length(delta);

    float angle = atan(delta.y, delta.x);
    float swirl = angle + time * 2.0 + dist * 10.0;

    float rings = sin(dist * 15.0 - time * 3.0) * 0.5 + 0.5;

    vec3 color1 = vec3(0.0, 0.9, 1.0);
    vec3 color2 = vec3(0.0, 0.5, 0.9);

    vec3 finalColor = mix(color1, color2, rings);

    float glow = 1.0 - smoothstep(0.0, 0.5, dist);
    finalColor += vec3(0.0, 0.4, 0.6) * glow;

    float crack = sin(texCoord.x * 20.0 + time * 5.0) * sin(texCoord.y * 30.0 - time * 3.0);
    crack = smoothstep(0.7, 1.0, crack);
    finalColor = mix(finalColor, vec3(1.0), crack * 0.5);

    float alpha = mix(0.8, 1.0, glow);
    FragColor = vec4(finalColor, alpha);
}