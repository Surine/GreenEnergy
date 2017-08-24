package google.google.zxing.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.greenenergy.greenenergy.Init.StatusUI;
import com.greenenergy.greenenergy.R;
import com.greenenergy.greenenergy.UI.CheckActivity;

import java.io.IOException;
import java.util.Vector;
import java.util.regex.Pattern;

import google.google.zxing.camera.CameraManager;
import google.google.zxing.decoding.CaptureActivityHandler;
import google.google.zxing.decoding.InactivityTimer;
import google.google.zxing.view.ViewfinderView;


/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Button light;
	private Camera camera;
	private Camera.Parameters parameter;
	private boolean isOpen =false;
	private Button number;
	private ImageView key;
	private ImageView light_icon;
	private EditText editText;
//	private Button cancelScanButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);
		StatusUI.StatusUISetting(this,"#50000000");
		//toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		setTitle("扫码投放");
		ActionBar actionBar = getSupportActionBar();
		if(actionBar!=null){
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_content);
//		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		light = (Button) findViewById(R.id.light);
		light_icon = (ImageView) findViewById(R.id.light_icon);
		light_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//闪光灯
				CameraManager.get().flashHandler();
			}
		});
		light.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//闪光灯
				CameraManager.get().flashHandler();
			}
		});
        key = (ImageView) findViewById(R.id.key);
		key.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ShowDialog();
			}
		});
		number = (Button)findViewById(R.id.number);
		number.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			ShowDialog();
			}
		});
	}

	private void ShowDialog() {
		View myview = LayoutInflater.from(CaptureActivity.this).inflate(R.layout.dialog_input_number,null);
		editText = (EditText) myview.findViewById(R.id.enter_number);
		AlertDialog.Builder builder = new AlertDialog.Builder(CaptureActivity.this);

		builder.setView(myview);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				if(!editText.getText().toString().equals("")&&editText.getText().toString().length()==10){
					startActivity(new Intent(CaptureActivity.this,CheckActivity.class).putExtra("RESULT",editText.getText().toString()));
					finish();
				}else{
					Toast.makeText(CaptureActivity.this,"输入有误",Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.create();
		builder.show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.scanner_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
		//quit the scan view
//		cancelScanButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				CaptureActivity.this.finish();
//			}
//		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		//FIXME
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "扫描失败!", Toast.LENGTH_SHORT).show();
		}else {
//			Intent resultIntent = new Intent();
//			Bundle bundle = new Bundle();
//			bundle.putString("result", resultString);
//			resultIntent.putExtras(bundle);
//			this.setResult(RESULT_OK, resultIntent);
			if(isDia(resultString)&&resultString.length() == 10) {
				startActivity(new Intent(CaptureActivity.this, CheckActivity.class).putExtra("RESULT", resultString));
				finish();
			}else{
				Toast.makeText(CaptureActivity.this, "鉴权失败!", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	//检查是否纯数字
	private boolean isDia(String resultString) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(resultString).matches();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}