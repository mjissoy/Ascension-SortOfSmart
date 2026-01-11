#version 150

uniform sampler2D Sampler0;
uniform float GameTime;

in vec2 texCoord0;
in vec4 vertexColor;
in vec3 normal;
in vec3 worldPos;
in float time;

out vec4 fragColor;

// Simplex noise function (simplified for performance)
float hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);

    vec2 u = f * f * (3.0 - 2.0 * f);

    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));

    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

// Fractal Brownian Motion
float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;
    float frequency = 1.0;

    for (int i = 0; i < 5; i++) {
        value += amplitude * noise(p * frequency);
        amplitude *= 0.5;
        frequency *= 2.0;
    }

    return value;
}

// Create crack pattern
float crackPattern(vec2 uv) {
    // Scale and rotate UV
    uv = uv * 4.0;
    uv.y += time * 0.1;

    // Create main crack lines
    float cracks = 0.0;
    cracks += fbm(uv * 2.0) * 0.8;
    cracks += fbm(uv * 4.0 + vec2(time * 0.05)) * 0.4;
    cracks += fbm(uv * 8.0 - vec2(time * 0.1)) * 0.2;

    // Threshold to create sharp cracks
    cracks = smoothstep(0.3, 0.7, cracks);

    return cracks;
}

void main() {
    // Create UV with time-based animation
    vec2 uv = texCoord0 * 2.0 - 1.0;

    // Calculate crack pattern
    float cracks = crackPattern(uv);

    // Base light blue color
    vec3 baseColor = vec3(0.5, 0.8, 1.0);

    // Inner glow color (brighter)
    vec3 innerColor = vec3(0.7, 0.9, 1.0);

    // Edge glow (very bright)
    vec3 edgeColor = vec3(0.9, 1.0, 1.0);

    // Create animated pulse
    float pulse = sin(time * 2.0) * 0.1 + 0.9;

    // Calculate final color
    vec3 finalColor;

    if (cracks > 0.8) {
        // Crack edges - brightest
        finalColor = edgeColor * pulse * 1.5;
    } else if (cracks > 0.5) {
        // Inside cracks - bright glow
        finalColor = mix(innerColor, edgeColor, (cracks - 0.5) / 0.3) * pulse;
    } else if (cracks > 0.2) {
        // Fade to base color
        finalColor = mix(baseColor, innerColor, (cracks - 0.2) / 0.3) * 0.8;
    } else {
        // Transparent outside
        finalColor = baseColor * cracks * 0.5;
    }

    // Add time-based swirling effect
    float swirl = sin(uv.x * 3.0 + uv.y * 3.0 + time) * 0.2;
    finalColor += vec3(0.1, 0.2, 0.3) * swirl;

    // Calculate alpha based on crack pattern
    float alpha = cracks * 0.8 + 0.2;

    // Make edges fade out
    float distFromCenter = length(uv);
    alpha *= 1.0 - smoothstep(0.7, 1.0, distFromCenter);

    // Add pulsing to alpha
    alpha *= 0.7 + 0.3 * pulse;

    // Output final color with transparency
    fragColor = vec4(finalColor, alpha);

    // Optional: For a more end-portal-like effect, uncomment this:
    // fragColor *= texture(Sampler0, texCoord0 + vec2(time * 0.05)) * vertexColor;
}