package com.jeremykenedy.helioslightfield;

import static org.junit.Assert.*;

import java.util.Random;
import org.junit.Test;

public class LightOptionsTest {
  @Test
  public void resolvesEverySettingChoice() {
    LightOptions slow = LightOptions.resolve("slow", "few", "soft", "solar", false, new Random(1));
    assertEquals(0.48f, slow.speed, 0f);
    assertEquals(4f, slow.density, 0f);
    assertEquals(0.55f, slow.glow, 0f);
    assertEquals(0f, slow.palette, 0f);
    LightOptions fast =
        LightOptions.resolve("fast", "many", "intense", "polar", false, new Random(2));
    assertEquals(1.4f, fast.speed, 0f);
    assertEquals(9f, fast.density, 0f);
    assertEquals(1.4f, fast.glow, 0f);
    assertEquals(1f, fast.palette, 0f);
    LightOptions medium =
        LightOptions.resolve("medium", "balanced", "luminous", "solar", false, new Random(3));
    assertEquals(0.85f, medium.speed, 0f);
    assertEquals(6f, medium.density, 0f);
    assertEquals(1f, medium.glow, 0f);
  }

  @Test
  public void randomAndInvalidSettingsResolveWithinSupportedValues() {
    LightOptions random =
        LightOptions.resolve("random", "random", "random", "random", false, new Random(4));
    assertTrue(random.speed == 0.48f || random.speed == 0.85f || random.speed == 1.4f);
    assertTrue(random.density == 4f || random.density == 6f || random.density == 9f);
    assertTrue(random.glow == 0.55f || random.glow == 1f || random.glow == 1.4f);
    assertTrue(random.palette == 0f || random.palette == 1f);
    LightOptions invalid =
        LightOptions.resolve("invalid", "invalid", "invalid", "invalid", false, new Random(1));
    assertEquals(0.48f, invalid.speed, 0f);
    assertEquals(6f, invalid.density, 0f);
    assertEquals(1f, invalid.glow, 0f);
    assertEquals(0f, invalid.palette, 0f);
  }

  @Test
  public void globalRandomizationResolvesAllControls() {
    LightOptions result = LightOptions.resolve("slow", "few", "soft", "polar", true, new Random(7));
    assertTrue(result.speed == 0.48f || result.speed == 0.85f || result.speed == 1.4f);
    assertTrue(result.density == 4f || result.density == 6f || result.density == 9f);
    assertTrue(result.glow == 0.55f || result.glow == 1f || result.glow == 1.4f);
    assertTrue(result.palette == 0f || result.palette == 1f);
  }

  @Test
  public void validatesSettingsSchemaAndValues() {
    assertTrue(SettingsValues.isSupported("speed", "random"));
    assertTrue(SettingsValues.isSupported("density", "many"));
    assertTrue(SettingsValues.isSupported("glow", "intense"));
    assertTrue(SettingsValues.isSupported("palette", "polar"));
    assertTrue(SettingsValues.isSupported("randomize_all", "false"));
    assertFalse(SettingsValues.isSupported(null, "slow"));
    assertFalse(SettingsValues.isSupported("speed", null));
    assertFalse(SettingsValues.isSupported("speed", "warp"));
    assertFalse(SettingsValues.isSupported("density", "tons"));
    assertFalse(SettingsValues.isSupported("glow", "maximum"));
    assertFalse(SettingsValues.isSupported("palette", "green"));
    assertFalse(SettingsValues.isSupported("randomize_all", "yes"));
    assertFalse(SettingsValues.isSupported("unknown", "anything"));
  }

  @Test
  public void geometryUsesSafeBoundsAndAspect() {
    float[] quad = LightGeometry.quadVertices();
    assertArrayEquals(new float[] {-1f, -1f, 1f, -1f, -1f, 1f, 1f, 1f}, quad, 0f);
    assertEquals(1f, LightGeometry.aspectScale(0, 1080), 0f);
    assertEquals(1f, LightGeometry.aspectScale(1920, 0), 0f);
    assertEquals(16f / 9f, LightGeometry.aspectScale(1920, 1080), 0.001f);
    assertEquals(2.3f, LightGeometry.aspectScale(3840, 1000), 0f);
  }

  @Test
  public void renderSizeKeepsShadingWorkWithinTheTvBudget() {
    for (float density : new float[] {4f, 6f, 9f}) {
      float scale = LightGeometry.renderScale(density);
      assertEquals(LightGeometry.PIXEL_BUDGET, scale * scale * density, 0.0001f);
    }
    assertEquals(679, LightGeometry.renderSize(1920, 6f));
    assertEquals(382, LightGeometry.renderSize(1080, 6f));
    assertTrue(LightGeometry.renderSize(1920, 9f) < LightGeometry.renderSize(1920, 4f));
    assertEquals(LightGeometry.renderScale(1f), LightGeometry.renderScale(0f), 0f);
    assertEquals(1, LightGeometry.renderSize(1, 6f));
    assertEquals(1, LightGeometry.renderSize(0, 6f));
  }
}
