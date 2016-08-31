package silvaren.easyrs.tools;

import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import silvaren.easyrs.tools.base.ConvertingTool;
import silvaren.easyrs.tools.base.RSToolboxContext;

public class Blur {

    static class BlurParams {
        public final float radius;
        public BlurParams(float radius) {
            this.radius = radius;
        }
    }

    private static ConvertingTool.BaseToolScript blurToolScript = new ConvertingTool.BaseToolScript<BlurParams>() {
        @Override
        public void runScript(RSToolboxContext rsToolboxContext, Allocation aout, BlurParams scriptParams) {
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(
                    rsToolboxContext.rs, rsToolboxContext.ain.getElement());
            blurScript.setInput(rsToolboxContext.ain);
            blurScript.setRadius(scriptParams.radius);
            blurScript.forEach(aout);
        }
    };

    public static Bitmap blur(RenderScript rs, Bitmap inputBitmap, float radius) {
        ConvertingTool<BlurParams> blurTool = new ConvertingTool<>(blurToolScript);
        return blurTool.doComputation(rs, inputBitmap, new BlurParams(radius));
    }

    public static byte[] blur(RenderScript rs, byte[] nv21ByteArray, int width, int height,
                              float radius) {
        ConvertingTool<BlurParams> blurTool = new ConvertingTool<>(blurToolScript);
        return blurTool.doComputation(rs, nv21ByteArray, width, height, new BlurParams(radius));
    }
}
