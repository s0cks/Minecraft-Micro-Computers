package mmc.api.fs;

import java.io.IOException;
import java.io.OutputStream;

public interface IWritableMount
extends IMount{
  public OutputStream openOutputStream(String path)
  throws IOException;

  public boolean mkdir(String path);
  public boolean touch(String path);
  public boolean rm(String path);
}