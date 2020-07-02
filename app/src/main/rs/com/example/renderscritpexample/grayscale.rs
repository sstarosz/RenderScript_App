#pragma version(1)
#pragma rs java_package_name(com.example.renderscritpexample)

static const float4 weight = {0.299f, 0.587f, 0.114f, 0.0f};



uchar4 RS_KERNEL greyscale(uchar4 in) {
  const float4 inF = rsUnpackColor8888(in);
  const float val = dot(inF, weight);
  const float4 outF = (float4){val,val,val,val};
  return rsPackColorTo8888(outF);
}

void process(rs_allocation inputImage, rs_allocation outputImage) {
  const uint32_t imageWidth = rsAllocationGetDimX(inputImage);
  const uint32_t imageHeight = rsAllocationGetDimY(inputImage);
  rsForEach(greyscale, inputImage, outputImage);
}