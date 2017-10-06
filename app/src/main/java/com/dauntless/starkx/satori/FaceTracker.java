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
package com.dauntless.starkx.satori;

import android.content.Context;
import android.graphics.PointF;

import com.dauntless.starkx.satori.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.HashMap;
import java.util.Map;

class FaceTracker extends Tracker<Face>
{

	private static final String TAG = "FaceTracker";

	private GraphicOverlay mOverlay;
	private FaceGraphic mFaceGraphic;
	private Context mContext;
	private boolean mIsFrontFacing;
	private FaceData mFaceData;

	// Subjects may move too quickly to for the system to detect their detect features,
	// or they may move so their features are out of the tracker's detection range.
	// This map keeps track of previously detected facial landmarks so that we can approximate
	// their locations when they momentarily "disappear".
	private Map<Integer, PointF> mPreviousLandmarkPositions = new HashMap<>();

	// As with facial landmarks, we keep track of the eye’s previous open/closed states
	// so that we can use them during those moments when they momentarily go undetected.
	private boolean mPreviousIsLeftEyeOpen = true;
	private boolean mPreviousIsRightEyeOpen = true;


	FaceTracker(GraphicOverlay overlay, Context context, boolean isFrontFacing)
	{
		mOverlay = overlay;
		mContext = context;
		mIsFrontFacing = isFrontFacing;
		mFaceData = new FaceData();
	}

	// Face detection event handlers
	// =============================

	/**
	 * Called when a new face is detected.
	 * We'll create a new graphic overlay whenever this happens.
	 */
	@Override
	public void onNewItem(int id, Face face)
	{
		mFaceGraphic = new FaceGraphic(mOverlay, mContext, mIsFrontFacing);
	}

	/**
	 * As detected faces are tracked over time, this method is called regularly to update
	 * their information. We'll collect the updated face information and use it
	 * to update the graphic overlay.
	 */
	@Override
	public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face)
	{
		mOverlay.add(mFaceGraphic);
		updatePreviousLandmarkPositions(face);

		// Get face dimensions.
		mFaceData.setPosition(face.getPosition());
		mFaceData.setWidth(face.getWidth());
		mFaceData.setHeight(face.getHeight());

		// Get head angles.
		mFaceData.setEulerY(face.getEulerY());
		mFaceData.setEulerZ(face.getEulerZ());

		// Get the positions of facial landmarks.
		mFaceData.setLeftEyePosition(getLandmarkPosition(face, Landmark.LEFT_EYE));
		mFaceData.setRightEyePosition(getLandmarkPosition(face, Landmark.RIGHT_EYE));
		mFaceData.setMouthBottomPosition(getLandmarkPosition(face, Landmark.LEFT_CHEEK));
		mFaceData.setMouthBottomPosition(getLandmarkPosition(face, Landmark.RIGHT_CHEEK));
		mFaceData.setNoseBasePosition(getLandmarkPosition(face, Landmark.NOSE_BASE));
		mFaceData.setMouthBottomPosition(getLandmarkPosition(face, Landmark.LEFT_EAR));
		mFaceData.setMouthBottomPosition(getLandmarkPosition(face, Landmark.LEFT_EAR_TIP));
		mFaceData.setMouthBottomPosition(getLandmarkPosition(face, Landmark.RIGHT_EAR));
		mFaceData.setMouthBottomPosition(getLandmarkPosition(face, Landmark.RIGHT_EAR_TIP));
		mFaceData.setMouthLeftPosition(getLandmarkPosition(face, Landmark.LEFT_MOUTH));
		mFaceData.setMouthBottomPosition(getLandmarkPosition(face, Landmark.BOTTOM_MOUTH));
		mFaceData.setMouthRightPosition(getLandmarkPosition(face, Landmark.RIGHT_MOUTH));

		// Determine if eyes are open.
		final float EYE_CLOSED_THRESHOLD = 0.4f;
		float leftOpenScore = face.getIsLeftEyeOpenProbability();
		if (leftOpenScore == Face.UNCOMPUTED_PROBABILITY)
		{
			mFaceData.setLeftEyeOpen(mPreviousIsLeftEyeOpen);
		}
		else
		{
			mFaceData.setLeftEyeOpen(leftOpenScore > EYE_CLOSED_THRESHOLD);
			mPreviousIsLeftEyeOpen = mFaceData.isLeftEyeOpen();
		}
		float rightOpenScore = face.getIsRightEyeOpenProbability();
		if (rightOpenScore == Face.UNCOMPUTED_PROBABILITY)
		{
			mFaceData.setRightEyeOpen(mPreviousIsRightEyeOpen);
		}
		else
		{
			mFaceData.setRightEyeOpen(rightOpenScore > EYE_CLOSED_THRESHOLD);
			mPreviousIsRightEyeOpen = mFaceData.isRightEyeOpen();
		}

		// See if there's a smile!
		// Determine if person is smiling.
		final float SMILING_THRESHOLD = 0.8f;
		mFaceData.setSmiling(face.getIsSmilingProbability() > SMILING_THRESHOLD);

		mFaceGraphic.update(mFaceData);
	}

	/**
	 * Called when a face momentarily goes undetected.
	 */
	@Override
	public void onMissing(FaceDetector.Detections<Face> detectionResults)
	{
		mOverlay.remove(mFaceGraphic);
	}

	/**
	 * Called when a face is assumed to be out of camera view for good.
	 */
	@Override
	public void onDone()
	{
		mOverlay.remove(mFaceGraphic);
	}

	// Facial landmark utility methods
	// ===============================

	/**
	 * Given a face and a facial landmark position,
	 * return the coordinates of the landmark if known,
	 * or approximated coordinates (based on prior data) if not.
	 */
	private PointF getLandmarkPosition(Face face, int landmarkId)
	{
		for (Landmark landmark : face.getLandmarks())
		{
			if (landmark.getType() == landmarkId)
			{
				return landmark.getPosition();
			}
		}

		PointF landmarkPosition = mPreviousLandmarkPositions.get(landmarkId);
		if (landmarkPosition == null)
		{
			return null;
		}

		float x = face.getPosition().x + ( landmarkPosition.x * face.getWidth() );
		float y = face.getPosition().y + ( landmarkPosition.y * face.getHeight() );
		return new PointF(x, y);
	}

	private void updatePreviousLandmarkPositions(Face face)
	{
		for (Landmark landmark : face.getLandmarks())
		{
			PointF position = landmark.getPosition();
			float xProp = ( position.x - face.getPosition().x ) / face.getWidth();
			float yProp = ( position.y - face.getPosition().y ) / face.getHeight();
			mPreviousLandmarkPositions.put(landmark.getType(), new PointF(xProp, yProp));
		}
	}

}