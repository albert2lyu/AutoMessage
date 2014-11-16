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
	// 收到的短信内容
	private String recvmsg;
	// 拦截的内容
	private String submsg;
	// 自动回复内容
	private String auto_reply;

	SharedPreferences sharedPreferences;

	public static final String DEFAULT_SUBMSG = "#回复码";
	public static final String DEFAULT_REPLY = "上门维修";

	// 正则表达式
	public static final String regex = "\\[[A-Z]+\\]";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SmsMessage msg = null;
		if (null != bundle) {
			Object[] smsObj = (Object[]) bundle.get("pdus");
			for (Object object : smsObj) {
				msg = SmsMessage.createFromPdu((byte[]) object);
				Date date = new Date(msg.getTimestampMillis());// 时间
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String receiveTime = format.format(date);
				System.out.println("number:" + msg.getOriginatingAddress()
						+ "   body:" + msg.getDisplayMessageBody() + "  time:"
						+ msg.getTimestampMillis());

				recvmsg = msg.getDisplayMessageBody();

				// 拿到sharedPreferences，并获得其中的拦截内容edit_detail
				sharedPreferences = context.getSharedPreferences("autoMsg",
						Context.MODE_PRIVATE);
				submsg = sharedPreferences.getString("custom_submsg", "");
				// 获得自定义回复内容
				auto_reply = sharedPreferences.getString("custom_reply", "");
				// 如果没有输入拦截内容，则使用默认的拦截条件
				if (submsg.equals("")) {
					// 没有输入内容，使用默认的“#回复码”
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
					// submsg的过滤条件不为空，当短信检测到时，自动回复
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
	 * 提取验证码 传入短信内容msg.getDisplayMessageBody()
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
