package com.example.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class TaskActivity extends ActionBarActivity implements OnClickListener{
	String addr = "192.168.1.10";
	int port = 18888;
	
	private Button button_4;
	
	public int T = 60; //����ʱʱ��  
    private Handler mHandler = new Handler();  
	
	MyTask mTask = new MyTask();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task);
		findViewById(R.id.button_1).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_2).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_3).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_4).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_5).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_6).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_7).setOnClickListener(TaskActivity.this);
		findViewById(R.id.button_8).setOnClickListener(TaskActivity.this);
		
		button_4 = (Button)this.findViewById(R.id.button_4);
	}
	
//	CountDownTimer timer = new CountDownTimer(10000, 1000) {
//		private String s=button_4.getText().toString();
//        @Override
//        public void onTick(long millisUntilFinished) {
//            button_4.setEnabled(false);
//            button_4.setText(s+"(" + millisUntilFinished / 1000 + ")");
//
//        }
//
//        @Override
//        public void onFinish() {
//        	button_4.setEnabled(true);
//        	button_4.setText(s);
//
//        }
//    };
	
    class MyCountDownTimer implements Runnable{  
    	private String s=button_4.getText().toString();  
        @Override  
        public void run() {  
  
            //����ʱ��ʼ��ѭ��  
            while (T > 0) {  
                mHandler.post(new Runnable() {  
                    @Override  
                    public void run() {  
                    	button_4.setEnabled(false);  
                        button_4.setText(T + "��");  
                    }  
                });  
                try {  
                    Thread.sleep(1000); //ǿ���߳�����1�룬�������õ���ʱ�ļ��ʱ��Ϊ1�롣  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                T--;  
            }  
  
            //����ʱ������Ҳ����ѭ������  
            mHandler.post(new Runnable() {  
                @Override  
                public void run() {  
                	button_4.setClickable(true);  
                	button_4.setText(s);  
                }  
            });  
            T = 10; //����ٻָ�����ʱʱ��  
        }  
    }  
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_1:
			mTask = new MyTask();
			mTask.execute("1");
			break;
		case R.id.button_2:
			mTask = new MyTask();
			mTask.execute("2");
			break;
		case R.id.button_3:
			mTask = new MyTask();
			mTask.execute("3");
			break;
		case R.id.button_4:
			//timer.start();
			new Thread(new MyCountDownTimer()).start();//��ʼִ�� 
			mTask = new MyTask();
			mTask.execute("4");
			break;
		case R.id.button_5:
			mTask = new MyTask();
			mTask.execute("5");
			break;
		case R.id.button_6:
			mTask = new MyTask();
			mTask.execute("6");
			break;
		case R.id.button_7:
			mTask = new MyTask();
			mTask.execute("7");
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

		// doInBackground�����ڲ�ִ�к�̨����,�����ڴ˷������޸�UI
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
					bytes[9] =5;
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

		// onProgressUpdate�������ڸ��½�����Ϣ
		protected void onProgressUpdate(Integer... progress) {
			// progressBar.setProgress(progresses[0]);
		}

		// onPostExecute����������ִ�����̨��������UI,��ʾ���
		protected void onPostExecute(String result) {
			// TextTime.setText(result);
		}

		// onCancelled����������ȡ��ִ���е�����ʱ����UI
		@Override
		protected void onCancelled() {
		}
	}

	



}
