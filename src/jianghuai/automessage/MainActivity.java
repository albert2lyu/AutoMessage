package jianghuai.automessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//�Զ�����������
	private String submsg;
	//�Զ����������
	private String auto_reply;
	
	//�Զ����������ݺ��Զ���������ݵ������
	private EditText edit_submsg, edit_auto_reply;
	private Button button;
	//��ʾ�Զ����������ݺ��Զ���ظ��������ݵ�TextView
	private TextView tv_submsg, tv_auto_reply;

	SharedPreferences sharedPreferences;
	
	private MsgReceiver msgReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		//��̬ע��broadcastreceiver
		msgReceiver = new MsgReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		MainActivity.this.registerReceiver(msgReceiver, filter);
		*/
		

		edit_submsg = (EditText) findViewById(R.id.edit_submsg);
		button = (Button) findViewById(R.id.submit);
		tv_submsg = (TextView) findViewById(R.id.tv_submsg);
		edit_auto_reply = (EditText) findViewById(R.id.edit_auto_reply);
		tv_auto_reply = (TextView) findViewById(R.id.tv_auto_reply);

		/*
		 * ���Զ��������ݺ��Զ��ظ����ݴ洢��autoMsg�ļ���,
		 * ��ֵΪcustom_submsg��custom_reply
		 */
		sharedPreferences = getSharedPreferences("autoMsg",
				Context.MODE_PRIVATE);
		final Editor editor = sharedPreferences.edit();
		
		tv_submsg.setText("��ǰ�Զ�����������Ϊ:"
				+ sharedPreferences.getString("custom_submsg", ""));
		tv_auto_reply.setText("��ǰ�Զ���ظ�����Ϊ:"
				+ sharedPreferences.getString("custom_reply", ""));

		// �ύ������еĶ����������ݺ��Զ��ظ�����
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����Զ������ض�������
				submsg = edit_submsg.getText().toString();
				// ����Զ���ظ���������
				auto_reply = edit_auto_reply.getText().toString();

				//����ֵ�Ա������ļ��У����ύ
				editor.putString("custom_submsg", submsg);
				editor.putString("custom_reply", auto_reply);
				editor.commit();
				
				tv_submsg.setText("��ǰ�Զ�����������Ϊ:"
						+ sharedPreferences.getString("custom_submsg", ""));
				tv_auto_reply.setText("��ǰ�Զ���ظ�����Ϊ:"
						+ sharedPreferences.getString("custom_reply", ""));
				Toast.makeText(getApplicationContext(), "�Զ����������ݣ��ظ������ύ�ɹ�",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//MainActivity.this.unregisterReceiver(msgReceiver);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
