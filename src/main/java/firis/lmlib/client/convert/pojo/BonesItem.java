package firis.lmlib.client.convert.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class BonesItem {
    @SerializedName("cubes")
    public List<CubesItem> cubes;

    @SerializedName("name")
    public String name;

    @SerializedName("pivot")
    public float[] pivot;

    @SerializedName("rotation")
    public double[] rotation;

    @SerializedName("parent")
    public String parent;
}