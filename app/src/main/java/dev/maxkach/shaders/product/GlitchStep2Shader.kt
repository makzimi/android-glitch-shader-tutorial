package dev.maxkach.shaders.product

const val GLITCH_STEP_2_SHADER = """
uniform shader image;
uniform float2 imageSize;
uniform float time;
uniform float intensity;
uniform float realRandom;
uniform float slices;
uniform float noiseIntensity;
uniform float colorBarsEnabled;
uniform float rgbSplitIntensity;

vec4 main(vec2 fragCoord) {
    vec2 uv = fragCoord / imageSize;

    float wave = sin(uv.y * 40.0 + time * 5.0);
    float amplitude = 0.01 * intensity;
    uv.x += wave * amplitude;

    vec2 warped = uv * imageSize;
    return image.eval(warped);
}
"""
