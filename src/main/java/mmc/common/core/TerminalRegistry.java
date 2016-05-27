package mmc.common.core;

import mmc.api.computer.ITerminal;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class TerminalRegistry<T extends ITerminal>{
  private final Map<String, T> terminals = new ConcurrentHashMap<>();
  private final Lock lock = new ReentrantLock();

  public abstract void update();

  public Collection<T> all(){
    try{
      this.lock.lock();
      return this.terminals.values();
    } finally{
      this.lock.unlock();
    }
  }

  public void reset(){
    try{
      this.lock.lock();
      this.terminals.clear();
    } finally{
      this.lock.unlock();
    }
  }

  public boolean contains(String id){
    try {
      this.lock.lock();
      return id != null && this.terminals.containsKey(id);
    } finally{
      this.lock.unlock();
    }
  }

  public T get(String id){
    try{
      this.lock.lock();
      return this.terminals.get(id);
    } finally{
      this.lock.unlock();
    }
  }

  public void remove(String id){
    try{
      this.lock.lock();
      if(this.terminals.containsKey(id)){
        this.terminals.remove(id);
      }
    } finally{
      this.lock.unlock();
    }
  }

  public void register(T terminal){
    try{
      this.lock.lock();

      if(this.terminals.containsKey(terminal.id())){
        this.terminals.remove(terminal.id());
      }

      this.terminals.put(terminal.id(), terminal);
    } finally{
      this.lock.unlock();
    }
  }
}