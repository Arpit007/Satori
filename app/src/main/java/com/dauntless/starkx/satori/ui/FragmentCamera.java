package com.dauntless.starkx.satori.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dauntless.starkx.satori.R;
import com.dauntless.starkx.satori.lib.FaceGraphic;
import com.dauntless.starkx.satori.lib.FaceTracker;
import com.dauntless.starkx.satori.ui.camera.CameraSourcePreview;
import com.dauntless.starkx.satori.ui.camera.GraphicOverlay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by sonu on 6/10/17.
 */

public class FragmentCamera extends Fragment {
	private static final String TAG = "FaceActivity";
	private static final int RC_HANDLE_GMS = 9001;
	private static final int RC_HANDLE_CAMERA_PERM = 255;
	private CameraSource mCameraSource = null;
	private CameraSourcePreview mPreview;
	private GraphicOverlay mGraphicOverlay;
	private boolean mIsFrontFacing = true;
	private FloatingActionButton floatingActionButton;
	private Integer REQUEST_CODE = 10;
	public static final int RESULT_CODE_EYE = 10;
	public static final int RESULT_CODE_NOSE = 11;
	public static final int RESULT_CODE_MUSTACHE = 12;
	public static final int RESULT_CODE_HEAD = 13;
	private Bitmap eyeBitmap, noseBitmap, mustacheBitmap, headBitmap;


	public FragmentCamera() {
	}

	public static FragmentCamera newInstance() {
		FragmentCamera fragment = new FragmentCamera();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

		mPreview = (CameraSourcePreview) rootView.findViewById(R.id.preview);
		mGraphicOverlay = (GraphicOverlay) rootView.findViewById(R.id.faceOverlay);
		floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);

		if (savedInstanceState != null) {
			mIsFrontFacing = savedInstanceState.getBoolean("IsFrontFacing");
		}
		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(), FilterPicker.class), REQUEST_CODE);
			}
		});

		view.findViewById(R.id.flipButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mIsFrontFacing = !mIsFrontFacing;

				if (mCameraSource != null) {
					mCameraSource.release();
					mCameraSource = null;
				}

				createCameraSource();
				startCameraSource();
			}
		});

		if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
			createCameraSource();
		}
		else {
			requestCameraPermission();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume called.");

		startCameraSource();
	}

	@Override
	public void onPause() {
		super.onPause();
		mPreview.stop();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putBoolean("IsFrontFacing", mIsFrontFacing);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mCameraSource != null) {
			mCameraSource.release();
		}
	}

	private void requestCameraPermission() {
		Log.w(TAG, "Camera permission not acquired. Requesting permission.");

		final String[] permissions = new String[]{ Manifest.permission.CAMERA };
		if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
				Manifest.permission.CAMERA)) {
			ActivityCompat.requestPermissions(getActivity(), permissions, RC_HANDLE_CAMERA_PERM);
			return;
		}

		final Activity thisActivity = getActivity();
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityCompat.requestPermissions(thisActivity, permissions, RC_HANDLE_CAMERA_PERM);
			}
		};
		Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
				Snackbar.LENGTH_INDEFINITE)
				.setAction(R.string.ok, listener)
				.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode != RC_HANDLE_CAMERA_PERM) {
			Log.d(TAG, "Got unexpected permission result: " + requestCode);
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			return;
		}

		if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			// We have permission to access the camera, so create the camera source.
			Log.d(TAG, "Camera permission granted - initializing camera source.");
			createCameraSource();
			return;
		}

		Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
				" Result code = " + ( grantResults.length > 0 ? grantResults[0] : "(empty)" ));
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				getActivity().finish();
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.app_name)
				.setMessage(R.string.no_camera_permission)
				.setPositiveButton(R.string.disappointed_ok, listener)
				.show();
	}

	private void createCameraSource() {
		Log.d(TAG, "createCameraSource called.");

		Context context = getActivity();
		FaceDetector detector = createFaceDetector(context);

		int facing = CameraSource.CAMERA_FACING_FRONT;
		if (!mIsFrontFacing) {
			facing = CameraSource.CAMERA_FACING_BACK;
		}

		// The camera source is initialized to use either the front or rear facing camera.  We use a
		// relatively low resolution for the camera preview, since this is sufficient for this app
		// and the face detector will run faster at lower camera resolutions.
		//
		// However, note that there is a speed/accuracy trade-off with respect to choosing the
		// camera resolution.  The face detector will run faster with lower camera resolutions,
		// but may miss smaller faces, landmarks, or may not correctly detect eyes open/closed in
		// comparison to using higher camera resolutions.  If you have any of these issues, you may
		// want to increase the resolution.
		mCameraSource = new CameraSource.Builder(context, detector)
				.setFacing(facing)
				.setRequestedPreviewSize(320, 240)
				.setRequestedFps(60.0f)
				.setAutoFocusEnabled(true)
				.build();
	}

	private void startCameraSource() {
		Log.d(TAG, "startCameraSource called.");

		// Make sure that the device has Google Play services available.
		int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
		if (code != ConnectionResult.SUCCESS) {
			Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
			dlg.show();
		}

		if (mCameraSource != null) {
			try {
				mPreview.start(mCameraSource, mGraphicOverlay);
			}
			catch (IOException e) {
				Log.e(TAG, "Unable to start camera source.", e);
				mCameraSource.release();
				mCameraSource = null;
			}
		}
	}

	@NonNull
	private FaceDetector createFaceDetector(final Context context) {
		Log.d(TAG, "createFaceDetector called.");

		FaceDetector detector = new FaceDetector.Builder(context)
				.setLandmarkType(FaceDetector.ALL_LANDMARKS)
				.setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
				.setTrackingEnabled(true)
				.setMode(FaceDetector.FAST_MODE)
				.setProminentFaceOnly(mIsFrontFacing)
				.setMinFaceSize(mIsFrontFacing ? 0.35f : 0.15f)
				.build();

		MultiProcessor.Factory<Face> factory = new MultiProcessor.Factory<Face>() {
			@Override
			public Tracker<Face> create(Face face) {
				return new FaceTracker(mGraphicOverlay, context, mIsFrontFacing, renderer);
			}
		};

		Detector.Processor<Face> processor = new MultiProcessor.Builder<>(factory).build();
		detector.setProcessor(processor);

		if (!detector.isOperational()) {
			Log.w(TAG, "Face detector dependencies are not yet available.");

			// Check the device's storage.  If there's little available storage, the native
			// face detection library will not be downloaded, and the app won't work,
			// so notify the user.
			IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
			boolean hasLowStorage = getActivity().registerReceiver(null, lowStorageFilter) != null;

			if (hasLowStorage) {
				Log.w(TAG, getString(R.string.low_storage_error));
				DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						getActivity().finish();
					}
				};
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.app_name)
						.setMessage(R.string.low_storage_error)
						.setPositiveButton(R.string.disappointed_ok, listener)
						.show();
			}
		}
		return detector;
	}

	private FaceGraphic.Renderer renderer = new FaceGraphic.Renderer() {
		@Override
		public void drawEye(Canvas canvas, PointF eyePosition, float eyeRadius, PointF irisPosition, float irisRadius, boolean eyeOpen, boolean smiling) {
			super.drawEye(canvas, eyePosition, eyeRadius, irisPosition, irisRadius, eyeOpen, smiling);
		}

		@Override
		public void drawNose(Canvas canvas, PointF noseBasePosition, PointF leftEyePosition, PointF rightEyePosition, float faceWidth) {
			super.drawNose(canvas, noseBasePosition, leftEyePosition, rightEyePosition, faceWidth);

			final float NOSE_FACE_WIDTH_RATIO = (float) ( 1 / 5.0 );
//            final float NOSE_FACE_WIDTH_RATIO = (float)(1 );
			float noseWidth = faceWidth * NOSE_FACE_WIDTH_RATIO;
			int left = (int) ( noseBasePosition.x - ( noseWidth / 2 ) );
			int right = (int) ( noseBasePosition.x + ( noseWidth / 2 ) );
			int top = (int) ( leftEyePosition.y + rightEyePosition.y ) / 2;
			int bottom = (int) noseBasePosition.y;
			Drawable mPigNoseGraphic = new BitmapDrawable(getResources(), noseBitmap);
			mPigNoseGraphic.setBounds(left, top, right, bottom);
			mPigNoseGraphic.draw(canvas);
		}

		@Override
		public void drawMustache(Canvas canvas, PointF noseBasePosition, PointF mouthLeftPosition, PointF mouthRightPosition) {
			super.drawMustache(canvas, noseBasePosition, mouthLeftPosition, mouthRightPosition);

			int left = (int) mouthLeftPosition.x + 1;
			int top = (int) noseBasePosition.y;
			int right = (int) mouthRightPosition.x + 1;
			int bottom = (int) Math.min(mouthLeftPosition.y, mouthRightPosition.y);
			Drawable mMustacheGraphic = new BitmapDrawable(getResources(), mustacheBitmap);
			if (mIsFrontFacing) {
				mMustacheGraphic.setBounds(left, top, right, bottom);
			}
			else {
				mMustacheGraphic.setBounds(right, top, left, bottom);
			}
			mMustacheGraphic.draw(canvas);
		}

		@Override
		public void drawHat(Canvas canvas, PointF facePosition, float faceWidth, float faceHeight, PointF noseBasePosition) {
			super.drawHat(canvas, facePosition, faceWidth, faceHeight, noseBasePosition);

//            final float HAT_FACE_WIDTH_RATIO = (float)(1.0 / 4.0);
			final float HAT_FACE_WIDTH_RATIO = (float) ( 1.0 );
//            final float HAT_FACE_HEIGHT_RATIO = (float)(1.0 / 6.0);
			final float HAT_FACE_HEIGHT_RATIO = (float) ( 1.0 / 2.0 );
			final float HAT_CENTER_Y_OFFSET_FACTOR = (float) ( 1.0 / 8.0 );

			float hatCenterY = facePosition.y + ( faceHeight * HAT_CENTER_Y_OFFSET_FACTOR );
			float hatWidth = faceWidth * HAT_FACE_WIDTH_RATIO;
			float hatHeight = faceHeight * HAT_FACE_HEIGHT_RATIO;

			int left = (int) ( noseBasePosition.x - ( hatWidth / 2 ) );
			int right = (int) ( noseBasePosition.x + ( hatWidth / 2 ) );
			int top = (int) ( hatCenterY - ( hatHeight / 2 ) );
			int bottom = (int) ( hatCenterY + ( hatHeight / 2 ) );
			Drawable mHatGraphic = new BitmapDrawable(getResources(), headBitmap);

			mHatGraphic.setBounds(left, top, right, bottom);
			mHatGraphic.draw(canvas);
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			try {
				Bundle bundle = data.getExtras();
				switch (bundle.getInt("code", -1)) {
					case RESULT_CODE_EYE:
						eyeBitmap = data.getParcelableExtra("bmp_img");
						break;
					case RESULT_CODE_NOSE:
						noseBitmap = data.getParcelableExtra("bmp_img");
						break;
					case RESULT_CODE_MUSTACHE:
						mustacheBitmap = data.getParcelableExtra("bmp_img");
						break;
					case RESULT_CODE_HEAD:
						headBitmap = data.getParcelableExtra("bmp_img");
						break;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
