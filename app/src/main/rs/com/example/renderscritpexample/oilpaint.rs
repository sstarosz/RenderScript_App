#pragma version(1)
#pragma rs java_package_name(com.example.renderscritpexample)

// set from the java SDK level
rs_allocation gIn;
rs_allocation gOut;

// Magic factors
static uint32_t Model = 15;

// Static variables
static uint32_t _width;
static uint32_t _height;

void setup_oilpaint() {
	_width = rsAllocationGetDimX(gIn);
	_height = rsAllocationGetDimY(gIn);
}

uchar4 RS_KERNEL oilpaint(const uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out = in;
	float4 f4 = rsUnpackColor8888(in);	// extract RGBA values, see rs_core.rsh

	int32_t pos = rsRand(1, 10000) % Model;
    int32_t theX = (x + pos) < _width ? (x + pos) : ((int)x - pos) >= 0 ? (x - pos) : x;
    int32_t theY = (y + pos) < _height ? (y + pos) : ((int)y - pos) >= 0 ? (y - pos) : y;

    float4 theF4 = rsUnpackColor8888(*(const uchar4*)rsGetElementAt(gIn, theX, theY));

    out = rsPackColorTo8888(theF4.rgb);
    return out;
}