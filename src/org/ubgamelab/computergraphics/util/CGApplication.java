/*
 * Copyright (c) 2014, Eriq Muhammad Adams J.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Brawijaya nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY ERIQ MUHAMMAD ADAMS J. ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <copyright holder> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.ubgamelab.computergraphics.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 * This Class is Small Utility, Supplement Code for Computer Graphic Course at
 * <a href="http://www.ub.ac.id">University of Brawijaya</a>, <br/>
 * Credits goes to Brian Matzon and Ninja Cave, <a
 * href="http://www.lwjgl.org">LWJGL</a>.
 * 
 * @author <a href="mailto:eriq.adams@ub.ac.id">Eriq Muhammad Adams J.</a>
 * 
 */
public abstract class CGApplication {

	/** Time at Last Frame */
	protected long lastFrame;

	/** Frames Per Second */
	protected int fps;

	/** Last FPS Time */
	protected long last;

	/** Window Title */
	protected String windowTitle = "";

	/** Window Width */
	protected int width;

	/** Window Height */
	protected int height;

	/** Full Screen Display */
	protected boolean fullscreen;

	/** Vertical Synchronization */
	protected boolean vsync;

	/** Default Font */
	private TrueTypeFont defaultFont;

	/** Font Color */
	private Color fontColor;

	/** Default Font Size */
	public final int DEFAULT_FONT_SIZE = 14;

	/** Show FPS */
	protected boolean showFPS;

	/** old FPS */
	protected int oldfps;

	protected int activeFontTextureId;

	/**
	 * Start and Running Application
	 * 
	 * @param width
	 *            Resolution Width
	 * @param height
	 *            Resolution Height
	 * @param fullscreen
	 *            Displaying in Full Screen or Windowed
	 * @param vsync
	 *            Activate Vertical Synchronization or not
	 * @param windowTitle
	 *            Title of Application Window
	 * */
	public void start(int width, int height, boolean fullscreen, boolean vsync,
			boolean showFPS, String windowTitle) {

		this.windowTitle = windowTitle;
		this.height = height;
		this.width = width;
		this.fullscreen = fullscreen;
		this.vsync = vsync;
		this.showFPS = showFPS;

		try {
			// Initialization
			createWindow();
			createDefaultFont();
			init();

			// Get Delta Time for 1st time
			getDelta();
			// Get Last Time
			last = getTime();

			while (!Display.isCloseRequested()
					&& !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				// Get Delta
				int delta = getDelta();

				// Update
				update(delta);
				updateFPS();

				// Rendering
				render();
				drawFPS();
				Display.update();
			}

			// Deinitialization
			deinit();
			Display.destroy();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	/**
	 * Create LWJGL Display
	 * 
	 * @throws LWJGLException
	 */
	private void createWindow() throws LWJGLException {
		// Create Display
		Display.setTitle(windowTitle);
		Display.setFullscreen(fullscreen);
		Display.setVSyncEnabled(vsync);
		DisplayMode displayMode = null;
		DisplayMode d[] = Display.getAvailableDisplayModes();
		for (int i = 0; i < d.length; i++) {
			if (d[i].getWidth() == width && d[i].getHeight() == height
					&& d[i].getBitsPerPixel() == 32) {
				displayMode = d[i];
				break;
			}
		}

		Display.setDisplayMode(displayMode);
		Display.create();
	}

	/**
	 * Set Orthographic Projection
	 */
	private void setOrthoMode() {
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		gluOrtho2D(0, width, height, 0);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
	}

	/**
	 * Unset Orthographic Projection
	 */
	private void unsetOrthoMode() {
		glPopMatrix();
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
	}

	/**
	 * Get Default Font
	 * 
	 * @return
	 */
	public TrueTypeFont getDefaultFont() {
		return defaultFont;
	}

	/**
	 * Set Default Font
	 * 
	 * @param defaultFont
	 *            true type font
	 */
	public void setDefaultFont(TrueTypeFont defaultFont) {
		this.defaultFont = defaultFont;
	}

	/**
	 * Create Default Font
	 */
	private void createDefaultFont() {
		Font awtFont = new Font("Lucida Console", Font.BOLD, DEFAULT_FONT_SIZE);
		defaultFont = new TrueTypeFont(awtFont, true);
		fontColor = Color.white;
		activeFontTextureId = glGetInteger(GL_TEXTURE_BINDING_2D);
	}

	/**
	 * Draw FPS
	 */
	private void drawFPS() {
		if (showFPS) {
			drawString(0, 0, "FPS : " + oldfps);
		}
	}

	/**
	 * Set Font color in red, green, blue values
	 * 
	 * @param r
	 *            Red value
	 * @param g
	 *            Green value
	 * @param b
	 *            Blue value
	 */
	public void setFontColor(float r, float g, float b) {
		fontColor = new Color(r, g, b);
	}

	/**
	 * Draw string s at x,y. (0,0) is at bottom left corner of the screen
	 * 
	 * @param x
	 *            X position
	 * @param y
	 *            Y position
	 * @param s
	 *            String to draw
	 */
	public void drawString(float x, float y, String s) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		setOrthoMode();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, activeFontTextureId);
		glPushMatrix();

		y = height - y - defaultFont.getLineHeight();
		defaultFont.drawString(x, y, s, fontColor);
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		unsetOrthoMode();
	}

	/**
	 * Calculate how many milliseconds have passed since last frame.
	 * 
	 * @return milliseconds passed since last frame
	 */
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - last > 1000) {
			oldfps = fps;
			fps = 0;
			last += 1000;
		}
		fps++;
	}

	/**
	 * 
	 * @return
	 */
	public int getFontSize() {
		return getDefaultFont().getLineHeight();
	}

	/** Initialization Procedure */
	public abstract void init();

	/**
	 * Update Logic Procedure
	 * 
	 * @param delta
	 *            delta time between two frames
	 * */
	public abstract void update(int delta);

	/** Rendering Procedure */
	public abstract void render();

	/** Deinitialization Procedure */
	public abstract void deinit();
}
