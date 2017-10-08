/*
 * Copyright (c) 2017 Razeware LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish, 
 * distribute, sublicense, create a derivative work, and/or sell copies of the 
 * Software in any work that is designed, intended, or marketed for pedagogical or 
 * instructional purposes related to programming, coding, application development, 
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works, 
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dauntless.starkx.satori.lib;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

import com.dauntless.starkx.satori.ui.camera.GraphicOverlay;


public class FaceGraphic extends GraphicOverlay.Graphic
{
    private Renderer renderer;
    private static final String TAG = "FaceGraphic";

    private boolean mIsFrontFacing;

    // This variable may be written to by one of many threads. By declaring it as volatile,
    // we guarantee that when we read its contents, we're reading the most recent "write"
    // by any thread.
    private volatile FaceData mFaceData;

    private EyePhysics mLeftPhysics = new EyePhysics();
    private EyePhysics mRightPhysics = new EyePhysics();


    FaceGraphic(GraphicOverlay overlay, boolean isFrontFacing, Renderer renderer) {
        super(overlay);
        mIsFrontFacing = isFrontFacing;
        this.renderer = renderer;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Update the face instance based on detection from the most recent frame.
     */
    void update(FaceData faceData) {
        mFaceData = faceData;
        postInvalidate(); // Trigger a redraw of the graphic (i.e. cause draw() to be called).
    }

    @Override
    public void draw(Canvas canvas) {
        // Confirm that the face data is still available
        // before using it.
        FaceData faceData = mFaceData;
        if (faceData == null) {
            return;
        }
        PointF detectPosition = faceData.getPosition();
        PointF detectLeftEyePosition = faceData.getLeftEyePosition();
        PointF detectRightEyePosition = faceData.getRightEyePosition();
        PointF detectNoseBasePosition = faceData.getNoseBasePosition();
        PointF detectMouthLeftPosition = faceData.getMouthLeftPosition();
        PointF detectMouthBottomPosition = faceData.getMouthBottomPosition();
        PointF detectMouthRightPosition = faceData.getMouthRightPosition();
        {
            if (( detectPosition == null ) ||
                    ( detectLeftEyePosition == null ) ||
                    ( detectRightEyePosition == null ) ||
                    ( detectNoseBasePosition == null ) ||
                    ( detectMouthLeftPosition == null ) ||
                    ( detectMouthBottomPosition == null ) ||
                    ( detectMouthRightPosition == null ))
            {
                return;
            }
        }

        // If we've made it this far, it means that the face data *is* available.
        // It's time to translate camera coordinates to view coordinates.

        // Face position, dimensions, and angle
        PointF position = new PointF(translateX(detectPosition.x),
                translateY(detectPosition.y));
        float width = scaleX(faceData.getWidth());
        float height = scaleY(faceData.getHeight());

        // Eye coordinates
        PointF leftEyePosition = new PointF(translateX(detectLeftEyePosition.x),
                translateY(detectLeftEyePosition.y));
        PointF rightEyePosition = new PointF(translateX(detectRightEyePosition.x),
                translateY(detectRightEyePosition.y));

        // Eye state
        boolean leftEyeOpen = faceData.isLeftEyeOpen();
        boolean rightEyeOpen = faceData.isRightEyeOpen();

        // Nose coordinates
        PointF noseBasePosition = new PointF(translateX(detectNoseBasePosition.x),
                translateY(detectNoseBasePosition.y));

        // Mouth coordinates
        PointF mouthLeftPosition = new PointF(translateX(detectMouthLeftPosition.x), translateY(detectMouthLeftPosition.y));
        PointF mouthRightPosition = new PointF(translateX(detectMouthRightPosition.x), translateY(detectMouthRightPosition.y));
        PointF mouthBottomPosition = new PointF(translateX(detectMouthBottomPosition.x), translateY(detectMouthBottomPosition.y));

        // Smile state
        boolean smiling = faceData.isSmiling();

        // Head tilt
        float eulerY = faceData.getEulerY();
        float eulerZ = faceData.getEulerZ();

        // Calculate the distance between the eyes using Pythagoras' formula,
        // and we'll use that distance to set the size of the eyes and irises.
        final float EYE_RADIUS_PROPORTION = 0.45f;
        final float IRIS_RADIUS_PROPORTION = EYE_RADIUS_PROPORTION / 2.0f;
        float distance = (float) Math.sqrt(( rightEyePosition.x - leftEyePosition.x ) * ( rightEyePosition.x - leftEyePosition.x ) +
                ( rightEyePosition.y - leftEyePosition.y ) * ( rightEyePosition.y - leftEyePosition.y ));
        float eyeRadius = EYE_RADIUS_PROPORTION * distance;
        float irisRadius = IRIS_RADIUS_PROPORTION * distance;

        if (renderer != null)
        {
            PointF leftIrisPosition = mLeftPhysics.nextIrisPosition(leftEyePosition, eyeRadius, irisRadius);
            renderer.drawEye(canvas, leftEyePosition, eyeRadius, leftIrisPosition, irisRadius, leftEyeOpen, smiling);

            PointF rightIrisPosition = mRightPhysics.nextIrisPosition(rightEyePosition, eyeRadius, irisRadius);
            renderer.drawEye(canvas, rightEyePosition, eyeRadius, rightIrisPosition, irisRadius, rightEyeOpen, smiling);

            renderer.drawNose(canvas, noseBasePosition, leftEyePosition, rightEyePosition, width);
            renderer.drawNoseFace(canvas, noseBasePosition, leftEyePosition, rightEyePosition, width , height);

            renderer.drawMustache(canvas, noseBasePosition, mouthLeftPosition, mouthRightPosition);

            // Draw the hat only if the subject's head is titled at a
            // sufficiently jaunty angle.
            final float HEAD_TILT_HAT_THRESHOLD = 2.0f;
            if (Math.abs(eulerZ) > HEAD_TILT_HAT_THRESHOLD) {
		        Log.d("========Graphics", "Drawn " + Math.abs(eulerZ));
		        renderer.drawHat(canvas, position, width, height, noseBasePosition);
	        }
	        else {
		        Log.d("========Graphics", "Not Drawn " + Math.abs(eulerZ));
	        }
        }
    }

    public static abstract class Renderer
    {
        public void drawEye(Canvas canvas, PointF eyePosition, float eyeRadius, PointF irisPosition, float irisRadius, boolean eyeOpen, boolean smiling)
        {
        }

        public void drawNose(Canvas canvas, PointF noseBasePosition, PointF leftEyePosition, PointF rightEyePosition, float faceWidth)
        {
        }
        public void drawNoseFace(Canvas canvas, PointF noseBasePosition, PointF leftEyePosition, PointF rightEyePosition, float faceWidth , float faceHeight)
        {
        }

        public void drawMustache(Canvas canvas, PointF noseBasePosition, PointF mouthLeftPosition, PointF mouthRightPosition)
        {
        }

        public void drawHat(Canvas canvas, PointF facePosition, float faceWidth, float faceHeight, PointF noseBasePosition)
        {
        }
    }
}