#pragma version(1)
#pragma rs java_package_name(com.example.renderscritpexample)

// set from the java SDK level
rs_allocation gIn;
rs_allocation gOut;

// Static variables
static uint32_t _width;
static uint32_t _height;

void setup_relief() {
	_width = rsAllocationGetDimX(gIn);
	_height = rsAllocationGetDimY(gIn);
}

static inline float FClampFloat(const float t, const float tLow, const float tHigh) {
    if (t < tHigh) {
        return ((t > tLow) ? t : tLow);
    }
    return tHigh;
}

static inline float3 FClamp01Float3(const float3 t) {
    t.r = FClampFloat(t.r, 0.0f, 1.0f);
    t.g = FClampFloat(t.g, 0.0f, 1.0f);
    t.b = FClampFloat(t.b, 0.0f, 1.0f);
    return t;
}

uchar4 RS_KERNEL relief(const uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out;
	float4 f4 = rsUnpackColor8888(in);	// extract RGBA values, see rs_core.rsh
	
    float3 f3;
    if (x == _width - 1) {
        f3 = f4.rgb;
    } else {
      float4 theF4 = rsUnpackColor8888(rsGetElementAt_uchar4(gIn, x + 1, y));
      f3 = (f4.rgb - theF4.rgb) + 0.5f;
      f3 = FClamp01Float3(f3);
    }

    out = rsPackColorTo8888(f3);
    return out;
}