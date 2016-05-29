package mmc.client;

import mmc.common.MMC;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import java.io.InputStream;

public final class ShaderHelper{
  public static int compile(ResourceLocation fsh, ResourceLocation vsh){
    int prog = 0;
    try{
      int vertex = createShader(vsh, ARBVertexShader.GL_VERTEX_SHADER_ARB);
      int frag = createShader(fsh, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
      prog = ARBShaderObjects.glCreateProgramObjectARB();
      ARBShaderObjects.glAttachObjectARB(prog, vertex);
      ARBShaderObjects.glAttachObjectARB(prog, frag);
      ARBShaderObjects.glLinkProgramARB(prog);
      ARBShaderObjects.glValidateProgramARB(prog);
    } catch(Exception e){
      throw new RuntimeException(getLog(prog), e);
    }
    return prog;
  }

  public static int createShader(ResourceLocation code, int type){
    int shader = 0;
    try(InputStream in = MMC.proxy.client().getResourceManager().getResource(code).getInputStream()){
      shader = ARBShaderObjects.glCreateShaderObjectARB(type);
      if(shader == 0) throw new IllegalStateException("Couldn't compile shader");

      ARBShaderObjects.glShaderSourceARB(shader, IOUtils.toString(in));
      ARBShaderObjects.glCompileShaderARB(shader);

      if(ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE){
        throw new IllegalStateException("Cannot compile shader: " + code.toString());
      }

      return shader;
    } catch(Exception e){
      throw new RuntimeException("Error Creating Shader (" + type + "): "+ getLog(shader), e);
    }
  }

  private static String getLog(int obj){
    return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
  }
}