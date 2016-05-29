package mmc.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class ClientEventHandler{
  private static ClientEventHandler instance;

  public static ClientEventHandler instance(){
    return (instance == null ? (instance = new ClientEventHandler()) : instance);
  }

  private TextureAtlasSprite assemblerTop;
  private TextureAtlasSprite assemblerBottom;

  @SubscribeEvent
  public void onTextureStitch(TextureStitchEvent e){
    this.assemblerTop = e.getMap().registerSprite(new ResourceLocation("mmc", "textures/models/assembler_top.png"));
    this.assemblerBottom = e.getMap().registerSprite(new ResourceLocation("mmc", "textures/models/assembler_bottom.png"));
  }

  public TextureAtlasSprite assemblerTop(){
    return this.assemblerTop;
  }

  public TextureAtlasSprite assemblerBottom(){
    return this.assemblerBottom;
  }
}