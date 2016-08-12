package org.ubgamelab.computergraphics.ProjectAkhirGK;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.Cylinder;
import org.ubgamelab.computergraphics.util.CGApplication;
import org.ubgamelab.computergraphics.util.Camera;
import org.ubgamelab.computergraphics.util.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.util.glu.GLU.*;


public class ProjectAkhir_v4 extends CGApplication {

	//Lighting
	private boolean KEY_0_pressed,light0_ON;

	//Toggling
	private boolean cSkybox = false;
	private boolean cPressed;

	private boolean cFly = false;
	private boolean fPressed;

    //default
	private Camera camera;
	private final int[] m_texID = new int[30];
	private Texture m_texture;
	public final static int NO_WRAP = -1;

    //helicopter
    private float angle = 0f;
    private float move = 0f;
    private float movespeed = 0.005f;
    private boolean togmove = true;

	private void drawSkyBox(float width, float height, int tex1, int tex2, int tex3, int tex4, int tex5, int tex6) {
		width = width / 2;

		// front side
		glBindTexture(GL_TEXTURE_2D, m_texID[tex1]);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, (-width+2f));
		glTexCoord2d(1, 0);
		glVertex3f(width, 0, (-width+2f));
		glTexCoord2d(1, 1);
		glVertex3f(width, height+0.01f, (-width+2f));
		glTexCoord2d(0, 1);
		glVertex3f(-width, height+0.01f,(-width+2f));
		glEnd();

		// right side
		glBindTexture(GL_TEXTURE_2D, m_texID[tex2]);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex3f(width, 0, -width);
		glTexCoord2d(1, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, width);
		glTexCoord2d(0, 1);
		glVertex3f(width, height, -width);
		glEnd();

		// back side
		glBindTexture(GL_TEXTURE_2D, m_texID[tex3]);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(1, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(1, 1);
		glVertex3f(-width, height, width);
		glTexCoord2d(0, 1);
		glVertex3f(width, height, width);
		glEnd();

		// left side
		glBindTexture(GL_TEXTURE_2D, m_texID[tex4]);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(1, 0);
		glVertex3f(-width, 0, -width);
		glTexCoord2d(1, 1);
		glVertex3f(-width, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(-width, height, width);
		glEnd();

		// top side
		glBindTexture(GL_TEXTURE_2D, m_texID[tex5]);
		glBegin(GL_QUADS);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(width, height, width);
		glTexCoord2d(0, 0);
		glVertex3f(-width, height, width);
		glTexCoord2d(1, 0);
		glVertex3f(-width, height, -width);
		glEnd();

		// bottom side
		glBindTexture(GL_TEXTURE_2D, m_texID[tex6]);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(width, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(width, width);
		glVertex3f(width, 0, -width);
		glTexCoord2d(0, width);
		glVertex3f(-width, 0, -width);
		glEnd();
	}

	private void drawBuilding(float width, float height, int texID) {
		width = width / 2;

		glBindTexture(GL_TEXTURE_2D, m_texID[texID]);
		glBegin(GL_QUADS);
		// front side
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(1, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, width);
		glTexCoord2d(0, 1);
		glVertex3f(-width, height, width);

		// right side
		glTexCoord2d(0, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(1, 0);
		glVertex3f(width, 0, -width);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(width, height, width);

		// back side
		glTexCoord2d(0, 0);
		glVertex3f(width, 0, -width);
		glTexCoord2d(1, 0);
		glVertex3f(-width, 0, -width);
		glTexCoord2d(1, 1);
		glVertex3f(-width, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(width, height, -width);

		// left side
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, -width);
		glTexCoord2d(1, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(1, 1);
		glVertex3f(-width, height, width);
		glTexCoord2d(0, 1);
		glVertex3f(-width, height, -width);

		// top side
		glVertex3f(-width, height, width);
		glVertex3f(width, height, width);
		glVertex3f(width, height, -width);
		glVertex3f(-width, height, -width);

		glEnd();

	}

	private void drawFullBuildingTex(float width, float height, int texID) {
		width = width / 2;

		glBindTexture(GL_TEXTURE_2D, m_texID[texID]);
		glBegin(GL_QUADS);
		// front side
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(1, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, width);
		glTexCoord2d(0, 1);
		glVertex3f(-width, height, width);

		// right side
		glTexCoord2d(0, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(1, 0);
		glVertex3f(width, 0, -width);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(width, height, width);

		// back side
		glTexCoord2d(0, 0);
		glVertex3f(width, 0, -width);
		glTexCoord2d(1, 0);
		glVertex3f(-width, 0, -width);
		glTexCoord2d(1, 1);
		glVertex3f(-width, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(width, height, -width);

		// left side
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, -width);
		glTexCoord2d(1, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(1, 1);
		glVertex3f(-width, height, width);
		glTexCoord2d(0, 1);
		glVertex3f(-width, height, -width);

		// top side
		glTexCoord2d(0, 0);
		glVertex3f(-width, height, width);
		glTexCoord2d(1, 0);
		glVertex3f(width, height, width);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(-width, height, -width);

		//bottom side
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(width, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(width, width);
		glVertex3f(width, 0, -width);
		glTexCoord2d(0, width);
		glVertex3f(-width, 0, -width);

		glEnd();

	}

	private void drawWall(float width, float height, int texID) {
		width = width / 2;

		glBindTexture(GL_TEXTURE_2D, m_texID[texID]);
		glBegin(GL_QUADS);

		// right side
		glTexCoord2d(0, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(1, 0);
		glVertex3f(width, 0, -width);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(width, height, width);

		// back side
		glTexCoord2d(0, 0);
		glVertex3f(width, 0, -width);
		glTexCoord2d(1, 0);
		glVertex3f(-width, 0, -width);
		glTexCoord2d(1, 1);
		glVertex3f(-width, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(width, height, -width);

		// left side
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, -width);
		glTexCoord2d(1, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(1, 1);
		glVertex3f(-width, height, width);
		glTexCoord2d(0, 1);
		glVertex3f(-width, height, -width);

		// top side
		glVertex3f(-width, height, width);
		glVertex3f(width, height, width);
		glVertex3f(width, height, -width);
		glVertex3f(-width, height, -width);

		glEnd();

	}

	private void drawWallDoor(float width, float height, int texID) {
		width = width / 2;

		glBindTexture(GL_TEXTURE_2D, m_texID[texID]);
		glBegin(GL_QUADS);
		// front side
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(1, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, width);
		glTexCoord2d(0, 1);
		glVertex3f(-width, height, width);

		glEnd();

	}

	private void drawRumah(float width, float height, int texID, int texID2) {
		/**
		 * Dikasih method draw wall sama drawwalldoor
		 */

		drawWall(width, height, texID);
		drawWallDoor(width, height, texID2);

	}

	private void drawTree(float width, float height, int texID) {
		width = width / 2;

		glBindTexture(GL_TEXTURE_2D, m_texID[texID]);
		glBegin(GL_QUADS);
		// front quads
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, 0);
		glTexCoord2d(1, 0);
		glVertex3f(width, 0, 0);
		glTexCoord2d(1, 1);
		glVertex3f(width, height, 0);
		glTexCoord2d(0, 1);
		glVertex3f(-width, height, 0);

		// cross quads
		glTexCoord2d(0, 0);
		glVertex3f(0, 0, width);
		glTexCoord2d(1, 0);
		glVertex3f(0, 0, -width);
		glTexCoord2d(1, 1);
		glVertex3f(0, height, -width);
		glTexCoord2d(0, 1);
		glVertex3f(0, height, width);
		glEnd();
	}

	private void drawPyramid(int texID) {
		glBindTexture(GL_TEXTURE_2D, m_texID[texID]);


		glBegin(GL_TRIANGLES);
		// 2-side of pyramid

		//depan
		glTexCoord2d(0, 0);
		glVertex3f(-1.5f, -1.25f, 1.5f);
		glTexCoord2d(1, 0);
		glVertex3f(1.5f, -1.25f, 1.5f);
		glTexCoord2d(0, 1);
		glVertex3f(0, 1, 0);

		//kanan
		glTexCoord2d(0, 0);
		glVertex3f(1.5f, -1.25f, 1.5f);
		glTexCoord2d(1, 0);
		glVertex3f(1.5f, -1.25f, -1.5f);
		glTexCoord2d(0, 1);
		glVertex3f(0, 1, 0);

		//belakang
		glTexCoord2d(0, 0);
		glVertex3f(1.5f, -1.25f, -1.5f);
		glTexCoord2d(1, 0);
		glVertex3f(-1.5f, -1.25f, -1.5f);
		glTexCoord2d(0, 1);
		glVertex3f(0, 1, 0);

		//kiri
		glTexCoord2d(0, 0);
		glVertex3f(-1.5f, -1.25f, 1.5f);
		glTexCoord2d(1, 0);
		glVertex3f(-1.5f, -1.25f, -1.5f);
		glTexCoord2d(0, 1);
		glVertex3f(0, 1, 0);

		glEnd();
	}

	private void drawKarpet(float width, int textID) {
		width = width / 2;

		// bottom side
		glBindTexture(GL_TEXTURE_2D, m_texID[textID]);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0, width);
		glTexCoord2d(width, 0);
		glVertex3f(width, 0, width);
		glTexCoord2d(width, width);
		glVertex3f(width, 0, -width);
		glTexCoord2d(0, width);
		glVertex3f(-width, 0, -width);
		glEnd();
	}

	private void drawJalan(float width,float height, int meme) {
		width = width / 2;

		// bottom side
//		glBindTexture(GL_TEXTURE_2D, m_texID[meme]);
//		glBegin(GL_QUADS);
//		glTexCoord2d(0, 0);
//		glVertex3f(-width, 0.01f, height);
//		glTexCoord2d(width, 0);
//		glVertex3f(width, 0.01f, height);
//		glTexCoord2d(width, width);
//		glVertex3f(width, 0.01f, -height);
//		glTexCoord2d(0, width);
//		glVertex3f(-width, 0.01f, -height);
//		glEnd();

		glBindTexture(GL_TEXTURE_2D, m_texID[meme]);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex3f(-width, 0.01f, height);
		glTexCoord2d(1, 0);
		glVertex3f(width, 0.01f, height);
		glTexCoord2d(1, 1);
		glVertex3f(width, 0.01f, -height);
		glTexCoord2d(0, 1);
		glVertex3f(-width, 0.01f, -height);
		glEnd();
	}

	private void drawGunung(float baseRadius, float topRadius, float height, int slices, int stacks, int m_texID){
		Cylinder kerucut = new Cylinder();
		glPushMatrix();
		glRotatef(-90, 1, 0, 0);
		kerucut.setNormals(GLU_SMOOTH);
		kerucut.setDrawStyle(GLU_FILL);
		kerucut.setOrientation(GLU_INSIDE);
		kerucut.setTextureFlag(true);
		glBindTexture(GL_TEXTURE_2D, m_texID);
		kerucut.draw(baseRadius, topRadius, height, slices, stacks);
		glPopMatrix();
	}

	private void idrawPerumahan(){
		// Render rumah, pohon dll
		for (int row = 0; row < 2; row++) {
			glPushMatrix();
			for (int col = 0; col < 2; col++) {
				//bikin rumah
				drawRumah(3, 2, 6, 7);

				//jalan
				glPushMatrix();
				glTranslatef(0,0.01f,4);
				drawJalan(8f,1,21);
				glPopMatrix();

				// bikin atap pop
				glPushMatrix();
				glTranslatef(0,3.25f,0);
				drawPyramid(9);
				glPopMatrix();

				// bikin rock pop
				glPushMatrix();
				glTranslatef(0,0.001f,2);
				glTranslatef(-0.5f,0,-0.25f);
				drawKarpet(0.75f,10);
				glPopMatrix();

				//bikin kebun di belakang pop
				glPushMatrix();
				glTranslatef(0,0.001f,-3);
				drawFullBuildingTex(2,0.15f,11);
				glPopMatrix();

				//pohon besar pop
				glPushMatrix();
				glTranslatef(-3, 0, 0);
				drawTree(1, 3,18);
				glPopMatrix();

				//pohon kecil / kanan
				glTranslatef(3, 0, 0);
				drawTree(1, 2,20);
				glTranslatef(5, 0, 0);
			}
			glPopMatrix();

			//jarak kolom antar rumah
			glTranslatef(0, 0, 12);
		}
	}

	private void idrawHelicopter(){
		glTranslatef(move,0,0);
		drawFullBuildingTex(2,1,12);

		glPushMatrix();
		glTranslatef(0,1,0);
		drawFullBuildingTex(0.25f,0.25f,12);
		glPopMatrix();

		glRotatef(angle, 0, 1, 0);
		glPushMatrix();
		glTranslatef(0,1.25f,0);
		drawFullBuildingTex(2,0.25f,12);
		glPopMatrix();
	}

	private void loadTexture(int texID, String path, boolean transparent,
			int filter, int wrap) {
		m_texture = new Texture();
		if (!m_texture.load(path)) {
			System.out.println("Failed to load texture\n");
			return;
		}

		glBindTexture(GL_TEXTURE_2D, texID);

		if (transparent) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, m_texture.getWidth(),
					m_texture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
					m_texture.getImageData());
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, m_texture.getWidth(),
					m_texture.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE,
					m_texture.getImageData());
		}

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);

		if (wrap == -1) {
			return;
		}

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);

	}

	@Override
	public void init() {
		// Initialise camera
		camera = new Camera();
		camera.init();
		camera.setCameraSpeed(0.005f);

		// init vars
		KEY_0_pressed = true;
		light0_ON = true;

		// init for lightning
		// setup lighting
		// Lighting variables

		//-//
		FloatBuffer ambientPointLight = (FloatBuffer) BufferUtils
				.createFloatBuffer(4)
				.put(new float[] { 0.5f, 0.5f, 0.5f, 1.0f }).flip(); // ambient
		// light

		//-//
		FloatBuffer diffusePointLight = (FloatBuffer) BufferUtils
				.createFloatBuffer(4)
				.put(new float[] { 0.5f, 0.5f, 0.5f, 1.0f }).flip(); // diffuse
		// light

		//--//
		FloatBuffer specularPointLight = (FloatBuffer) BufferUtils
				.createFloatBuffer(4)
				.put(new float[] { 1.0f, 1.0f, 1.0f, 1.0f }).flip(); // specular
		// light

		// position

		//-//
		FloatBuffer pointlightPosition = (FloatBuffer) BufferUtils
				.createFloatBuffer(4)
				.put(new float[] { 6.0f, 0.5f, 0.0f, 1.0f }).flip(); // spotlight

		// spotlight
		// downwards

		// Material variables
		FloatBuffer matAmbient = (FloatBuffer) BufferUtils.createFloatBuffer(4)
				.put(new float[] { 1.0f, 1.0f, 1.0f, 1.0f }).flip(); // ambient
		// material

		FloatBuffer matDiff = (FloatBuffer) BufferUtils.createFloatBuffer(4)
				.put(new float[] { 1.0f, 1.0f, 1.0f, 1.0f }).flip(); // diffuse
		// material

		FloatBuffer matSpecular = (FloatBuffer) BufferUtils
				.createFloatBuffer(4)
				.put(new float[] { 1.0f, 1.0f, 1.0f, 1.0f }).flip(); // specular
		// material

		// Now setup LIGHT0
		glLight(GL_LIGHT0, GL_AMBIENT, ambientPointLight); // setup the
		// ambient
		// element
		glLight(GL_LIGHT0, GL_DIFFUSE, diffusePointLight); // the diffuse
		// element
		glLight(GL_LIGHT0, GL_SPECULAR, specularPointLight); // the specular
		// element
		glLight(GL_LIGHT0, GL_POSITION, pointlightPosition); // place the
		// light in the
		// world


		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);

		glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);
		glMaterial(GL_FRONT, GL_AMBIENT, matAmbient);
		glMaterial(GL_FRONT, GL_DIFFUSE, matDiff);
		glMaterialf(GL_FRONT, GL_SHININESS, 10.0f);

		IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(30);
		glGenTextures(textureIDBuffer);
		m_texID[0] = textureIDBuffer.get(0);
		m_texID[1] = textureIDBuffer.get(1);
		m_texID[2] = textureIDBuffer.get(2);
		m_texID[3] = textureIDBuffer.get(3);
		m_texID[4] = textureIDBuffer.get(4);
		m_texID[5] = textureIDBuffer.get(5);
		m_texID[6] = textureIDBuffer.get(6);
		m_texID[7] = textureIDBuffer.get(7);
		m_texID[8] = textureIDBuffer.get(8);
		m_texID[9] = textureIDBuffer.get(9);
		m_texID[10] = textureIDBuffer.get(10);
		m_texID[11] = textureIDBuffer.get(11);
		m_texID[12] = textureIDBuffer.get(12);
		m_texID[13] = textureIDBuffer.get(13);
		m_texID[14] = textureIDBuffer.get(14);
		m_texID[15] = textureIDBuffer.get(15);
		m_texID[16] = textureIDBuffer.get(16);
		m_texID[17] = textureIDBuffer.get(17);
		m_texID[18] = textureIDBuffer.get(18);
		m_texID[19] = textureIDBuffer.get(19);
		m_texID[20] = textureIDBuffer.get(20);
		m_texID[21] = textureIDBuffer.get(21);
		m_texID[22] = textureIDBuffer.get(22);
		m_texID[23] = textureIDBuffer.get(23);
		m_texID[24] = textureIDBuffer.get(24);
		m_texID[25] = textureIDBuffer.get(25);
		m_texID[26] = textureIDBuffer.get(26);

		// Texture SkyBox Night
		loadTexture(m_texID[0], "Skybox/front2.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);
		loadTexture(m_texID[1], "Skybox/right.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);
		loadTexture(m_texID[2], "Skybox/back.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);
		loadTexture(m_texID[3], "Skybox/left.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);
		loadTexture(m_texID[4], "Skybox/up.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);
		loadTexture(m_texID[5], "Data/grass.png", false, GL_LINEAR, GL_REPEAT);

		// Texture Skybox Sunnyyyy~~

		loadTexture(m_texID[13], "Data/0.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);
		loadTexture(m_texID[14], "Data/90.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);
		loadTexture(m_texID[15], "Data/180.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);
		loadTexture(m_texID[16], "Data/270.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);
		loadTexture(m_texID[17], "Data/up.png", false, GL_LINEAR,
				GL_CLAMP_TO_EDGE);

		//wallpaper rumah
		loadTexture(m_texID[6], "Data/wall2.jpg", false, GL_LINEAR,
				NO_WRAP);

		//wallpaper rumah ada pintu
		loadTexture(m_texID[7], "Data/walldesa.png", true, GL_LINEAR,
				NO_WRAP);

		//Texture Pohon
		loadTexture(m_texID[8], "Data/tree.png", true, GL_LINEAR, NO_WRAP);

		//Texture Atap
		loadTexture(m_texID[9], "Data/atap.jpg", false, GL_LINEAR, NO_WRAP);

		//Texture Rock
		loadTexture(m_texID[10], "Data/rock.jpg", false, GL_LINEAR, NO_WRAP);

		//Texture Sawah
		loadTexture(m_texID[11], "Data/sawah.jpg", false, GL_LINEAR, NO_WRAP);

        //Texture Pesawat
        loadTexture(m_texID[12], "Data/heli1.jpg", false, GL_LINEAR, NO_WRAP);

		//Texture Tree
		loadTexture(m_texID[18], "Pohon/Bleech.png", true, GL_LINEAR, NO_WRAP);
		loadTexture(m_texID[19], "Pohon/oak.png", true, GL_LINEAR, NO_WRAP);
		loadTexture(m_texID[20], "Pohon/tree.png", true, GL_LINEAR, NO_WRAP);
		loadTexture(m_texID[22], "Pohon/berry.png", true, GL_LINEAR, NO_WRAP);

		//Texture Pesawat
		loadTexture(m_texID[21], "Data/jalan.jpg", false, GL_LINEAR, GL_CLAMP_TO_EDGE);


	}

	@Override
	public void update(int delta) {
        //helicoper update
        angle += delta * 1.5f;

        if(move>=50) {
            togmove=true;
        }
        else if  (move<=-50){
            togmove=false;
        }
        if (togmove){
            move -= delta * movespeed;
        }
        else{
            move += delta * movespeed;
        }


        // Update camera
		camera.update();

		// Toggling ^^
		togglecSkybox();
		toggleKey();
		toggleFly();


	}

	public void toggleKey() {
		// Toggle light 0,1,2
		if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
			if (!KEY_0_pressed) {
				light0_ON = !light0_ON;
				KEY_0_pressed = true;
			}
		} else {
			KEY_0_pressed = false;
		}
	}

	private void toggleLighting(int lightID, boolean on) {
		if (on) {
			glEnable(lightID);
		} else {
			glDisable(lightID);
		}
	}

	private void togglecSkybox() {

		if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
			if (!cPressed) {
				cSkybox = !cSkybox;
				cPressed = true;
			}
		} else {
			cPressed = false;
		}
	}

	private void toggleFly() {

		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			if (!fPressed) {
				cFly = !cFly;
				fPressed = true;
			}
		} else {
			fPressed = false;
		}
	}


	@Override
	public void render() {
		// Setting up default viewport
		glViewport(0, 0, width, height);

		// Set perspective projection
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(45.0f, (width) / (height), 1.0f, 700.0f);

		// Transformation
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		//Lihat dari sisi mana
		if (cFly) {
			// tampilan
			gluLookAt(move+10, 15, 0 ,move, 10, 0, 0, 1, 0);
		} else {
			//...
		}



		// Clear color buffer to black
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// Set background color to black
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Positioning camera
		if (cFly) {
			//.....
		} else {
			camera.render();
		}

		// Enable lighting
		glEnable(GL_LIGHTING);

		// toggle lighting
		toggleLighting(GL_LIGHT0, light0_ON);

		// Enable Depth Testing for correct z-ordering
		glEnable(GL_DEPTH_TEST);
		// Enable alpha test for correct transparency ordering
		glEnable(GL_ALPHA_TEST);
		// Display image pixel which transparency greater than 0.1
		glAlphaFunc(GL_GREATER, 0.1f);

		// Enable transparency of image with blending
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// Enable texturing
		glEnable(GL_TEXTURE_2D);


		if (cSkybox) {
			// Render sky box malam
			drawSkyBox(500, 100,0,1,2,3,4,5);
		} else {
			drawSkyBox(500, 100,13,14,15,16,17,5);
		}

		//main posisi camera nantinya
		glTranslatef(-0, 0, -0);

        //helicopter
        glPushMatrix();
        glTranslatef(0,10,0);
		idrawHelicopter();
        glPopMatrix();

		//render rumah
		idrawPerumahan();

		//render rumah 2
		glPushMatrix();
		glTranslatef(0,0,1);
		idrawPerumahan();
		glPopMatrix();



		//pindah ke tengah - no pop
		glTranslatef(12.5f,0.01f,4);


		//render hutan
		glPushMatrix();
		glTranslatef(-1,0,-42);
			glPushMatrix();
			for (int row = 0; row < 5; row++) {
				glPushMatrix();
				for (int col = 0; col <8; col++) {
					drawTree(1, 3, 22);
					glTranslatef(-1, 0, 0);
					drawTree(1, 3, 22);
					glTranslatef(-1, 0, 0);
				}
				glPopMatrix();
				glTranslatef(0,0,2);
			}
			glPopMatrix();

		glTranslatef(17,0,0);
			glPushMatrix();
			for (int row = 0; row < 9; row++) {
				glPushMatrix();
				for (int col = 0; col <8; col++) {
					drawTree(1, 3, 22);
					glTranslatef(-1, 0, 0);
					drawTree(1, 3, 22);
					glTranslatef(-1, 0, 0);
				}
				glPopMatrix();
				glTranslatef(0,0,2);
			}
			glPopMatrix();
		glPopMatrix();

		//Render Gunung dan Jalan
		glPushMatrix();
			glTranslatef(0,0.01f,-75);

			//Render Bukit
			glPushMatrix();
			glTranslatef(-9.5f, 0, 0);
				glPushMatrix();
				for (int col = 0; col < 4; col++ ){
					glPushMatrix();
					drawGunung(30, 1, 10, 35, 1,m_texID[5]);
					glPopMatrix();
					glTranslatef(31, 0.01f, 0);
				}
				glPopMatrix();
			glPopMatrix();

			//Render Jalan
			for (int row = 0; row < 40; row++) {
				glPushMatrix();
				drawJalan(1,3,21);
				glPopMatrix();
				glTranslatef(0,0,6);
			}
		glPopMatrix();

		//--------------------------------//

		//render hutan
		glPushMatrix();
		glTranslatef(-20,0,-40);
		for (int row = 0; row < 20; row++) {
			glPushMatrix();
			for (int col = 0; col < 5; col++) {
				drawTree(1, 3, 19);
				glTranslatef(-1, 0, 0);
				drawTree(1, 3, 19);
				glTranslatef(-1, 0, 0);
			}
			glPopMatrix();
			glTranslatef(0,0,2);
		}
		glPopMatrix();

		//render hutan kanan
		glPushMatrix();
		glTranslatef(30,0,-40);
		for (int row = 0; row < 20; row++) {
			glPushMatrix();
			for (int col = 0; col < 5; col++) {
				drawTree(1, 3, 19);
				glTranslatef(-1, 0, 0);
				drawTree(1, 3, 19);
				glTranslatef(-1, 0, 0);
			}
			glPopMatrix();
			glTranslatef(0,0,2);
		}
		glPopMatrix();

		glPushMatrix();
		glTranslatef(4.5f,0,-20);
		//render rumah
		idrawPerumahan();
		glPopMatrix();

		//tengah
		glPushMatrix();
		glTranslatef(4.5f,0,5);
		//render rumah
		idrawPerumahan();
		glPopMatrix();



		glDisable(GL_DEPTH_TEST);
		glDisable(GL_ALPHA_TEST);
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);

		renderText();

	}

	private void renderText() {
		drawString(0, height - getDefaultFont().getLineHeight(),
				"Modern Village");
		drawString(0, height - 2 * getDefaultFont().getLineHeight(),
				"Press '0' to change LIGHTNING");
		drawString(0, height - 3 * getDefaultFont().getLineHeight(),
				"Press 'C' to change WEATHER");
		drawString(0, height - 4 * getDefaultFont().getLineHeight(),
				"Press 'F' to Follow Helicopter");
	}

	@Override
	public void deinit() {
		glDeleteTextures(m_texID[0]);
		glDeleteTextures(m_texID[1]);
		glDeleteTextures(m_texID[2]);
		glDeleteTextures(m_texID[3]);
		glDeleteTextures(m_texID[4]);
		glDeleteTextures(m_texID[5]);
		glDeleteTextures(m_texID[6]);
		glDeleteTextures(m_texID[7]);
		glDeleteTextures(m_texID[8]);
		glDeleteTextures(m_texID[9]);
		glDeleteTextures(m_texID[10]);
		glDeleteTextures(m_texID[11]);
        glDeleteTextures(m_texID[12]);
		glDeleteTextures(m_texID[13]);
		glDeleteTextures(m_texID[14]);
		glDeleteTextures(m_texID[15]);
		glDeleteTextures(m_texID[16]);
		glDeleteTextures(m_texID[17]);
		glDeleteTextures(m_texID[18]);
		glDeleteTextures(m_texID[19]);
		glDeleteTextures(m_texID[20]);
		glDeleteTextures(m_texID[21]);
		glDeleteTextures(m_texID[22]);

	}

	public static void main(String[] args) {
		ProjectAkhir_v4 app = new ProjectAkhir_v4();
		app.start(1366, 768,true, false, true,
				"Modern Village");
	}

}
