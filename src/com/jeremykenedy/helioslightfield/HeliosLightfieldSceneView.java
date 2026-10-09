package com.jeremykenedy.helioslightfield;

import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.preference.PreferenceManager;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

final class HeliosLightfieldSceneView extends GLSurfaceView implements GLSurfaceView.Renderer {
  private static final String VERTEX_SHADER =
      "attribute vec2 aPosition; varying vec2 vUv;"
          + "void main(){vUv=aPosition*0.5+0.5;gl_Position=vec4(aPosition,0.0,1.0);}";
  private static final String FRAGMENT_SHADER =
      "precision mediump float; varying vec2 vUv;"
          + "uniform float uTime; uniform float uSpeed; uniform float uGlow;"
          + "uniform float uDensity; uniform float uPalette; uniform vec2 uAspect;"
          + "void main(){vec2 p=(vUv-0.5)*uAspect; float t=uTime*uSpeed;"
          + "vec3 color=vec3(0.004,0.008,0.018);"
          + "float haze=exp(-dot(p,p)*2.2); color+=vec3(0.014,0.024,0.052)*haze;"
          + "for(int i=0;i<10;i++){float fi=float(i); if(fi>=uDensity) break;"
          + "float phase=t*(0.22+fi*0.013)+fi*0.63;"
          + "float c=cos(phase*0.21),s=sin(phase*0.21);"
          + "vec2 q=mat2(c,-s,s,c)*p;"
          + "float a=0.38+fi*0.009; float b=0.245+fi*0.005;"
          + "float ang=atan(q.y/b,q.x/a); float wave=sin(ang*(2.0+mod(fi,3.0))+phase)*0.055;"
          + "float ring=abs(length(vec2(q.x/a,q.y/b))-(0.92+wave));"
          + "float width=0.007+0.002*sin(phase+ang*3.0);"
          + "float core=exp(-ring*ring/(width*width));"
          + "float bloom=exp(-ring*5.5)*0.17;"
          + "float pulse=0.72+0.28*sin(phase*1.7+ang*2.0);"
          + "vec3 warm=mix(vec3(1.0,0.24,0.035),vec3(1.0,0.83,0.42),0.5+0.5*sin(fi*0.74+ang));"
          + "vec3 cool=mix(vec3(0.06,0.33,1.0),vec3(0.38,0.95,1.0),0.5+0.5*sin(fi*0.74+ang));"
          + "vec3 hue=mix(warm,cool,uPalette); color+=(core+bloom)*pulse*hue*uGlow;"
          + "} gl_FragColor=vec4(color,1.0);}";

  private final FloatBuffer vertices;
  private final LightOptions options;
  private final long startedAt = System.nanoTime();
  private int program;
  private int timeLocation;
  private int speedLocation;
  private int glowLocation;
  private int densityLocation;
  private int paletteLocation;
  private int aspectLocation;
  private int width;
  private int height;
  private volatile boolean running;

  HeliosLightfieldSceneView(Context context) {
    super(context);
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    options =
        LightOptions.resolve(
            preferences.getString("speed", "slow"),
            preferences.getString("density", "balanced"),
            preferences.getString("glow", "luminous"),
            preferences.getString("palette", "solar"),
            preferences.getBoolean("randomize_all", false),
            new Random(System.currentTimeMillis()));
    vertices = ByteBuffer.allocateDirect(8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    vertices.put(LightGeometry.quadVertices()).position(0);
    setEGLContextClientVersion(2);
    setPreserveEGLContextOnPause(true);
    setRenderer(this);
    setRenderMode(RENDERMODE_WHEN_DIRTY);
  }

  void start() {
    running = true;
    setRenderMode(RENDERMODE_CONTINUOUSLY);
  }

  void stop() {
    running = false;
    setRenderMode(RENDERMODE_WHEN_DIRTY);
  }

  @Override
  public void onSurfaceCreated(GL10 unused, EGLConfig config) {
    GLES20.glClearColor(0.004f, 0.008f, 0.018f, 1f);
    program = createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
    timeLocation = GLES20.glGetUniformLocation(program, "uTime");
    speedLocation = GLES20.glGetUniformLocation(program, "uSpeed");
    glowLocation = GLES20.glGetUniformLocation(program, "uGlow");
    densityLocation = GLES20.glGetUniformLocation(program, "uDensity");
    paletteLocation = GLES20.glGetUniformLocation(program, "uPalette");
    aspectLocation = GLES20.glGetUniformLocation(program, "uAspect");
  }

  @Override
  public void onSurfaceChanged(GL10 unused, int width, int height) {
    this.width = width;
    this.height = height;
    GLES20.glViewport(0, 0, width, height);
  }

  @Override
  public void onDrawFrame(GL10 unused) {
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    if (!running || width == 0 || height == 0) return;
    GLES20.glUseProgram(program);
    GLES20.glUniform1f(timeLocation, (System.nanoTime() - startedAt) / 1_000_000_000f);
    GLES20.glUniform1f(speedLocation, options.speed);
    GLES20.glUniform1f(glowLocation, options.glow);
    GLES20.glUniform1f(densityLocation, options.density);
    GLES20.glUniform1f(paletteLocation, options.palette);
    float aspect = (float) width / Math.max(1, height);
    GLES20.glUniform2f(aspectLocation, LightGeometry.aspectScale(width, height), 1f);
    int position = GLES20.glGetAttribLocation(program, "aPosition");
    GLES20.glEnableVertexAttribArray(position);
    vertices.position(0);
    GLES20.glVertexAttribPointer(position, 2, GLES20.GL_FLOAT, false, 0, vertices);
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    GLES20.glDisableVertexAttribArray(position);
  }

  private static int createProgram(String vertexSource, String fragmentSource) {
    int vertex = compileShader(GLES20.GL_VERTEX_SHADER, vertexSource);
    int fragment = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
    int result = GLES20.glCreateProgram();
    GLES20.glAttachShader(result, vertex);
    GLES20.glAttachShader(result, fragment);
    GLES20.glLinkProgram(result);
    int[] status = new int[1];
    GLES20.glGetProgramiv(result, GLES20.GL_LINK_STATUS, status, 0);
    if (status[0] == 0) throw new IllegalStateException(GLES20.glGetProgramInfoLog(result));
    GLES20.glDeleteShader(vertex);
    GLES20.glDeleteShader(fragment);
    return result;
  }

  private static int compileShader(int type, String source) {
    int shader = GLES20.glCreateShader(type);
    GLES20.glShaderSource(shader, source);
    GLES20.glCompileShader(shader);
    int[] status = new int[1];
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
    if (status[0] == 0) throw new IllegalStateException(GLES20.glGetShaderInfoLog(shader));
    return shader;
  }
}
