package cn.getdone.common;

import android.util.Log;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.DeviceEnum;
import cn.jpush.api.push.CustomMessageParams;
import cn.jpush.api.push.MessageResult;
import cn.jpush.api.push.ReceiverTypeEnum;

public class JPushUtils {
	
	public static boolean pushMessage(String userId,String msg){
		boolean result = false;
		final String masterSecret = "d9073a45570bce1b7a0d6308";
		final String appKey = "df9192d420e4ad66d5f8950c";
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 0, DeviceEnum.Android, false);
		CustomMessageParams params = new CustomMessageParams();
		params.setReceiverType(ReceiverTypeEnum.REGISTRATION_ID);
		params.setReceiverValue(userId);

		MessageResult msgResult = jpushClient.sendCustomMessage("", msg, params, null);
		
		Log.d("JPushUtils","responseContent - " + msgResult.responseResult.responseContent);
		if (msgResult.isResultOK()) {
			result = true;
		    Log.i("JPushUtils","msgResult - " + msgResult);
		    Log.i("JPushUtils","messageId - " + msgResult.getMessageId());
		} else {
		    if (msgResult.getErrorCode() > 0) {
		        // 业务异常
		        Log.w("JPushUtils","Service error - ErrorCode: "
		                + msgResult.getErrorCode() + ", ErrorMessage: "
		                + msgResult.getErrorMessage());
		    } else {
		        // 未到达 JPush 
		        Log.e("JPushUtils","Other excepitons - "
		                + msgResult.responseResult.exceptionString);
		    }
		}
		
		return result;
	}
}
