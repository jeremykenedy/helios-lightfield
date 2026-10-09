package com.jeremykenedy.helioslightfield;

import java.util.Random;

public final class LightOptions {
  public final float speed;
  public final float density;
  public final float glow;
  public final float palette;

  private LightOptions(float speed, float density, float glow, float palette) {
    this.speed = speed;
    this.density = density;
    this.glow = glow;
    this.palette = palette;
  }

  public static LightOptions resolve(
      String speed,
      String density,
      String glow,
      String palette,
      boolean randomizeAll,
      Random random) {
    String selectedSpeed = choose(speed, randomizeAll, random, "slow", "medium", "fast");
    String selectedDensity = choose(density, randomizeAll, random, "balanced", "few", "many");
    String selectedGlow = choose(glow, randomizeAll, random, "luminous", "soft", "intense");
    String selectedPalette = choose(palette, randomizeAll, random, "solar", "polar");
    return new LightOptions(
        speedFor(selectedSpeed),
        densityFor(selectedDensity),
        glowFor(selectedGlow),
        paletteFor(selectedPalette));
  }

  static String choose(String selected, boolean randomizeAll, Random random, String... values) {
    if (randomizeAll || "random".equals(selected)) return values[random.nextInt(values.length)];
    for (String value : values) if (value.equals(selected)) return selected;
    return values[0];
  }

  static float speedFor(String value) {
    if ("medium".equals(value)) return 0.85f;
    if ("fast".equals(value)) return 1.4f;
    return 0.48f;
  }

  static float densityFor(String value) {
    if ("few".equals(value)) return 4f;
    if ("many".equals(value)) return 9f;
    return 6f;
  }

  static float glowFor(String value) {
    if ("soft".equals(value)) return 0.55f;
    if ("intense".equals(value)) return 1.4f;
    return 1f;
  }

  static float paletteFor(String value) {
    return "polar".equals(value) ? 1f : 0f;
  }
}
