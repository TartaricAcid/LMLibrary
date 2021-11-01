package firis.lmlib.client.convert.pojo;

import com.google.gson.annotations.SerializedName;

public class UV {
    public static final int planeXY = 0;
    public static final int planeZY = 1;
    public static final int planeXZ = 2;
    @SerializedName("down")
    public FaceUv down;
    @SerializedName("east")
    public FaceUv east;
    @SerializedName("north")
    public FaceUv north;
    @SerializedName("south")
    public FaceUv south;
    @SerializedName("up")
    public FaceUv up;
    @SerializedName("west")
    public FaceUv west;

    public static UV getBoxUv(float x, float y, float z, int ox, int oy, boolean mirror) {
        UV uv = new UV();
        if (mirror) {
            uv.down = new FaceUv(ox + z + x + x, oy + z, -x, -z);
            uv.east = new FaceUv(ox + z + x + z, oy + z, -z, y);
            uv.north = new FaceUv(ox + z + x, oy + z, -x, y);
            uv.south = new FaceUv(ox + z + x + z + x, oy + z, -x, y);
            uv.up = new FaceUv(ox + z + x, oy, -x, z);
            uv.west = new FaceUv(ox + z, oy + z, -z, y);
        } else {
            uv.down = new FaceUv(ox + z + x, oy + z, x, -z);
            uv.east = new FaceUv(ox, oy + z, z, y);
            uv.north = new FaceUv(ox + z, oy + z, x, y);
            uv.south = new FaceUv(ox + z + x + z, oy + z, x, y);
            uv.up = new FaceUv(ox + z, oy, x, z);
            uv.west = new FaceUv(ox + z + x, oy + z, z, y);
        }
        return uv;
    }

    public static UV getPlateUv(int w, int h, int ox, int oy, int lPlane, boolean lotherplane) {
        UV uv = new UV();
        switch (lPlane) {
            case planeXY:
                if (lotherplane) {
                    uv.north = new FaceUv(0, 0, 0, 0);
                    uv.south = new FaceUv(ox, oy, w, h);
                } else {
                    uv.north = new FaceUv(ox, oy, w, h);
                    uv.south = new FaceUv(0, 0, 0, 0);
                }
                uv.down = new FaceUv(0, 0, 0, 0);
                uv.east = new FaceUv(0, 0, 0, 0);
                uv.up = new FaceUv(0, 0, 0, 0);
                uv.west = new FaceUv(0, 0, 0, 0);
                return uv;
            case planeZY:
                if (lotherplane) {
                    uv.east = new FaceUv(ox, oy, w, h);
                    uv.west = new FaceUv(0, 0, 0, 0);
                } else {
                    uv.east = new FaceUv(0, 0, 0, 0);
                    uv.west = new FaceUv(ox, oy, w, h);
                }
                uv.down = new FaceUv(0, 0, 0, 0);
                uv.north = new FaceUv(0, 0, 0, 0);
                uv.south = new FaceUv(0, 0, 0, 0);
                uv.up = new FaceUv(0, 0, 0, 0);
                return uv;
            case planeXZ:
            default:
                if (lotherplane) {
                    uv.up = new FaceUv(0, 0, 0, 0);
                    uv.down = new FaceUv(ox, oy, w, h);
                } else {
                    uv.up = new FaceUv(ox, oy, w, h);
                    uv.down = new FaceUv(0, 0, 0, 0);
                }
                uv.north = new FaceUv(0, 0, 0, 0);
                uv.south = new FaceUv(0, 0, 0, 0);
                uv.east = new FaceUv(0, 0, 0, 0);
                uv.west = new FaceUv(0, 0, 0, 0);
                return uv;
        }
    }
}
