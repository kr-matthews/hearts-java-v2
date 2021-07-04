package Players;

import java.io.PrintStream;

public class NullOutputStream extends PrintStream {
  public NullOutputStream() {
    super(System.out);
  }

  @Override
  public void write(byte[] b) {
    return;
  }

  @Override
  public void write(byte[] b, int off, int len) {
    return;
  }

  @Override
  public void write(int b) {
  }
}
