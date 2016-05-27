package mmc.api.computer;

public interface ITerminal{
  public enum State{
    ON, OFF;
  }

  public String id();
  public void onKeydown(int code, char c);
  public void turnOff();
  public void turnOn();
  public IProcessor owner();
  public String line(int i);
  public State state();
}