#pragma version(1)
#pragma rs java_package_name(com.example.renderscritpexample)

static inline uchar ClampUchar(const uchar t, const uchar tLow, const uchar tHigh) {
    if (t < tHigh) {
        if(t > tLow){
            return t;
        }else{
        return tLow;
        }
    }
    return tHigh;
}

static inline uchar4 ClampUchar4(uchar4 t) {
    t.r = ClampUchar(t.r, 0, 255);
    t.g = ClampUchar(t.g, 0, 255);
    t.b = ClampUchar(t.b, 0, 255);
    return t;
}

uchar4 RS_KERNEL Noise(uchar4 in, uint32_t x, uint32_t y) {
	uchar4 out = in;
    out.r = in.r + rsRand(-20,20);
    out.g = in.g + rsRand(-20,20);
    out.b = in.b + rsRand(-20,20);
    out = ClampUchar4(out);

    return out;
}



