package mmc.test;

import io.github.s0cks.mmc.Binary;
import io.github.s0cks.mmc.assembler.Parser;
import io.github.s0cks.mmc.linker.Linker;
import io.github.s0cks.mmc.util.Disassembler;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

public final class TestBios{
  @Test
  public void test()
  throws Exception {
    try(InputStream biosIn = System.class.getResourceAsStream("/assets/mmc/root/bios.S");
        InputStream stdlibIn = System.class.getResourceAsStream("/assets/mmc/root/stdlib.S")){
      Linker.SimpleObjectFile stdlibSof;
      try(ByteArrayOutputStream stdlibOut = new ByteArrayOutputStream()){
        (new Parser(stdlibIn)).compile(stdlibOut);
        stdlibSof= new Linker.SimpleObjectFile(new ByteArrayInputStream(stdlibOut.toByteArray()));
      }

      Binary bin = (new Linker()).link(biosIn, stdlibSof);
      Disassembler.dump(new PrintWriter(System.out), bin);
    }
  }
}