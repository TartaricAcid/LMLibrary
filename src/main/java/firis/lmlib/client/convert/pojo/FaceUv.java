package firis.lmlib.client.convert.pojo;

import com.google.gson.annotations.SerializedName;

public class FaceUv {
    @SerializedName("uv")
    public double[] uv;

    @SerializedName("uv_size")
    public double[] uvSize;

    public FaceUv(double u, double v, double us, double vs) {
        this.uv = new double[]{u, v};
        this.uvSize = new double[]{us, vs};
    }
}
