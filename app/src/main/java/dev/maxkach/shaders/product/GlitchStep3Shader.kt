package dev.maxkach.shaders.product

const val GLITCH_STEP_3_SHADER = """
uniform shader image;
uniform float2 imageSize;
uniform float time;
uniform float intensity;
uniform float realRandom;
uniform float slices;
uniform float noiseIntensity;
uniform float colorBarsEnabled;
uniform float rgbSplitIntensity;

float rand(float x) {
    return fract(sin(x * 1234.567 + 0.123) * 43758.5453);
}

float randDirection(int sliceIndex) {
    return (rand(float(sliceIndex) + 99.0) > 0.5) ? 1.0 : -1.0;
}

vec4 main(vec2 fragCoord) {
    vec2 uv = fragCoord / imageSize;

    int numSlices = int(slices);
    float sliceHeight = 1.0 / float(numSlices);
    int sliceIndex = int(uv.y / sliceHeight);

    float r = rand(float(sliceIndex));

    float sliceGlitch = 0.0;
    if (r > 0.7) {
        sliceGlitch = (r - 0.7) * 4.0 * intensity;
    }

    float direction = randDirection(sliceIndex);
    float offset = sliceGlitch * 0.08 * direction;
    uv.x += offset;

    uv = clamp(uv, 0.0, 1.0);
    vec2 warped = uv * imageSize;
    return image.eval(warped);
}
"""
