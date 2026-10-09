package com.jeremykenedy.helioslightfield;

final class LightGeometry {
  private LightGeometry() {}

  static float[] quadVertices() {
    return new float[] {-1f, -1f, 1f, -1f, -1f, 1f, 1f, 1f};
  }

  static float aspectScale(int width, int height) {
    if (width <= 0 || height <= 0) return 1f;
    return Math.min((float) width / height, 2.3f);
  }
}
