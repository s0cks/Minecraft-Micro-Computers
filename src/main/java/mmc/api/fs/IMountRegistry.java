package mmc.api.fs;

import java.util.Map;
import java.util.Set;

public interface IMountRegistry{
  public void mount(String path, IMount mount);
  public void unmount(String path);
  public Set<Map.Entry<String, IMount>> mounts();
}