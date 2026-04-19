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

vec2 rot(vec2 v, float a) {
    float c = cos(a), s = sin(a);
    return vec2(c*v.x - s*v.y, s*v.x + c*v.y);
}

// Rotate UV space so this crack arm points in `angle` direction,
// shift so the bright root (v=1, bottom of texture) sits at quad centre,
// then sample.  The smoothstep threshold KILLS the grey background haze
// that was flooding the whole quad teal.
float sampleCrack(sampler2D tex, vec2 uv, float angle, float scale, float offsetY) {
    vec2 c = uv - 0.5;
    c = rot(c, -angle);
    c /= scale;
    c.y -= offsetY;
    c += 0.5;
    c = clamp(c, 0.001, 0.999);
    float raw = texture(tex, c).r;
    // CRITICAL: strip grey haze — only bright crack lines survive
    return smoothstep(0.25, 0.65, raw);
}

float sampleRune(vec2 uv, vec2 centre, float scale) {
    vec2 d = (uv - centre) / scale + 0.5;
    if (any(lessThan(d, vec2(0.0))) || any(greaterThan(d, vec2(1.0)))) return 0.0;
    return smoothstep(0.20, 0.60, texture(SamplerRuneGlow, d).r);
}

void main() {
    vec2 uv = texCoord0;
    vec2 c  = uv - 0.5;
    float t = GameTime;

    // 1 - Vortex spinning disc at centre
    vec2 vUV   = rot(c, t * 0.018) + 0.5;
    float vRaw = texture(SamplerVortex, clamp(vUV, 0.001, 0.999)).r;
    float vMask   = 1.0 - smoothstep(0.08, 0.20, length(c));
    float vBright = smoothstep(0.20, 0.55, vRaw) * vMask;

    // 2 - Five crack arms, all rooted at quad centre, radiating outward
    float sway = t * 0.007;
    float S    = 1.2566;   // 2PI / 5
    float sc   = 0.55;     // how much of the quad height each arm spans
    float oy   = -0.5;     // anchor: root (v=1) at centre

    float c1 = sampleCrack(SamplerCrack1, uv,  0.0*S + sin(sway      )*0.05, sc, oy);
    float c2 = sampleCrack(SamplerCrack2, uv,  1.0*S + sin(sway*1.30 )*0.05, sc, oy);
    float c3 = sampleCrack(SamplerCrack3, uv,  2.0*S + sin(sway*0.90 )*0.05, sc, oy);
    float c4 = sampleCrack(SamplerCrack4, uv,  3.0*S + sin(sway*1.10 )*0.05, sc, oy);
    float c5 = sampleCrack(SamplerCrack5, uv,  4.0*S + sin(sway*0.75 )*0.05, sc, oy);

    float cracks = max(max(max(max(c1,c2),c3),c4),c5);

    // 3 - Rune glows at mid-reach of each arm
    float rP = t * 0.018;
    float rm = 0.21;
    float rs = 0.095;

    vec2 rp1 = vec2(0.5) + rot(vec2(0.0, rm), 0.0*S);
    vec2 rp2 = vec2(0.5) + rot(vec2(0.0, rm), 1.0*S);
    vec2 rp3 = vec2(0.5) + rot(vec2(0.0, rm), 2.0*S);
    vec2 rp4 = vec2(0.5) + rot(vec2(0.0, rm), 3.0*S);
    vec2 rp5 = vec2(0.5) + rot(vec2(0.0, rm), 4.0*S);

    float r1 = sampleRune(uv, rp1, rs) * (0.55 + 0.45*sin(rP        ));
    float r2 = sampleRune(uv, rp2, rs) * (0.55 + 0.45*sin(rP + 1.257));
    float r3 = sampleRune(uv, rp3, rs) * (0.55 + 0.45*sin(rP + 2.513));
    float r4 = sampleRune(uv, rp4, rs) * (0.55 + 0.45*sin(rP + 3.770));
    float r5 = sampleRune(uv, rp5, rs) * (0.55 + 0.45*sin(rP + 5.027));

    float runes = max(max(max(max(r1,r2),r3),r4),r5);

    // 4 - Hard glowing point at dead centre
    float core = pow(max(0.0, 1.0 - length(c) / 0.10), 2.5);

    // 5 - Combine
    float bright = core    * 1.1
    + vBright * 0.8
    + cracks  * 1.2
    + runes   * 0.6;
    bright = clamp(bright, 0.0, 1.0);

    // 6 - Green palette (cyan-white core, teal-green cracks, matches reference)
    vec3 colCore  = vec3(0.80, 1.00, 0.95);
    vec3 colCrack = vec3(0.04, 0.94, 0.60);
    vec3 colRune  = vec3(0.30, 1.00, 0.55);
    vec3 colEdge  = vec3(0.00, 0.06, 0.02);

    float coreness = clamp(core + vBright*0.5, 0.0, 1.0);
    vec3 tint = mix(colEdge, colCrack, smoothstep(0.0, 0.3, cracks));
    tint = mix(tint, colRune,  runes   * 0.70);
    tint = mix(tint, colCore,  coreness);

    // 7 - Alpha: transparent everywhere except actual crack/glow pixels
    float alpha = smoothstep(0.0, 0.08, bright) * vertexColor.a;

    fragColor = vec4(tint * bright * 2.0, alpha);
}
