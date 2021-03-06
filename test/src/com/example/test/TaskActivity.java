package com.example.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TaskActivity extends ActionBarActivity implements OnClickListener {
	String addr = "192.168.1.10";
	int port = 18888;

	private Button[] buttons = new Button[6];

	public int T = 60; // 倒计时时长
	private Handler mHandler = new Handler();

	MyTask mTask = new MyTask();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task);
		findViewById(R.id.button_1).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_2).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_3).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_4).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_5).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_6).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_7).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_8).setOnClickListener(TaskActivity.this);

		buttons[0] = (Button) this.findViewById(R.id.button_1);
		buttons[1] = (Button) this.findViewById(R.id.button_2);
		buttons[2] = (Button) this.findViewById(R.id.button_3);
		buttons[3] = (Button) this.findViewById(R.id.button_4);
		buttons[4] = (Button) this.findViewById(R.id.button_5);
		buttons[5] = (Button) this.findViewById(R.id.button_6);
	}

	// CountDownTimer timer = new CountDownTimer(10000, 1000) {
	// private String s=button_4.getText().toString();
	// @Override
	// public void onTick(long millisUntilFinished) {
	// button_4.setEnabled(false);
	// button_4.setText(s+"(" + millisUntilFinished / 1000 + ")");
	//
	// }
	//
	// @Override
	// public void onFinish() {
	// button_4.setEnabled(true);
	// button_4.setText(s);
	//
	// }
	// };

	class MyCountDownTimer implements Runnable {
		private String s[] = new String[6];

		@Override
		public void run() {
			for (int i = 0; i < 6; i++) {
				s[i] = buttons[i].getText().toString();
			}
			// 倒计时开始，循环
			while (T > 0) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < 6; i++) {
							buttons[i].setEnabled(false);
							buttons[i].setText(s[i] + T + "秒");
						}
					}
				});
				try {
					Thread.sleep(1000); // 强制线程休眠1秒，就是设置倒计时的间隔时间为1秒。
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				T--;
			}

			// 倒计时结束，也就是循环结束
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 6; i++) {
						buttons[i].setEnabled(true);
						buttons[i].setText(s[i]);
					}
				}
			});
			T = 60; // 最后再恢复倒计时时长
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_1:
			new Thread(new MyCountDownTimer()).start();
			mTask = new MyTask();
			mTask.execute("1");
			break;
		case R.id.button_2:
			new Thread(new MyCountDownTimer()).start();
			mTask = new MyTask();
			mTask.execute("2");
			break;
		case R.id.button_3:
			new Thread(new MyCountDownTimer()).start();
			mTask = new MyTask();
			mTask.execute("3");
			break;
		case R.id.button_4:
			// timer.start();
			new Thread(new MyCountDownTimer()).start();
			mTask = new MyTask();
			mTask.execute("4");
			break;
		case R.id.button_5:
			new Thread(new MyCountDownTimer()).start();
			mTask = new MyTask();
			mTask.execute("5");
			break;
		case R.id.button_6:
			new Thread(new MyCountDownTimer()).start();
			mTask = new MyTask();
			mTask.execute("6");
			break;
		case R.id.button_7:
			new AlertDialog.Builder(this)
					.setTitle("确认")
					.setMessage("请确认是否要终止任务 ？")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mTask = new MyTask();
									mTask.execute("7");
								}
							}).setNegativeButton("否", null).show();

			break;
		case R.id.button_8:
			finish();
			break;

		default:
			break;
		}
	}

	private class MyTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		protected String doInBackground(String... parms) {
			DatagramSocket UDPsocket = null;
			try {
				UDPsocket = new DatagramSocket(port);
				InetAddress serverAddress = InetAddress.getByName(addr);

				byte bytes[] = { 0x40, 0x40, 0, 0, 0, 4, 0, 1, 1, 1 };
				if ("1".equals(parms[0])) {
					bytes[9] = 1;
				}
				if ("2".equals(parms[0])) {
					bytes[9] = 2;
				}
				if ("3".equals(parms[0])) {
					bytes[9] = 3;
				}
				if ("4".equals(parms[0])) {
					bytes[9] = 4;
				}
				if ("5".equals(parms[0])) {
					bytes[9] = 5;
				}
				if ("6".equals(parms[0])) {
					bytes[9] = 6;
				}
				if ("7".equals(parms[0])) {
					bytes[9] = 7;
				}
				DatagramPacket p = new DatagramPacket(bytes, bytes.length,
						serverAddress, port);
				UDPsocket.send(p);
				UDPsocket.close();
				return "d";
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("main", "f");
			return "off";
		}

		// onProgressUpdate方法用于更新进度信息
		protected void onProgressUpdate(Integer... progress) {
			// progressBar.setProgress(progresses[0]);
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		protected void onPostExecute(String result) {
			// TextTime.setText(result);
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
		}
	}

}
