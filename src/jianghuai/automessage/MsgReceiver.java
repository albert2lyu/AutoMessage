package jianghuai.automessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class MsgReceiver extends BroadcastReceiver {
	// �յ��Ķ�������
	private String recvmsg;
	// ���ص�����
	private String submsg;
	// �Զ��ظ�����
	private String auto_reply;

	SharedPreferences sharedPreferences;

	public static final String DEFAULT_SUBMSG = "#�ظ���";
	public static final String DEFAULT_REPLY = "����ά��";

	// ������ʽ
	public static final String regex = "\\[[A-Z]+\\]";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SmsMessage msg = null;
		if (null != bundle) {
			Object[] smsObj = (Object[]) bundle.get("pdus");
			for (Object object : smsObj) {
				msg = SmsMessage.createFromPdu((byte[]) object);
				Date date = new Date(msg.getTimestampMillis());// ʱ��
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String receiveTime = format.format(date);
				System.out.println("number:" + msg.getOriginatingAddress()
						+ "   body:" + msg.getDisplayMessageBody() + "  time:"
						+ msg.getTimestampMillis());

				recvmsg = msg.getDisplayMessageBody();

				// �õ�sharedPreferences����������е���������edit_detail
				sharedPreferences = context.getSharedPreferences("autoMsg",
						Context.MODE_PRIVATE);
				submsg = sharedPreferences.getString("custom_submsg", "");
				// ����Զ���ظ�����
				auto_reply = sharedPreferences.getString("custom_reply", "");
				// ���û�������������ݣ���ʹ��Ĭ�ϵ���������
				if (submsg.equals("")) {
					// û���������ݣ�ʹ��Ĭ�ϵġ�#�ظ��롱
					if (recvmsg.contains(DEFAULT_SUBMSG)) {
						SmsManager smsManager = SmsManager.getDefault();
						if (auto_reply.equals("")) {
							smsManager.sendTextMessage(
									msg.getOriginatingAddress(), null, "JD"
											+ pickString(recvmsg) + "#"
											+ DEFAULT_REPLY, null, null);
						} else {
							smsManager.sendTextMessage(
									msg.getOriginatingAddress(), null, "JD"
											+ pickString(recvmsg) + "#"
											+ auto_reply, null, null);
						}

					}
				} else {
					// submsg�Ĺ���������Ϊ�գ������ż�⵽ʱ���Զ��ظ�
					if (recvmsg.contains(submsg)) {
						SmsManager smsManager = SmsManager.getDefault();
						if (auto_reply.equals("")) {
							smsManager.sendTextMessage(
									msg.getOriginatingAddress(), null,
									DEFAULT_REPLY, null, null);
						} else {
							smsManager.sendTextMessage(
									msg.getOriginatingAddress(), null,
									auto_reply, null, null);
						}
					}

				}

			}
		}

	}

	/*
	 * ��ȡ��֤�� �����������msg.getDisplayMessageBody()
	 */
	public String pickString(String str) {
		String result = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(str);

		if (m.find()) {
			result = (String) m.group().subSequence(1, 4);
		}
		return result;

	}

}
