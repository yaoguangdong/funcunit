package com.commonsware.cwac.camera;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.yaogd.R;

/**
 * @author: yaoguangdong
 * @data: 2014-2-7
 */
public class CWACActivity extends Activity implements OnSeekBarChangeListener {
	private CameraView cameraView = null;
	private CameraHost host = null;
	private SeekBar zoom = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment);

		cameraView = new CameraView(this);
		cameraView.setHost(getHost());
		((ViewGroup) findViewById(R.id.camera)).addView(cameraView);
		zoom = (SeekBar) findViewById(R.id.zoom);

	}

	@Override
	public void onResume() {
		super.onResume();

		cameraView.onResume();
	}

	@Override
	public void onPause() {
		cameraView.onPause();

		super.onPause();
	}

	protected void setCameraView(CameraView cameraView) {
		this.cameraView = cameraView;
	}

	public CameraHost getHost() {
		if (host == null) {
			host = new SimpleCameraHost(this);
		}

		return (host);
	}

	public void setHost(CameraHost host) {
		this.host = host;
	}

	public void takePicture() {
		takePicture(false, true);
	}

	public void takePicture(boolean needBitmap, boolean needByteArray) {
		cameraView.takePicture(needBitmap, needByteArray);
	}

	/**
	 * @return the orientation of the screen, in degrees (0-360)
	 */
	public int getDisplayOrientation() {
		return (cameraView.getDisplayOrientation());
	}

	/**
	 * Call this to lock the camera to landscape mode (with a parameter of
	 * true), regardless of what the actual screen orientation is.
	 * 
	 * @param enable
	 *            true to lock the camera to landscape, false to allow normal
	 *            rotation
	 */
	public void lockToLandscape(boolean enable) {
		cameraView.lockToLandscape(enable);
	}

	/**
	 * Call this to begin an auto-focus operation (e.g., in response to the user
	 * tapping something to focus the camera).
	 */
	public void autoFocus() {
		cameraView.autoFocus();
	}

	/**
	 * Call this to cancel an auto-focus operation that had been started via a
	 * call to autoFocus().
	 */
	public void cancelAutoFocus() {
		cameraView.cancelAutoFocus();
	}

	/**
	 * @return true if auto-focus is an option on this device, false otherwise
	 */
	public boolean isAutoFocusAvailable() {
		return (cameraView.isAutoFocusAvailable());
	}

	/**
	 * If you are in single-shot mode and are done processing a previous
	 * picture, call this to restart the camera preview.
	 */
	public void restartPreview() {
		cameraView.restartPreview();
	}

	/**
	 * @return the name of the current flash mode, as reported by
	 *         Camera.Parameters
	 */
	public String getFlashMode() {
		return (cameraView.getFlashMode());
	}

	/**
	 * Call this to begin populating a ZoomTransaction, with the eventual goal
	 * of changing the camera's zoom level.
	 * 
	 * @param level
	 *            a value from 0 to getMaxZoom() (called on Camera.Parameters),
	 *            to indicate how tight the zoom should be (0 indicates no zoom)
	 * @return a ZoomTransaction to configure further and eventually call go()
	 *         to actually do the zooming
	 */
	public ZoomTransaction zoomTo(int level) {
		return (cameraView.zoomTo(level));
	}

	public boolean doesZoomReallyWork() {
		return (cameraView.doesZoomReallyWork());
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			zoom.setEnabled(false);
			zoomTo(zoom.getProgress()).onComplete(new Runnable() {
				@Override
				public void run() {
					zoom.setEnabled(true);
				}
			}).go();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}
}
