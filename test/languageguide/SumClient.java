// SumClient.java
public class SumClient {
  private Sum sum;
  public SumClient(Sum sum) {
    this.sum = sum;
  }
  public int addUpInts(int[] ints) {
    int result = 0;
    for (int i = 0; i < ints.length; i++) {
      result = sum.addInts(result, ints[i]);
    }
    return result;
  }
  public double addUpDoubles(double[] doubles) {
    double result = 0.0;
    for (int i = 0; i < doubles.length; i++) {
      result = sum.addDoubles(result, doubles[i]);
    }
    return result;
  }
}
