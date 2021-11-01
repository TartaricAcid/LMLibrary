package firis.lmlib.client.convert.pojo;

import com.google.gson.annotations.SerializedName;

public class CubesItem {
    @SerializedName("uv")
    public UV uv;

    @SerializedName("inflate")
    public float inflate;

    @SerializedName("size")
    public float[] size;

    @SerializedName("origin")
    public float[] origin;

    @SerializedName("rotation")
    private float[] rotation;

    @SerializedName("pivot")
    private float[] pivot;
}