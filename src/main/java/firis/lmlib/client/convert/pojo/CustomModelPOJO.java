package firis.lmlib.client.convert.pojo;

import com.google.gson.annotations.SerializedName;

public class CustomModelPOJO {
    @SerializedName("format_version")
    public String formatVersion = "1.12.0";

    @SerializedName("minecraft:geometry")
    public GeometryModelLegacy[] geometryModels = new GeometryModelLegacy[1];
}