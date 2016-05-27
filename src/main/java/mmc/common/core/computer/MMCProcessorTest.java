package mmc.common.core.computer;

import io.github.s0cks.mmc.assembler.Parser;

public class MMCProcessorTest {
  @org.junit.Test
  public void testTick()
  throws Exception {
    MMCProcessor processor = new MMCProcessor(null);
    processor.loadBinary((new Parser(System.class.getResourceAsStream("/assets/mmc/root/bios.S"))).compile());
    while(true){
      processor.tick();
    }
  }
}