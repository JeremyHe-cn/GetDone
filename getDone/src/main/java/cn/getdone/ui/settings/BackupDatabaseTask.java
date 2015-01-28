package cn.getdone.ui.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;

import me.jeremyhe.lib.androidutils.ToastUtils;
import me.jeremyhe.lib.common.DateUtils;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

/**
 * 用于数据库备份的
 * @author JeremyHe
 */
public class BackupDatabaseTask extends AsyncTask<String, Void, Boolean> {

	public static final String COMMAND_BACKUP = "backupDatabase";
	public static final String COMMAND_RESTORE = "restroeDatabase";
	
	private Context mContext;
	private final String DB_NAME = "getdone-db";
	
	public BackupDatabaseTask(Context context) {
		this.mContext = context;
	}

	@SuppressWarnings("resource")
	private void fileCopy(File dbFile, File backup) throws IOException {
		FileChannel inChannel = new FileInputStream(dbFile).getChannel();
		FileChannel outChannel = new FileOutputStream(backup).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String dbFileName = Environment.getDataDirectory().getAbsolutePath()+ "/data/cn.getdone/databases/"+ DB_NAME;
		File dbFile = mContext.getDatabasePath(dbFileName);
		File exportDir = new File(Environment.getExternalStorageDirectory(),"/getdone/backup");
		if (!exportDir.exists()) {
			exportDir.mkdirs();
		}
		
		String command = params[0];
		
		if (command.equals(COMMAND_BACKUP)) {
			try {
				String backupFileName = DateUtils.format(new Date(), DateUtils.format_yyyy_MM_dd)+".backup";
				File backupFile = new File(exportDir, backupFileName);

				backupFile.createNewFile();
				fileCopy(dbFile, backupFile);
			} catch (Exception e) {
				return false;
			}
		} else if (command.equals(COMMAND_RESTORE)) {
			try {
				String backupFileName = params[1];
				File backupFile = new File(exportDir, backupFileName);

				fileCopy(backupFile, dbFile);
			} catch (Exception e) {
				return false;
			}
		} 
		
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			ToastUtils.showLongToast(mContext, "备份或恢复成功!");
		} else {
			ToastUtils.showLongToast(mContext, "备份或恢复失败!");
		}
		super.onPostExecute(result);
	}

}
