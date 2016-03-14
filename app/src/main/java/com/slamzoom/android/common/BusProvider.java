package com.slamzoom.android.common;

import com.squareup.otto.Bus;

/**
 * Created by clocksmith on 3/7/16.
 */
public final class BusProvider {
  private static final Bus BUS = new Bus();

  public static Bus getInstance() {
    return BUS;
  }

  private BusProvider() {}
}
