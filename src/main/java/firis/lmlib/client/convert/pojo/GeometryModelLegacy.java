package firis.lmlib.client.convert.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeometryModelLegacy {
    @SerializedName("bones")
    public List<BonesItem> bones;

    @SerializedName("description")
    public Description description = new Description();
}