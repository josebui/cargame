package cargame.android;

import cargame.android.gui.AndroidGUILogOutput;
import cargame.android.gui.AndroidShootError;
import cargame.communication.tracker.client.TrackerClient;
import cargame.error.ShootError;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class TrackerClientActivity extends Activity {

	/**
	 * Keep track of the connection task to ensure we can cancel it if requested.
	 */
	private ConnectionTask mConnectionTask = null;

	private String mTrackerAddress;
	private String mTrackerPort;

	// UI references.
	private EditText mTrackerAddressView;
	private EditText mTrackerPortView;
	private View mConnectionFormView;
	private View mConnectionStatusView;
	private TextView mConnectionStatusMessageView;

	private Button mConnectButtonView;
	
	
	public TrackerClientActivity() {
		super();
		ShootError.registerShootErrorImplementation(new AndroidShootError());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tracker_client);

		// Set up the connection form.
		mTrackerAddressView = (EditText) findViewById(R.id.trackerIP);
		mTrackerAddressView.setText(mTrackerAddress);

		mTrackerPortView = (EditText) findViewById(R.id.trackerPort);
		mTrackerPortView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptConnection();
							return true;
						}
						return false;
					}
				});

		mConnectionFormView = findViewById(R.id.connection_form);
		mConnectionStatusView = findViewById(R.id.tracker_status);
		mConnectionStatusMessageView = (TextView) findViewById(R.id.tracker_status_message);

		mConnectButtonView = (Button)findViewById(R.id.connect_button);
		
		findViewById(R.id.connect_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptConnection();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.tracker_client, menu);
		return true;
	}

	/**
	 * Attempts to connect to a game
	 */
	public void attemptConnection() {
		if (mConnectionTask != null) {
			return;
		}

		// Reset errors.
		mTrackerAddressView.setError(null);
		mTrackerPortView.setError(null);

		// Store values at the time of the login attempt.
		mTrackerAddress = mTrackerAddressView.getText().toString();
		mTrackerPort = mTrackerPortView.getText().toString();

		boolean cancel = false;
		View focusView = null;

//		// TODO Check for a valid password.
//		if (TextUtils.isEmpty(mTrackerPort)) {
//			mTrackerPortView.setError(getString(R.string.error_field_required));
//			focusView = mTrackerPortView;
//			cancel = true;
//		} else if (mTrackerPort.length() < 4) {
//			mTrackerPortView.setError(getString(R.string.error_invalid_password));
//			focusView = mTrackerPortView;
//			cancel = true;
//		}
//
//		// Check for a valid email address.
//		if (TextUtils.isEmpty(mTrackerAddress)) {
//			mTrackerAddressView.setError(getString(R.string.error_field_required));
//			focusView = mTrackerAddressView;
//			cancel = true;
//		} else if (!mTrackerAddress.contains("@")) {
//			mTrackerAddressView.setError(getString(R.string.error_invalid_email));
//			focusView = mTrackerAddressView;
//			cancel = true;
//		}

		if (cancel) {
//			TODO when validation fixed focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mConnectionStatusMessageView.setText(R.string.tracker_progress_connecting);
			showProgress(true);
			mConnectionTask = new ConnectionTask(this.mTrackerAddress,Integer.parseInt(this.mTrackerPort));
			mConnectionTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mConnectionStatusView.setVisibility(View.VISIBLE);
			mConnectionStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mConnectionStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mConnectionFormView.setVisibility(View.VISIBLE);
			mConnectionFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mConnectionFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mConnectionStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mConnectionFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous connection task used to join a game.
	 */
	public class ConnectionTask extends AsyncTask<Void, Void, Boolean> {
		
		private String ip;
		private int port;
		
		private String gameId;
		private boolean server;
		private String peerIp;
		
		public ConnectionTask(String ip, int port) {
			this.ip = ip;
			this.port = port;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
//				System.out.println(IP+":"+port);
				TrackerClient trackerClient = new TrackerClient(ip, port, new AndroidGUILogOutput());
				trackerClient.start();
				trackerClient.join();
//				window.closeWindow();
				if (trackerClient.ifGotAnswerFromTracker()){
					this.gameId = trackerClient.gameId;
					this.server = trackerClient.isServer;
					this.peerIp = trackerClient.toConnectIP;
//					LwjglApplication app = Main.startCarGameDesktop(carGameUI,trackerClient.gameId,trackerClient.isServer,
//							trackerClient.toConnectIP, 1,
//							(trackerClient.isServer ? 1 : 3));
//					app.addLifecycleListener(new DesktopApplicationListener(carGameUI));
					return true;
				}else{
//					carGameUI.toggleButton();
					return false;
				}
			} catch (InterruptedException e) {
				ShootError.shoot("Interrupted",	"Application execution was interrupted!");
				return false;
			}

//			for (String credential : DUMMY_CREDENTIALS) {
//				String[] pieces = credential.split(":");
//				if (pieces[0].equals(mTrackerAddress)) {
//					// Account exists, return true if the password matches.
//					return pieces[1].equals(mTrackerPort);
//				}
//			}

			// TODO: register the new account here.
//			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mConnectionTask = null;
			showProgress(false);

			if (success) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("gameId", this.gameId);
				intent.putExtra("server", this.server);
				intent.putExtra("peerIp", this.peerIp);
				startActivityForResult(intent, 0);
			} else {
//				mConnectionStatusMessageView.setText(R.string.error_tracker_unreachable);
				mConnectButtonView.setError(getString(R.string.error_tracker_unreachable));
				mTrackerAddressView.setError(getString(R.string.error_tracker_unreachable));
//				mTrackerPortView.setError(getString(R.string.error_incorrect_password));
//				mTrackerPortView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mConnectionTask = null;
			showProgress(false);
		}
	}
}
