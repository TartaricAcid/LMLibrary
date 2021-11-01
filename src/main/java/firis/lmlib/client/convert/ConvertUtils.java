package firis.lmlib.client.convert;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.api.resource.LMTextureBox;
import firis.lmlib.api.resource.MultiModelPack;
import firis.lmlib.client.convert.pojo.*;
import firis.lmlib.common.helper.ResourceFileHelper;
import firis.lmmm.api.model.ModelMultiBase;
import firis.lmmm.api.model.parts.ModelBox;
import firis.lmmm.api.model.parts.ModelBoxBase;
import firis.lmmm.api.model.parts.ModelPlate;
import firis.lmmm.api.renderer.ModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class ConvertUtils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static int BONES_COUNT;

    public static void transModelToBedrock() {
        Collection<LMTextureBox> lmTextureBoxList = LMLibraryAPI.instance().getTextureManager().getLMTextureBoxList();
        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        for (LMTextureBox textureBox : lmTextureBoxList) {
            Path packDir = createPackDirectory(textureBox.getTextureModelName());
            saveBedrockModel(packDir.resolve(textureBox.multiModelPack.multiModelName + ".json").toFile(), textureBox.multiModelPack);
            for (ResourceLocation res : textureBox.textureLittleMaid.values()) {
                copyTextures(resourceManager, packDir, res);
            }
            for (ResourceLocation res : textureBox.textureWildLittleMaid.values()) {
                copyTextures(resourceManager, packDir, res);
            }
            for (ResourceLocation res : textureBox.textureInnerArmor.values()) {
                copyTextures(resourceManager, packDir, res);
            }
            for (ResourceLocation res : textureBox.textureOuterArmor.values()) {
                copyTextures(resourceManager, packDir, res);
            }
        }
    }

    private static void copyTextures(IResourceManager resourceManager, Path packDir, ResourceLocation res) {
        String fileName = Paths.get(res.getResourcePath()).getFileName().toString();
        try (InputStream stream = resourceManager.getResource(res).getInputStream()) {
            File image = packDir.resolve(fileName).toFile();
            FileUtils.copyToFile(stream, image);
        } catch (Exception ignore) {
        }
    }

    private static void saveBedrockModel(File file, MultiModelPack modelPack) {
        ModelMultiBase model = modelPack.modelLittleMaid;
        CustomModelPOJO pojo = new CustomModelPOJO();
        pojo.geometryModels[0] = new GeometryModelLegacy();
        pojo.geometryModels[0].description.textureHeight = model.textureHeight;
        pojo.geometryModels[0].description.textureWidth = model.textureWidth;
        pojo.geometryModels[0].bones = Lists.newArrayList();
        BONES_COUNT = 0;
        readBones(model.mainFrame, new float[]{0, 24, 0}, pojo.geometryModels[0].bones, "");
        saveFiles(file, pojo);
    }

    private static void readBones(ModelRenderer model, float[] rot, List<BonesItem> bones, String parentName) {
        if (model.cubeList.isEmpty() && (model.childModels == null || model.childModels.isEmpty())) {
            return;
        }

        BonesItem bonesItem = new BonesItem();

        bonesItem.name = "bone" + (BONES_COUNT == 0 ? "" : BONES_COUNT);
        BONES_COUNT++;
        if (StringUtils.isNotBlank(parentName)) {
            bonesItem.parent = parentName;
        }

        rot = new float[]{rot[0] + model.rotationPointX, rot[1] - model.rotationPointY, rot[2] + model.rotationPointZ};
        bonesItem.pivot = new float[]{rot[0], rot[1], rot[2]};
        bonesItem.rotation = new double[]{
                Math.toDegrees(model.rotateAngleX),
                Math.toDegrees(model.rotateAngleY),
                Math.toDegrees(model.rotateAngleZ)
        };

        if (!model.cubeList.isEmpty()) {
            bonesItem.cubes = Lists.newArrayList();
            for (ModelBoxBase modelBase : model.cubeList) {
                if (modelBase instanceof ModelBox) {
                    ModelBox box = (ModelBox) modelBase;
                    readModelBox(rot, bonesItem, box, model);
                }
                if (modelBase instanceof ModelPlate) {
                    ModelPlate plate = (ModelPlate) modelBase;
                    readModelPlate(rot, bonesItem, plate, model);
                }
            }
        }

        bones.add(bonesItem);

        if (model.childModels != null) {
            for (ModelRenderer child : model.childModels) {
                readBones(child, rot, bones, bonesItem.name);
            }
        }
    }

    private static void readModelPlate(float[] rot, BonesItem bonesItem, ModelPlate plate, ModelRenderer model) {
        CubesItem cubesItem = new CubesItem();
        if (plate.inflate != 0) {
            cubesItem.inflate = plate.inflate;
        }
        cubesItem.origin = new float[]{
                rot[0] + plate.posX1,
                rot[1] - plate.posY2,
                rot[2] + plate.posZ1
        };
        cubesItem.size = new float[]{
                plate.posX2 - plate.posX1,
                plate.posY2 - plate.posY1,
                plate.posZ2 - plate.posZ1
        };
        if (model.mirror) {
            cubesItem.uv = UV.getPlateUv(plate.width, plate.height, plate.texU, plate.texV, plate.lPlane, !plate.lotherplane);
        } else {
            cubesItem.uv = UV.getPlateUv(plate.width, plate.height, plate.texU, plate.texV, plate.lPlane, plate.lotherplane);
        }
        bonesItem.cubes.add(cubesItem);
    }

    private static void readModelBox(float[] rot, BonesItem bonesItem, ModelBox modelBox, ModelRenderer model) {
        CubesItem cubesItem = new CubesItem();
        if (modelBox.inflate != 0) {
            cubesItem.inflate = modelBox.inflate;
        }
        cubesItem.origin = new float[]{
                rot[0] + modelBox.posX1,
                rot[1] - modelBox.posY2,
                rot[2] + modelBox.posZ1
        };
        cubesItem.size = new float[]{
                modelBox.posX2 - modelBox.posX1,
                modelBox.posY2 - modelBox.posY1,
                modelBox.posZ2 - modelBox.posZ1
        };
        cubesItem.uv = UV.getBoxUv(cubesItem.size[0], cubesItem.size[1], cubesItem.size[2], modelBox.texU, modelBox.texV, model.mirror);
        bonesItem.cubes.add(cubesItem);
    }

    private static Path createPackDirectory(String packName) {
        Path dir = ResourceFileHelper.RESOURCE_DIR.resolve(packName);
        try {
            if (!Files.isDirectory(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dir;
    }

    private static void saveFiles(File file, CustomModelPOJO pojo) {
        try {
            FileUtils.write(file, GSON.toJson(pojo), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
