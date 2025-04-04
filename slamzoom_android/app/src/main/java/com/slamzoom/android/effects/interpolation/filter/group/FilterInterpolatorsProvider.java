package com.slamzoom.android.effects.interpolation.filter.group;

import com.slamzoom.android.effects.interpolation.filter.FilterInterpolator;

import java.util.List;

/**
 * Created by clocksmith on 4/22/16.
 */
public interface FilterInterpolatorsProvider {
  List<FilterInterpolator> getFilterInterpolators();
}
