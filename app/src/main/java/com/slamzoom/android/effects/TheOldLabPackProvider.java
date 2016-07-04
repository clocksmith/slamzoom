package com.slamzoom.android.effects;

import com.google.common.collect.Lists;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeDoubleLeftRightFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeEyesSwirlMouthFilterInterpoaltor;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.BulgeLeftRightSwapFilterInterpolationGroup;
import com.slamzoom.android.effects.interpolation.filter.group.DeflateFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.InflateFaceFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.group.SumoBulge2FilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.SumoBulgeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.SwirlEyesTwistyMouthFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.group.UnswirlEyesFilterInterpolatorGroup;
import com.slamzoom.android.effects.interpolation.filter.single.BulgeInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianBlurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.GaussianUnblurFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.OverExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ShrinkInAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnderExposeFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnsaturateFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.UnswirlTurntableAtHotspotOnHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.filter.single.ZoomBlurAtHotspotFilterInterpolator;
import com.slamzoom.android.effects.interpolation.transform.TranslateInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CircleCenterInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceBottomInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashBounceDiagonalInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashMissInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashRumbleInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.CrashTaranInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.FlushInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.scaletranslate.ShakeSwitchInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.BaseShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.MegaShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.ShakeInterpolatorProvider;
import com.slamzoom.android.effects.interpolation.transform.translate.SuperShakeInterpolatorProvider;
import com.slamzoom.android.interpolators.Interpolator;
import com.slamzoom.android.interpolators.LinearInterpolator;
import com.slamzoom.android.interpolators.ReverseLinearInterpolator;
import com.slamzoom.android.interpolators.custom.HalfInAndOutInterpolator;
import com.slamzoom.android.interpolators.custom.InAndOutInterpolator;
import com.slamzoom.android.interpolators.custom.OutAndInInterpolator;
import com.slamzoom.android.interpolators.custom.OvershootInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardInAndOutInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardInterpolator;
import com.slamzoom.android.interpolators.custom.SlamHardNoPauseInterpolator;
import com.slamzoom.android.interpolators.custom.SlamSoftInterpolator;
import com.slamzoom.android.interpolators.custom.SlamSoftOutInterpolator;
import com.slamzoom.android.interpolators.custom.SlamSoftOutNoPauseInterpolator;
import com.slamzoom.android.interpolators.spline.CubicSplineInterpolator;
import com.slamzoom.android.interpolators.spline.LinearSplineInterpolator;
import com.slamzoom.android.interpolators.spline.PointsBuilder;

import java.util.List;

/**
 * Created by clocksmith on 3/31/16.
 */
public class TheOldLabPackProvider {
  public static List<EffectTemplate> getPack() {
    List<EffectTemplate> packModels = Lists.newArrayList();

    packModels.add(EffectTemplate.newBuilder()
        .withName("crashdiag")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceDiagonalInterpolatorProvider())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("flush")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleAndTranslateInterpolatorProvider(new FlushInterpolatorProvider())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("spiral")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleAndTranslateInterpolatorProvider(new CircleCenterInterpolatorProvider())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlout")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new ReverseLinearInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("twistyfrog")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeEyesSwirlMouthFilterInterpoaltor())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("twistyfroggy")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolatorGroup(new BulgeEyesSwirlMouthFilterInterpoaltor())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamSoftInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
            .withEndPauseSeconds(0.5f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlyhead")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirleyes")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new UnswirlEyesFilterInterpolatorGroup())
            .withEndPauseSeconds(1)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlyeyes")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolatorGroup(new UnswirlEyesFilterInterpolatorGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlaway")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotFilterInterpolator())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(0.5f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurcrash")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(0.8f)
            .withScaleInterpolator(new CubicSplineInterpolator(PointsBuilder.create()
                .withPoint(0, 0)
                .withPoint(0.3f, 1f)
                .withPoint(1f, 1f)
                .build()))
            .withFilterInterpolator(new GaussianUnblurFilterInterpolator(
                new CubicSplineInterpolator(PointsBuilder.create()
                    .withPoint(0, 0f)
                    .withPoint(0.8f, 0)
                    .withPoint(1, 1)
                    .build())))
            .withEndPauseSeconds(0.5f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurshake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new GaussianUnblurFilterInterpolator())
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blursmith")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearSplineInterpolator(PointsBuilder.create()
                .withPoint(0, 0)
                .withPoint(0.4f, 0)
                .withPoint(0.6f, 1)
                .withPoint(1, 1)
                .build()))
            .withFilterInterpolator(new GaussianBlurFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.4f, 1)
                    .withPoint(0.6f, 1)
                    .withPoint(1, 0)
                    .build())))
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurmagic")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearSplineInterpolator(PointsBuilder.create()
                .withPoint(0, 0)
                .withPoint(0.4999f, 0)
                .withPoint(0.5f, 1)
                .withPoint(1, 1)
                .build()))
//            .withFilterInterpolator(new GuassianBlurFilterInterpolator(new LinearSplineInterpolator(
//                PointListBuilder.create()
//                    .withPoint(0, 0)
//                    .withPoint(0.5f, 1)
//                    .withPoint(1, 0)
//                    .build())) {
//              @Override
//              public float getBlurSize() {
//                mBlurCalculator.setBaseValue(10);
//                return mBlurCalculator.getValueFromInterpolation();
//              }
//            })
            .withEndPauseSeconds(1f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("crashmiss")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.75f)
            .withDurationSeconds(0.75f)
            .withEndPauseSeconds(2f)
            .withScaleAndTranslateInterpolatorProvider(new CrashMissInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashin")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withEndPauseSeconds(2f)
            .withScaleAndTranslateInterpolatorProvider(new CrashBounceBottomInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("overcrash")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1f)
            .withEndPauseSeconds(2f)
            .withScaleInterpolator(new OvershootInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashblur")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.75f)
            .withDurationSeconds(0.75f)
            .withEndPauseSeconds(2f)
            .withScaleAndTranslateInterpolatorProvider(new CrashTaranInterpolatorProvider())
            .withFilterInterpolator(new GaussianBlurFilterInterpolator(
                new LinearSplineInterpolator(PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.3f, 1)
                    .withPoint(0.6f, 1)
                    .withPoint(0.9f, 0)
                    .withPoint(1, 0)
                    .build())) {
              @Override
              public float getBlurSize() {
                return super.getBlurSize() * 2;
              }
            })
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("crashrumble")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(1.5f)
            .withScaleAndTranslateInterpolatorProvider(new CrashRumbleInterpolatorProvider())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("tile")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile4")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(2)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile9")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(3)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile16")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(4)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("tile25")
        .addEffectStep(EffectStep.newBuilder()
            .withEndPauseSeconds(1)
            .withScaleInterpolator(new LinearInterpolator())
            .build())
        .withNumTilesInRow(5)
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("bulger")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new BulgeInAtHotspotFilterInterpolator())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("shrinkydink")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new ShrinkInAtHotspotFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("smush")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeFaceFilterInterpolatorGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("inflate")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new InflateFaceFilterInterpolatorGroup())
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("sumo")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new SumoBulgeFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("doublebulge")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeDoubleLeftRightFilterInterpolatorGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("magoo")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeEyesFilterInterpolatorGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("deflate")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new DeflateFaceFilterInterpolatorGroup())
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("bulgeswap")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new BulgeLeftRightSwapFilterInterpolationGroup())
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("weirdo")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new SumoBulge2FilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayson")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnsaturateFilterInterpolator())
            .withEndPauseSeconds(2f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.7f, 0)
                    .withPoint(0.9f, 1f)
                    .withPoint(0.9999f, 1f)
                    .withPoint(1f, 0)
                    .build())))
            .withFilterInterpolator(new UnsaturateFilterInterpolator())
            .withEndPauseSeconds(2f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayfreeze")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0.5f)
            .withDurationSeconds(2f)
            .withScaleInterpolator(new CubicSplineInterpolator(PointsBuilder.create()
                .withPoint(0, 0)
                .withPoint(0.15f, 1f)
                .withPoint(1f, 1f)
                .build()))
            .withFilterInterpolator(new UnsaturateFilterInterpolator(
                new CubicSplineInterpolator(PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.5f, 0)
                    .withPoint(1f, 1f)
                    .build())))
            .withEndPauseSeconds(2f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("grayrumble")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withFilterInterpolator(new UnsaturateFilterInterpolator())
            .withEndPauseSeconds(2f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("graytake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new OutAndInInterpolator())
            .withFilterInterpolator(new UnsaturateFilterInterpolator(new LinearInterpolator()))
            .withEndPauseSeconds(2f)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("rumble")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("earthquake")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new SuperShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("shakezilla")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new HalfInAndOutInterpolator())
            .withTranslateInterpolator(new MegaShakeInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("rumbleslam")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new SlamHardInterpolator())
            .withTranslateInterpolator(new ShakeInterpolatorProvider())
            .withEndPauseSeconds(1)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("rumblestiltskin")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(3)
            .withScaleAndTranslateInterpolatorProvider(new ShakeSwitchInterpolatorProvider())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamin")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1.4f)
            .withDurationSeconds(0.6f)
            .withEndPauseSeconds(2)
            .withScaleInterpolator(new SlamHardNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.9f, 1)
                    .withPoint(0.9999f, 1)
                    .withPoint(1, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamout")
        .addEffectStep(EffectStep.newBuilder()
//            .withStartPauseSeconds(1.2f)
//            .withDurationSeconds(0.8f)
            .withEndPauseSeconds(2)
//            .withScaleInterpolator(new SlamSoftOutNoPauseInterpolator())
            .withScaleInterpolator(new SlamSoftOutInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.8f, 1)
                    .withPoint(1, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withPackName("slam pack")
        .withName("slamio")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(1f)
            .withScaleInterpolator(new SlamHardInAndOutInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.35f, 0)
                    .withPoint(0.45f, 1f)
                    .withPoint(0.4999f, 1f)
                    .withPoint(0.5f, 0)
                    .withPoint(0.85f, 0)
                    .withPoint(0.95f, 1f)
                    .withPoint(0.9999f, 1f)
                    .withPoint(1f, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("slamfinity")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0)
            .withDurationSeconds(0.5f)
            .withScaleInterpolator(new SlamHardNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.9f, 1f)
                    .withPoint(0.9999f, 1f)
                    .withPoint(1f, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("slamfunity")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(0)
            .withDurationSeconds(0.5f)
            .withScaleInterpolator(new SlamSoftOutNoPauseInterpolator())
            .withFilterInterpolator(new ZoomBlurAtHotspotFilterInterpolator(new LinearSplineInterpolator(
                PointsBuilder.create()
                    .withPoint(0, 0)
                    .withPoint(0.9f, 1f)
                    .withPoint(0.9999f, 1f)
                    .withPoint(1f, 0)
                    .build())))
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlin")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlinout")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new InAndOutInterpolator())
            .withFilterInterpolator(new UnswirlFilterInterpolator())
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("swirlspot")
        .addEffectStep(EffectStep.newBuilder()
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlAtHotspotOnHotspotFilterInterpolator())
            .withDurationSeconds(3f)
            .withEndPauseSeconds(2)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("djswirls")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolator(new UnswirlTurntableAtHotspotOnHotspotFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("mctwisty")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(2)
            .withScaleInterpolator(new LinearInterpolator())
            .withFilterInterpolatorGroup(new SwirlEyesTwistyMouthFilterInterpolator())
            .withEndPauseSeconds(2)
            .build())
        .build());
    packModels.add(EffectTemplate.newBuilder()
        .withName("blacktease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4f)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getValue(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.8333) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new UnderExposeFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getValue(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("whitetease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getValue(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.8333) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new OverExposeFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getValue(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("blurtease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getValue(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.8333) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new GaussianBlurFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getValue(float t) {
                    return (float) Math.pow(Math.sin(3 * Math.PI * t), 10);
                  }
                }))
            .withEndPauseSeconds(2)
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("rumbletease")
        .addEffectStep(EffectStep.newBuilder()
            .withDurationSeconds(6)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getValue(float t) {
                if (t < 0.25) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.75) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withTranslateInterpolator(new TranslateInterpolatorProvider() {
              private BaseShakeInterpolatorProvider first = new BaseShakeInterpolatorProvider(6, 12);
              private BaseShakeInterpolatorProvider second = new BaseShakeInterpolatorProvider(5, 15);
              private BaseShakeInterpolatorProvider third = new BaseShakeInterpolatorProvider(4, 20);
              private BaseShakeInterpolatorProvider fourth = new BaseShakeInterpolatorProvider(2, 30);
              @Override
              public Interpolator getXInterpolator() {
                return new Interpolator() {
                  @Override
                  protected float getValue(float t) {
                    if (t < 0.25) {
                      return first.getXInterpolator().getInterpolation(t);
                    } else if (t < 0.5) {
                      return second.getXInterpolator().getInterpolation(t);
                    } else if (t < 0.75) {
                      return third.getXInterpolator().getInterpolation(t);
                    } else {
                      return fourth.getXInterpolator().getInterpolation(t);
                    }
                  }
                };
              }

              @Override
              public Interpolator getYInterpolator() {
                return new Interpolator() {
                  @Override
                  protected float getValue(float t) {
                    if (t < 0.25) {
                      return first.getYInterpolator().getInterpolation(t);
                    } else if (t < 0.5) {
                      return second.getYInterpolator().getInterpolation(t);
                    } else if (t < 0.75) {
                      return third.getYInterpolator().getInterpolation(t);
                    } else {
                      return fourth.getYInterpolator().getInterpolation(t);
                    }
                  }
                };
              }
            })
            .build())
        .build());

    packModels.add(EffectTemplate.newBuilder()
        .withName("graytease")
        .addEffectStep(EffectStep.newBuilder()
            .withStartPauseSeconds(1)
            .withDurationSeconds(4)
            .withScaleInterpolator(new Interpolator() {
              @Override
              protected float getValue(float t) {
                if (t < 0.1667) {
                  return 0;
                } else if (t < 0.5) {
                  return 0.2f;
                } else if (t < 0.8333) {
                  return 0.45f;
                } else {
                  return 1;
                }
              }
            })
            .withFilterInterpolator(new UnsaturateFilterInterpolator(
                new Interpolator() {
                  @Override
                  protected float getValue(float t) {
                    return (float) Math.pow(Math.sin(3.5 * Math.PI * (t + 2f / 7)), 2);
                  }
                }))
            .withEndPauseSeconds(2)
            .build())
        .build());


    return packModels;
  }
}
