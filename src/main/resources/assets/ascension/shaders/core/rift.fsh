#version 150

uniform sampler2D SamplerVortex;
uniform sampler2D SamplerCrack1;
uniform sampler2D SamplerCrack2;
uniform sampler2D SamplerCrack3;
uniform sampler2D SamplerCrack4;
uniform sampler2D SamplerCrack5;
uniform sampler2D SamplerRuneGlow;

uniform float GameTime;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

// ── helpers ──────────────────────────────────────────────────────────────────

vec2 rotate(vec2 uv, float angle) {
    float c = cos(angle), s = sin(angle);
    return vec2(c * uv.x - s * uv.y, s * uv.x + c * uv.y);
}

// Sample a crack texture centred at `origin`, rotated by `angle`,
// scaled by `scale`, and stretched tall (crack textures are portrait).
float sampleCrack(sampler2D tex, vec2 uv, vec2 origin, float angle, float scale) {
    vec2 d = uv - origin;
    d = rotate(d, angle);
    d /= vec2(scale * 0.28, scale);   // narrow width, tall height
    d += 0.5;
    if (d.x < 0.0 || d.x > 1.0 || d.y < 0.0 || d.y > 1.0) return 0.0;
    return texture(tex, d).r;
}

// Sample the rune glow texture as a small radial splash centred at `origin`.
float sampleRune(vec2 uv, vec2 origin, float angle, float scale) {
    vec2 d = uv - origin;
    d = rotate(d, angle);
    d /= scale * 0.4;
    d += 0.5;
    if (d.x < 0.0 || d.x > 1.0 || d.y < 0.0 || d.y > 1.0) return 0.0;
    return texture(SamplerRuneGlow, d).r;
}

// ── main ─────────────────────────────────────────────────────────────────────

void main() {
    vec2 uv = texCoord0;                       // [0,1]²  –  centre is (0.5, 0.5)

    float t = GameTime * 20.0;                 // slow tick (GameTime is in ticks)

    // ── 1. Vortex (centre) ───────────────────────────────────────────────────
    // Spin the UV around the centre for the vortex texture.
    vec2 centred = uv - 0.5;
    float vortexSpin = t * 0.018;
    vec2 vortexUV = rotate(centred, vortexSpin) + 0.5;
    vec4 vortexSample = texture(SamplerVortex, vortexUV);
    // Fade to edges so it sits as a central disc.
    float vortexMask = 1.0 - smoothstep(0.18, 0.42, length(centred));
    float vortexBright = vortexSample.r * vortexMask;

    // ── 2. Five crack blooms ─────────────────────────────────────────────────
    // Positions (x,y) spread around centre; angles so they radiate outward.
    // Slow individual sways add life.
    float sway = t * 0.012;

    float c1 = sampleCrack(SamplerCrack1, uv, vec2(0.50, 0.20), 0.0   + sin(sway       ) * 0.08, 0.55);
    float c2 = sampleCrack(SamplerCrack2, uv, vec2(0.75, 0.38), 1.257 + sin(sway * 1.3 ) * 0.08, 0.50);
    float c3 = sampleCrack(SamplerCrack3, uv, vec2(0.65, 0.78), 2.513 + sin(sway * 0.9 ) * 0.08, 0.52);
    float c4 = sampleCrack(SamplerCrack4, uv, vec2(0.30, 0.78), 3.770 + sin(sway * 1.1 ) * 0.08, 0.50);
    float c5 = sampleCrack(SamplerCrack5, uv, vec2(0.22, 0.38), 5.027 + sin(sway * 0.7 ) * 0.08, 0.52);

    float cracks = max(max(max(max(c1, c2), c3), c4), c5);

    // ── 3. Rune glows (at mid-point of each crack) ──────────────────────────
    // Pulse each rune at a slightly different rate.
    float rPulse = t * 0.025;
    float r1 = sampleRune(uv, vec2(0.50, 0.30), 0.0,   0.18) * (0.6 + 0.4 * sin(rPulse      ));
    float r2 = sampleRune(uv, vec2(0.68, 0.43), 1.257, 0.18) * (0.6 + 0.4 * sin(rPulse+1.26));
    float r3 = sampleRune(uv, vec2(0.62, 0.68), 2.513, 0.18) * (0.6 + 0.4 * sin(rPulse+2.51));
    float r4 = sampleRune(uv, vec2(0.35, 0.68), 3.770, 0.18) * (0.6 + 0.4 * sin(rPulse+3.77));
    float r5 = sampleRune(uv, vec2(0.28, 0.43), 5.027, 0.18) * (0.6 + 0.4 * sin(rPulse+5.03));

    float runes = max(max(max(max(r1, r2), r3), r4), r5);

    // ── 4. Combine into a single brightness ─────────────────────────────────
    float bright = vortexBright * 1.2 + cracks * 0.9 + runes * 0.8;
    bright = clamp(bright, 0.0, 1.0);

    // ── 5. Green palette tint ────────────────────────────────────────────────
    // Inner core: bright cyan-white; outer cracks: deep emerald; edge fade: near-black.
    vec3 coreColor  = vec3(0.55, 1.00, 0.65);   // bright mint/cyan-green
    vec3 crackColor = vec3(0.10, 0.90, 0.35);   // vivid emerald
    vec3 runeColor  = vec3(0.20, 1.00, 0.50);   // lime rune glow
    vec3 edgeColor  = vec3(0.00, 0.15, 0.05);   // deep dark green

    // Blend between crack/edge and core based on proximity to vortex.
    float coreness = vortexMask * vortexSample.r;
    vec3 tint = mix(mix(edgeColor, crackColor, cracks), mix(crackColor, coreColor, coreness), coreness);
    tint = mix(tint, runeColor, runes * 0.6);

    // ── 6. Alpha: transparent background, visible only where bright ──────────
    float alpha = smoothstep(0.02, 0.18, bright) * vertexColor.a;

    fragColor = vec4(tint * bright * 1.6, alpha);
}
