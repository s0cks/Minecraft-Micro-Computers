package mmc.client.gui;

import mmc.common.core.ClientTerminal;
import mmc.common.tile.TileEntityComputer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public final class GuiComputer
extends GuiScreen {
  private static final ResourceLocation texture = new ResourceLocation("mmc", "textures/gui/display.png");
  private static final float scaleFactor = 0.45F;
  private static final int xSize = 710;
  private static final int ySize = 510;

  private final ClientTerminal terminal;

  public GuiComputer(TileEntityComputer computer){
    this.terminal = computer.createClientTerminal();
  }

  @Override
  public void initGui() {
    super.initGui();
    Keyboard.enableRepeatEvents(true);
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    GlStateManager.pushMatrix();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);

    int guiLeft = ((int) ((this.width - (xSize * scaleFactor)) / (2 * scaleFactor))) - ((int) (xSize * 0.125F));
    int guiTop = ((int) ((this.height - (ySize * scaleFactor)) / (2 * scaleFactor))) - 10;

    this.drawTerminalTexture(guiLeft, guiTop);
    GlStateManager.popMatrix();

    GlStateManager.pushMatrix();
    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
    GlStateManager.disableTexture2D();
    this.drawBackground(guiLeft + 32, guiTop + 22, 835, 450, 0x111111);
    GlStateManager.enableTexture2D();
    GlStateManager.popMatrix();

    GlStateManager.pushMatrix();
    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
    GlStateManager.translate(guiLeft + 36, guiTop + 26, 100.0F);
    GlStateManager.scale(2.0F, 2.0F, 2.0F);

    int y = 0;
    for(int i = 0; i <= 11; i++){
      this.fontRendererObj.drawString(this.terminal.line(i), 0, y, 0xFFFFFF);
      y += this.fontRendererObj.FONT_HEIGHT + 2;
    }

    GlStateManager.popMatrix();
  }

  @Override
  public void handleKeyboardInput()
  throws IOException {
    super.handleKeyboardInput();
  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }

  private void drawBackground(int x, int y, int width, int height, int color) {
    Tessellator tess = Tessellator.getInstance();
    VertexBuffer vb = tess.getBuffer();

    int r = (color >> 16 & 0xFF);
    int g = (color >> 8 & 0xFF);
    int b = (color & 0xFF);

    vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
    vb.pos(x, y, 1.0)
      .color(r, g, b, 255)
      .endVertex();
    vb.pos(x, y + height, 1.0)
      .color(r, g, b, 255)
      .endVertex();
    vb.pos(x + width, y + height, 1.0)
      .color(r, g, b, 255)
      .endVertex();
    vb.pos(x + width, y, 1.0)
      .color(r, g, b, 255)
      .endVertex();
    tess.draw();
  }

  private void drawTerminalTexture(int guiLeft, int guiTop){
    this.mc.renderEngine.bindTexture(texture);
    Tessellator tess = Tessellator.getInstance();
    VertexBuffer vb = tess.getBuffer();
    vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    vb.pos(guiLeft, guiTop, this.zLevel)
      .tex(0, 0)
      .endVertex();
    vb.pos(guiLeft, guiTop + (ySize * 1.5F), this.zLevel)
      .tex(0, 1)
      .endVertex();
    vb.pos(guiLeft + (xSize * 1.5F), guiTop + (ySize * 1.5F), this.zLevel)
      .tex(1, 1)
      .endVertex();
    vb.pos(guiLeft + (xSize * 1.5F), guiTop, this.zLevel)
      .tex(1, 0)
      .endVertex();
    tess.draw();
  }
}