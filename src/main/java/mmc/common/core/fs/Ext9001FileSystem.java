package mmc.common.core.fs;

import mmc.api.MMCApi;
import mmc.api.fs.IFileSystem;
import mmc.api.fs.IMount;
import mmc.api.fs.IWritableMount;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public final class Ext9001FileSystem
implements IFileSystem {
  private String sanitize(String path, boolean wildcards) {
    path = path.replace("\\", "/");
    char[] specChars = {'"', ':', '<', '>', '?', '|'};
    StringBuilder clean = new StringBuilder();
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if ((c >= ' ') && Arrays.binarySearch(specChars, c) < 0 && (wildcards || (c != '*'))) {
        clean.append(c);
      }
    }
    path = clean.toString();

    String[] parts = path.split("/");
    Stack<String> output = new Stack<>();
    for (String part : parts) {
      if ((part.length() != 0) && (!part.equals("."))) {
        if (part.equals("..")) {
          if (!output.empty()) {
            String top = output.peek();
            if (!top.equals("..")) {
              output.pop();
            } else {
              output.push("..");
            }
          } else {
            output.push("..");
          }
        } else if (part.length() >= 255) {
          output.push(part.substring(0, 255));
        } else {
          output.push(part);
        }
      }
    }

    StringBuilder res = new StringBuilder("");
    Iterator<String> it = output.iterator();
    while (it.hasNext()) {
      String part = it.next();
      res.append(part);
      if (it.hasNext()) {
        res.append("/");
      }
    }
    return res.toString();
  }

  private IMount getMount(String path) {
    Iterator<Map.Entry<String, IMount>> it = MMCApi.MOUNT_REGISTRY.mounts()
                                                                  .iterator();
    int len = 0;
    IMount match = null;
    while (it.hasNext()) {
      Map.Entry<String, IMount> next = it.next();
      if (this.contains(next.getKey(), path)) {
        int l = this.toLocal(path, next.getKey())
                    .length();
        if ((match == null) || (l < len)) {
          match = next.getValue();
          len = l;
        }
      }
    }

    if (match == null) {
      throw new IllegalStateException("Invalid path");
    }

    return match;
  }

  private boolean contains(String a, String b) {
    a = this.sanitize(a, false);
    b = this.sanitize(b, false);
    return !(b.equals("..") || b.startsWith("../"))
           && (b.equals(a) || a.isEmpty() || b.startsWith(a + "/"));
  }

  private String toLocal(String path, String loc) {
    path = this.sanitize(path, false);
    loc = this.sanitize(loc, false);

    String local = path.substring(loc.length());
    if (local.startsWith("/")) {
      return local.substring(1);
    }

    return local;
  }

  @Override
  public OutputStream openOutputStream(String path)
  throws IOException {
    try {
      path = this.sanitize(path, false);
      IMount m = this.getMount(path);
      return m instanceof IWritableMount
             ? ((IWritableMount) m).openOutputStream(path)
             : null;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  @Override
  public InputStream openInputStream(String path)
  throws IOException {
    try {
      path = this.sanitize(path, false);
      IMount m = this.getMount(path);
      return m != null
             ? m.openInputStream(path)
             : null;
    } catch(Exception e){
      throw new IOException(e);
    }
  }

  @Override
  public void ls(String path, List<String> list) {
    path = this.sanitize(path, false);
    IMount m = this.getMount(path);
    m.list(path, list);
  }

  @Override
  public Path resolve(String path) {
    path = this.sanitize(path, false);
    IMount m = this.getMount(path);
    return m.resolve(path);
  }

  @Override
  public boolean mkdir(String path) {
    path = this.sanitize(path, false);
    IMount m = this.getMount(path);
    return (m instanceof IWritableMount) && ((IWritableMount) m).mkdir(path);
  }

  @Override
  public boolean touch(String path) {
    path = this.sanitize(path, false);
    IMount m = this.getMount(path);
    return (m instanceof IWritableMount) && ((IWritableMount) m).touch(path);
  }

  @Override
  public boolean exists(String path) {
    path = this.sanitize(path, false);
    IMount m = this.getMount(path);
    return m != null && m.exists(path);
  }

  @Override
  public boolean isDirectory(String path) {
    path = this.sanitize(path, false);
    IMount m = this.getMount(path);
    return m != null && m.isDirectory(path);
  }
}