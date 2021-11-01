package firis.lmlib.client.convert.pojo;

import com.google.gson.annotations.SerializedName;

public class Description {
    @SerializedName("texture_height")
    public int textureHeight;

    @SerializedName("texture_width")
    public int textureWidth;

    @SerializedName("visible_bounds_height")
    public float visibleBoundsHeight = 2;

    @SerializedName("visible_bounds_width")
    public float visibleBoundsWidth = 3;

    @SerializedName("visible_bounds_offset")
    public float[] visibleBoundsOffset = new float[]{0, 0, 0};
}
