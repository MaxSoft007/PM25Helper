package org.maxsoft.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.maxsoft.pm.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateManager {
	//下载中状态
	public static final int DOWNLOAD= 1;
	//下载结束
	public static final int DOWNLOAD_FINISH = 2;
	//保存解析的xml版本信息
	private HashMap<String, String> hashMap = null;
	//下载的保存路径
	private String save_path;
	//下载进度条
	private int progress;
	//是否取消更新
	private boolean isCancle = false;
	
	private Context context = null;
	
	//更新进度条
	private ProgressBar mProgressBar = null;
	private Dialog mDownloadDialog = null;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case DOWNLOAD://正在 下载
				//设置进度条位置
				mProgressBar.setProgress(progress);
				break;
			case DOWNLOAD_FINISH://下载结束
				//安装 文件
				installApk();
				break;
			default:
				break;
			}
		}
	};
	
	public UpdateManager(Context context){
		this.context = context;
	}
	
	ParseXmlService service = new ParseXmlService();
	//检查版本是否有更新
	private boolean isUpdate(){
		int versionCode = service.getVersionCode(context, "org.maxsoft.pm");
		//获取版本文件流
		InputStream inputStream = ParseXmlService.class.getClassLoader().getResourceAsStream("version.xml");
		try {
			hashMap = service.parseXml(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null != hashMap){
			int version = Integer.valueOf(hashMap.get("version"));
			if(version > versionCode){
				return true;
			}
		}
		return false;
	}
	
	//检查版本更新
	public void checkUpdate(){
		if (isUpdate()){
			// 显示提示对话框
			showNoticeDialog();
		} else {
			Toast.makeText(context, "没有新版本", Toast.LENGTH_LONG).show();
		}
	}
	
	private void showNoticeDialog(){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("是否更新");
		builder.setMessage("是否更新版本？");
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//显示下载对话 框
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("稍后更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
	}
    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog()
    {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("开始下载");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgressBar = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton("取消更新", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 设置取消状态
                isCancle = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 下载文件
        downloadApk();
    }
	
	/**
     * 下载apk文件
     */
    private void downloadApk()
    {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }
    
    /**
     * 下载文件线程
     * 
     * @author coolszy
     *@date 2012-4-26
     *@blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    save_path = sdpath + "download";
                    URL url = new URL(hashMap.get("url"));
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(save_path);
                    // 判断文件目录是否存在
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                    File apkFile = new File(save_path, hashMap.get("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        handler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // 下载完成
                        	handler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!isCancle);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    };
    
    
    /**
     * 安装APK文件
     */
    private void installApk()
    {
        File apkfile = new File(save_path, hashMap.get("name"));
        if (!apkfile.exists())
        {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
    }

}
