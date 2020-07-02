#pragma version(1)
#pragma rs java_package_name(com.example.renderscritpexample)

static float bright = 0.0f;

void setBright(float v){
    rsDebug("Test",v);
    bright = 255.0f / (255.0f - v);
}

void exposure(const uchar4 *in, uchar4 *out){
    out->r = clamp((int)(bright * in->r),0,255);
    out->g = clamp((int)(bright * in->g),0,255);
    out->b = clamp((int)(bright * in->b),0,255);
}