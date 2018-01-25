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
	int port = 8888;
	// 变量
	int CurrentSpeed = 0;
	int CurrentDegree = 0;

	private Button button_q;
	private Button button_w;
	private Button button_a;
	private Button button_s;
	private Button button_d;
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
		toggleButton1 = (ToggleButton) this.findViewById(R.id.toggleButton1);
		TextSpeed = (TextView) this.findViewById(R.id.textView_speed);
		TextDegree = (TextView) this.findViewById(R.id.textView_degree);

		toggleButton1.setChecked(false);
		setButtonEnabled(false);
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

	private void ResetUI() {
		TextSpeed.setText(String.valueOf(CurrentSpeed));
		TextDegree.setText(String.valueOf(CurrentDegree));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_q:
			CurrentSpeed = 0;
			CurrentDegree = 0;
			mTask = new MyTask();
			mTask.execute("q");
			ResetUI();
			break;
		case R.id.button_w:
			if (CurrentSpeed < MaxSpeed) {
				CurrentSpeed++;
				// TextSpeed.setText(String.valueOf(CurrentSpeed));
				mTask = new MyTask();
				mTask.execute("w");
			}
			ResetUI();
			break;
		case R.id.button_s:
			if (CurrentSpeed > MinSpeed) {
				CurrentSpeed--;
				// TextSpeed.setText(String.valueOf(CurrentSpeed));
				mTask = new MyTask();
				mTask.execute("s");
			}
			ResetUI();
			break;
		case R.id.button_a:
			if (CurrentDegree < MaxDegree) {
				CurrentDegree++;
				// TextDegree.setText(String.valueOf(CurrentDegree));
				mTask = new MyTask();
				mTask.execute("a");
			}
			ResetUI();
			break;
		case R.id.button_d:
			if (CurrentDegree > MinDegree) {
				CurrentDegree--;
				// TextDegree.setText(String.valueOf(CurrentDegree));
				mTask = new MyTask();
				mTask.execute("d");
			}
			ResetUI();
			break;
		case R.id.button_start:
			// mTask = new MyTask();
			// mTask.execute("start");
			// Toast.makeText(MainActivity.this, "You clicked Button 1",
			// Toast.LENGTH_SHORT).show();
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
			finish();
			break;
		case R.id.toggleButton1:
			if (toggleButton1.isChecked()) {
				setButtonEnabled(true);
			} else {
				setButtonEnabled(false);
			}
			ResetUI();
			break;

		default:
			break;
		}
	}

	void setButtonEnabled(boolean flag) {
		button_q.setEnabled(flag);
		button_w.setEnabled(flag);
		button_a.setEnabled(flag);
		button_d.setEnabled(flag);
		button_s.setEnabled(flag);
		if (flag) {
			mTask = new MyTask();
			mTask.execute("1");

			// before control,click "q" first.
			CurrentSpeed = 0;
			CurrentDegree = 0;
			mTask = new MyTask();
			mTask.execute("q");
		} else {

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

			// Socket socket = null;
			DatagramSocket UDPsocket = null;
			try {
				// socket = new Socket(parms[0], 8888);
				// System.out.println("socket has connected");
				//
				// //2.得到socket读写流
				//
				// OutputStream os=socket.getOutputStream();
				// OutputStreamWriter pw=new OutputStreamWriter (os);
				//
				// BufferedWriter bw = new BufferedWriter(pw);
				//
				// bw.write("TIME");
				// bw.flush();
				UDPsocket = new DatagramSocket(port);
				InetAddress serverAddress = InetAddress.getByName(addr);

				byte bytes[] = { 0x40, 0x23, 0, 0, 0, 4, 0, 1, 1, 1 };
				port = 8888;
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
				if ("1".equals(parms[0])) {
					bytes[9] = 1;
				}
				if ("0".equals(parms[0])) {
					bytes[9] = 0;
				}
				// if ("start".equals(parms[0])) {
				// port = 18888;
				// bytes[1] = 64;
				// bytes[9] = 1;
				// }
				// if ("stop".equals(parms[0])) {
				// port = 18888;
				// bytes[1] = 64;
				// bytes[9] = 2;
				// }
				DatagramPacket p = new DatagramPacket(bytes, bytes.length,
						serverAddress, port);
				UDPsocket.send(p);
				// Log.d("main2", String.valueOf(bytes.length));
				// 从服务端程序接收数据
				// InputStream ips = socket.getInputStream();
				// InputStreamReader ipsr = new InputStreamReader(ips);
				// BufferedReader br = new BufferedReader(ipsr);
				// String s = "";
				// String result="";
				// while((s = br.readLine()) != null) {
				// result += s;
				// System.out.println(s);
				// }
				UDPsocket.close();
				// return result;
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
