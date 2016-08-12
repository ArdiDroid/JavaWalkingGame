package org.ubgamelab.computergraphics.util;

import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Camera {
	// gluLookAt params
	private float viewCamX, viewCamY, viewCamZ, upCamX, upCamY, upCamZ,
			posCamX, posCamY, posCamZ, CAMERA_SPEED;


	private void moveCamera(float speed) {
		float x = viewCamX - posCamX;
		float z = viewCamZ - posCamZ;
		// forward positive cameraspeed and backward negative -cameraspeed.
		posCamX = posCamX + x * speed;
		posCamZ = posCamZ + z * speed;
		viewCamX = viewCamX + x * speed;
		viewCamZ = viewCamZ + z * speed;
	}

	private void rotateCamera(float speed) {
		float x = viewCamX - posCamX;
		float z = viewCamZ - posCamZ;
		viewCamZ = (float) (posCamZ + Math.sin(speed) * x + Math.cos(speed) * z);
		viewCamX = (float) (posCamX + Math.cos(speed) * x - Math.sin(speed) * z);
	}

	private void strafeCamera(float speed) {
		float x = viewCamX - posCamX;
		float z = viewCamZ - posCamZ;
		float orthoX = -z;
		float orthoZ = x;

		// left positive cameraspeed and right negative -cameraspeed.
		posCamX = posCamX + orthoX * speed;
		posCamZ = posCamZ + orthoZ * speed;
		viewCamX = viewCamX + orthoX * speed;
		viewCamZ = viewCamZ + orthoZ * speed;
	}

	private void mouseMove() {
		int width, height, mouseX, mouseY;
		// Get window size
		width = Display.getWidth();
		height = Display.getHeight();

		int midX = width >> 1;
		int midY = height >> 1;
		float angleY = 0.0f;
		float angleZ = 0.0f;

		// Get mouse position
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();

		if ((mouseX == midX) && (mouseY == midY)) {
			return;
		}

		// Set mouse position
		Mouse.setCursorPosition(midX, midY);

		// Get the direction from the mouse cursor, set a resonable maneuvering
		// speed
		angleY = (float) ((midX - mouseX)) / 1000;
		angleZ = (float) ((midY - mouseY)) / 1000;

		// The higher the value is the faster the camera looks around.
		viewCamY += angleZ * 2;

		// limit the rotation around the x-axis
		if ((viewCamY - posCamY) > 8) {
			viewCamY = posCamY + 8;
		}

		if ((viewCamY - posCamY) < -8) {
			viewCamY = posCamY - 8;
		}

		rotateCamera(-angleY);
	}

	public void init() {
		posCamX = 0.0f;
		posCamY = 1.0f;
		posCamZ = 8.0f;
		viewCamX = 0.0f;
		viewCamY = 1.0f;
		viewCamZ = 0.0f;
		upCamX = 0.0f;
		upCamY = 1.0f;
		upCamZ = 0.0f;
		CAMERA_SPEED = 0.01f;
		Mouse.setGrabbed(true);
	}

	public void setCameraSpeed(float speed) {
		CAMERA_SPEED = speed;
	}

	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveCamera(CAMERA_SPEED);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveCamera(-CAMERA_SPEED);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			strafeCamera(-CAMERA_SPEED);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			strafeCamera(CAMERA_SPEED);
		}

		mouseMove();
	}

	public void render() {
		gluLookAt(posCamX, posCamY, posCamZ, viewCamX, viewCamY, viewCamZ,
				upCamX, upCamY, upCamZ);
	}
}
