package com.jeremykenedy.helioslightfield;

final class LightGeometry {
  /**
   * Shading cost grows with pixels times strands, and a Fire TV GPU manages about 55 fps for 0.75
   * of a full-size frame of one strand. The scene is drawn at the size that fits that budget for
   * its strand count and scaled up by the display; the soft glow hides the difference.
   */
  static final float PIXEL_BUDGET = 0.75f;

  private LightGeometry() {}

  static float renderScale(float density) {
    return (float) Math.sqrt(PIXEL_BUDGET / Math.max(1f, density));
  }

  static int renderSize(int size, float density) {
    return Math.max(1, Math.round(size * renderScale(density)));
  }

  static float[] quadVertices() {
    return new float[] {-1f, -1f, 1f, -1f, -1f, 1f, 1f, 1f};
  }

  static float aspectScale(int width, int height) {
    if (width <= 0 || height <= 0) return 1f;
    return Math.min((float) width / height, 2.3f);
  }
}
