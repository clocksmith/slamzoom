package com.slamzoom.android.interpolators;

/**
 * Created by clocksmith on 3/11/16.
 *
 * Base interpolator.
 */
public abstract class Interpolator implements Cloneable {
  private float start;
  private float end;

  /**
   * Default constructor. Creates an interpolator with default range of [0, 1].
   */
  public Interpolator() {
    setRange(0, 1);
  }

  /**
   * Creates an interpolator with a range of [{@code start}, {@code end}].
   * @param start the start value of the range.
   * @param end the end value of the range.
   */
  public Interpolator(float start, float end) {
    setRange(start, end);
  }

  /**
   * Returns the interpolation value for a given {@code t}.
   *
   * @param t input to the interpolator. The domain is [0,1].
   * @return the interpolation value with respect to the range.
   */
  public float getInterpolation(float t) {
    return getStart() + getRangePercent(t) * (getEnd() - getStart());
  }

  /**
   * This method defines the "type" of the interpolation. For example, returning {@code t} would define a linear
   * interpolator.
   *
   * @param t original input to the interpolator. See {@link #getInterpolation(float)}.
   * @return value to multiple to the range of the interpolator.
   */
  protected abstract float getRangePercent(float t);

  @Override
  public Interpolator clone() throws CloneNotSupportedException {
    return (Interpolator) super.clone();
  }

  public void setRange(float start, float end) {
    this.start = start;
    this.end = end;
  }

  public float getStart() {
    return start;
  }

  public float getEnd() {
    return end;
  }
}
