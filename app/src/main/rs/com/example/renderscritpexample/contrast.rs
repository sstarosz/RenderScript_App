#pragma version(1)
#pragma rs java_package_name(com.example.renderscritpexample)

float contrastFactor = 0.0f;

uchar4 RS_KERNEL contrast(uchar4 in, uint32_t x, uint32_t y) {
  uchar4 out = in;
  float factor = (259*(255+contrastFactor))/(255*(259 - contrastFactor));

  float red = factor  * (in.r - 128) + 128;
  float green = factor  * (in.b - 128) + 128;
  float blue = factor  * (in.g - 128) + 128;

  out.r = clamp((int)red,0,255);
  out.b = clamp((int)green,0,255);
  out.g = clamp((int)blue,0,255);

  return out;
}