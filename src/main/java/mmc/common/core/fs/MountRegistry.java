package mmc.common.core.fs;

import com.google.common.collect.ImmutableSet;
import mmc.api.fs.IMount;
import mmc.api.fs.IMountRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MountRegistry
implements IMountRegistry{
  private final Map<String, IMount> mounts = new HashMap<>();

  @Override
  public void mount(String path, IMount mount) {
    if(this.mounts.containsKey(path)) return;
    this.mounts.put(path, mount);
  }

  @Override
  public void unmount(String path) {
    if(!this.mounts.containsKey(path)) return;
    this.mounts.remove(path);
  }

  @Override
  public Set<Map.Entry<String, IMount>> mounts() {
    return ImmutableSet.copyOf(this.mounts.entrySet());
  }
}