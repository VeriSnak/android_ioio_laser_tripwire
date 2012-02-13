package net.mitchtech.ioio;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import net.mitchtech.ioio.lasertripwire.R;
import android.os.Bundle;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

public class LaserTripwireActivity extends AbstractIOIOActivity {

	private final int ANALOG_SENSOR_PIN = 34;

	private TextView mValueTextView;
	private SeekBar mValueSeekBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		mValueTextView = (TextView) findViewById(R.id.tvValue);
		mValueSeekBar = (SeekBar) findViewById(R.id.sbValue);

	}

	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		private AnalogInput mInput;

		@Override
		public void setup() throws ConnectionLostException {
			try {
				mInput = ioio_.openAnalogInput(ANALOG_SENSOR_PIN);
			} catch (ConnectionLostException e) {
				throw e;
			}
		}

		@Override
		public void loop() throws ConnectionLostException {
			try {
				final float reading = mInput.read();
				setSeekBar((int) (reading * 100));
				setText(Float.toString((reading * 100)));
				sleep(10);
			} catch (InterruptedException e) {
				ioio_.disconnect();
			} catch (ConnectionLostException e) {
				throw e;
			}
		}
	}

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}

	private void setSeekBar(final int value) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mValueSeekBar.setProgress(value);
			}
		});
	}

	private void setText(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mValueTextView.setText(str);
			}
		});
	}
}