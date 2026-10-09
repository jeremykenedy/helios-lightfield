package com.jeremykenedy.helioslightfield;

public final class SettingsValues {
  private SettingsValues() {}

  public static boolean isSupported(String key, String value) {
    if (key == null || value == null) return false;
    if ("speed".equals(key)) return oneOf(value, "slow", "medium", "fast", "random");
    if ("density".equals(key)) return oneOf(value, "few", "balanced", "many", "random");
    if ("glow".equals(key)) return oneOf(value, "soft", "luminous", "intense", "random");
    if ("palette".equals(key)) return oneOf(value, "solar", "polar", "random");
    if ("randomize_all".equals(key)) return oneOf(value, "true", "false");
    return false;
  }

  private static boolean oneOf(String value, String... allowed) {
    for (String option : allowed) if (option.equals(value)) return true;
    return false;
  }
}
