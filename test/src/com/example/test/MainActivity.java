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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	// 设置项
	int MaxSpeed = 5;
	int MinSpeed = -5;
	int MaxDegree = 7;
	int MinDegree = -7;

	String addr = "192.168.1.10";
	//String addr="192.168.1.109";
	int port = 8888;
	// 变量
	int CurrentSpeed = 0;
	int CurrentDegree = 0;

	private Button button_q;
	private Button button_w;
	private Button button_a;
	private Button button_s;
	private Button button_d;
	private Button button_start;
	private TextView TextSpeed;
	private TextView TextDegree;
	private ToggleButton toggleButton1;
	MyTask mTask = new MyTask();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button_q).setOnClickListener(MainActivity.this);
		findViewById(R.id.button_w).setOnClickListener(MainActivity.this);
		findViewById(R.id.button_s).setOnClickListener(MainActivity.this);
		findViewById(R.id.button_a).setOnClickListener(MainActivity.this);
		findViewById(R.id.button_d).setOnClickListener(MainActivity.this);
		findViewById(R.id.button_start).setOnClickListener(MainActivity.this);
		findViewById(R.id.button_stop).setOnClickListener(MainActivity.this);
		findViewById(R.id.toggleButton1).setOnClickListener(MainActivity.this);

		button_q = (Button) this.findViewById(R.id.button_q);
		button_w = (Button) this.findViewById(R.id.button_w);
		button_a = (Button) this.findViewById(R.id.button_a);
		button_s = (Button) this.findViewById(R.id.button_s);
		button_d = (Button) this.findViewById(R.id.button_d);
		button_start = (Button) this.findViewById(R.id.button_start);
		toggleButton1 = (ToggleButton) this.findViewById(R.id.toggleButton1);
		TextSpeed = (TextView) this.findViewById(R.id.textView_speed);
		TextDegree = (TextView) this.findViewById(R.id.textView_degree);

		toggleButton1.setChecked(false);
		setButtonEnabled(false);
		Log.d("main", "oncreate");
	}

	@Override
	protected void onRestart() {
		toggleButton1.setChecked(false);
		setButtonEnabled(false);
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		toggleButton1.setChecked(false);
		setButtonEnabled(false);
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		toggleButton1.setChecked(false);
		setButtonEnabled(false);
		super.onStop();
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_q:
			CurrentSpeed = 0;
			CurrentDegree = 0;
			mTask = new MyTask();
			mTask.execute("q");
			break;
		case R.id.button_w:
			if (CurrentSpeed < MaxSpeed) {
				CurrentSpeed++;
				// TextSpeed.setText(String.valueOf(CurrentSpeed));
				mTask = new MyTask();
				mTask.execute("w");
			}
			break;
		case R.id.button_s:
			if (CurrentSpeed > MinSpeed) {
				CurrentSpeed--;
				// TextSpeed.setText(String.valueOf(CurrentSpeed));
				mTask = new MyTask();
				mTask.execute("s");
			}
			break;
		case R.id.button_a:
			if (CurrentDegree < MaxDegree) {
				CurrentDegree++;
				// TextDegree.setText(String.valueOf(CurrentDegree));
				mTask = new MyTask();
				mTask.execute("a");
			}
			break;
		case R.id.button_d:
			if (CurrentDegree > MinDegree) {
				CurrentDegree--;
				// TextDegree.setText(String.valueOf(CurrentDegree));
				mTask = new MyTask();
				mTask.execute("d");
			}
			break;
		case R.id.button_start:
			CurrentSpeed = 0;
			CurrentDegree = 0;
			mTask = new MyTask();
			mTask.execute("q");
			new AlertDialog.Builder(this)
					.setTitle("确认")
					.setMessage("请确认机器人是否已经遥控到正确位置了？")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											MainActivity.this,
											TaskActivity.class);
									startActivity(intent);
								}
							}).setNegativeButton("否", null).show();

			break;
		case R.id.button_stop:
			// mTask = new MyTask();
			// mTask.execute("stop");
			//android.os.Process.killProcess(android.os.Process.myPid());
			finish();
			break;
		case R.id.toggleButton1:
			if (toggleButton1.isChecked()) {
				setButtonEnabled(true);
			} else {
				setButtonEnabled(false);
			}
			break;

		default:
			break;
		}
	}

	void setButtonEnabled(boolean flag) {
		Log.d("main", "onsetbuttonenabled");
		button_q.setEnabled(flag);
		button_w.setEnabled(flag);
		button_a.setEnabled(flag);
		button_d.setEnabled(flag);
		button_s.setEnabled(flag);
		button_start.setEnabled(flag);
		if (flag) {
			mTask = new MyTask();
			mTask.execute("login");

			mTask = new MyTask();
			mTask.execute("1");
		} else {
			// before control,click "q" first.
			CurrentSpeed = 0;
			CurrentDegree = 0;
			mTask = new MyTask();
			mTask.execute("q");

			mTask = new MyTask();
			mTask.execute("0");
		}

	}

	private class MyTask extends AsyncTask<String, Integer, String> {

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			// TextTime.setText("fwe");
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		protected String doInBackground(String... parms) {
			Log.d("main", "task doInBackground");
			// Socket socket = null;
			DatagramSocket UDPsocket = null;
			try {
				byte bytes[] = { 0x40, 0x23, 0, 0, 0, 4, 0, 1, 1, 1 };
				port = 8888;
				if ("login".equals(parms[0])) {//20180507修改为发送中止任务
					Log.d("main", "task doInBackground login");
					port = 18888;
//					bytes[0] = 22;
//					bytes[1] = 44;
//					bytes[2] = 66;
//					bytes[3] = 66;
					bytes[0] = 0x40;
					bytes[1] = 0x40;
					bytes[9] = 7;
				}

				if ("w".equals(parms[0])) {
					bytes[9] = 2;
				}
				if ("s".equals(parms[0])) {
					bytes[9] = 3;
				}
				if ("a".equals(parms[0])) {
					bytes[9] = 4;
				}
				if ("d".equals(parms[0])) {
					bytes[9] = 5;
				}
				if ("q".equals(parms[0])) {
					bytes[9] = 6;
				}
				if ("1".equals(parms[0])) {//发送此命令开启机器人受遥控的状态
					bytes[9] = 1;
				}
				if ("0".equals(parms[0])) {//发送此命令关闭机器人受遥控的状态
					bytes[9] = 0;
				}

				UDPsocket = new DatagramSocket();
				InetAddress serverAddress = InetAddress.getByName(addr);

				DatagramPacket p = new DatagramPacket(bytes, bytes.length,
						serverAddress, port);
				UDPsocket.send(p);
				Log.d("main", "task doInBackground end send");
				// 从服务端程序接收数据
				// Log.d("main",
				// String.valueOf(bytes[0]) + String.valueOf(bytes[1]));
				// if (bytes[0] == 22 && bytes[1] == 44) {
				// Log.d("main", "begin receive data");
				// try {
				// UDPsocket.receive(p);
				// String result = new String(p.getData(), 0,
				// p.getLength());
				// Log.d("main", "end receive data: " + result);
				// if ("no".equals(result)) {
				// Log.d("main", "no");
				// return "no";
				// } else if ("yes".equals(result)) {
				// Log.d("main", "yes");
				// return "yes";
				// } else {
				// Log.d("main", "unknow1");
				// return "nuknow";
				// }
				// } catch (IOException e) {
				// Log.d("main", "IOException");
				// e.printStackTrace();
				// }
				//
				// }
				UDPsocket.close();
				UDPsocket = null;
				// return result;
				Log.d("main", "ok");
				return "OK";
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("main", "error");
			return "ERROR";
		}

		// onProgressUpdate方法用于更新进度信息
		protected void onProgressUpdate(Integer... progress) {
			// progressBar.setProgress(progresses[0]);
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		protected void onPostExecute(String result) {
			if ("OK".equals(result)) {
				TextSpeed.setText(String.valueOf(CurrentSpeed));
				TextDegree.setText(String.valueOf(CurrentDegree));
			}
			if ("no".equals(result)) {
				finish();
			}
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
		}
	}
}
