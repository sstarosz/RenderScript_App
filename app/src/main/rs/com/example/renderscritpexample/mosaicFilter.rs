#pragma version(1)
#pragma rs java_package_name(com.example.renderscritpexample)

// set from the java SDK level
rs_allocation gIn;
rs_allocation gOut;

// Magic factors
static uint32_t MosiacSize = 20;

// Static variables
static uint32_t _width;
static uint32_t _height;

void setup_mosaic() {
	_width = rsAllocationGetDimX(gIn);
	_height = rsAllocationGetDimY(gIn);
}


uchar4 RS_KERNEL mosaic(uchar4 in, uint32_t x, uint32_t y) {

    uchar4 out = in;
    float4 tempOutput;

    if (y % MosiacSize == 0 && x % MosiacSize == 0) {
    		tempOutput = rsUnpackColor8888(in);
    	} else {
            uint32_t theX = MosiacSize * (x / MosiacSize);
            uint32_t theY = MosiacSize * (y / MosiacSize);
            tempOutput = rsUnpackColor8888(*(const uchar4*)rsGetElementAt(gIn, theX, theY));
        }

    out = rsPackColorTo8888(tempOutput);
    return out;
}