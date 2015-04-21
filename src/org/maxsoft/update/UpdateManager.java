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
	//������״̬
	public static final int DOWNLOAD= 1;
	//���ؽ���
	public static final int DOWNLOAD_FINISH = 2;
	//���������xml�汾��Ϣ
	private HashMap<String, String> hashMap = null;
	//���صı���·��
	private String save_path;
	//���ؽ�����
	private int progress;
	//�Ƿ�ȡ������
	private boolean isCancle = false;
	
	private Context context = null;
	
	//���½�����
	private ProgressBar mProgressBar = null;
	private Dialog mDownloadDialog = null;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case DOWNLOAD://���� ����
				//���ý�����λ��
				mProgressBar.setProgress(progress);
				break;
			case DOWNLOAD_FINISH://���ؽ���
				//��װ �ļ�
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
	//���汾�Ƿ��и���
	private boolean isUpdate(){
		int versionCode = service.getVersionCode(context, "org.maxsoft.pm");
		//��ȡ�汾�ļ���
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
	
	//���汾����
	public void checkUpdate(){
		if (isUpdate()){
			// ��ʾ��ʾ�Ի���
			showNoticeDialog();
		} else {
			Toast.makeText(context, "û���°汾", Toast.LENGTH_LONG).show();
		}
	}
	
	private void showNoticeDialog(){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("�Ƿ����");
		builder.setMessage("�Ƿ���°汾��");
		builder.setPositiveButton("����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//��ʾ���ضԻ� ��
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("�Ժ����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
	}
    /**
     * ��ʾ������ضԻ���
     */
    private void showDownloadDialog()
    {
        // ����������ضԻ���
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("��ʼ����");
        // �����ضԻ������ӽ�����
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgressBar = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // ȡ������
        builder.setNegativeButton("ȡ������", new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // ����ȡ��״̬
                isCancle = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // �����ļ�
        downloadApk();
    }
	
	/**
     * ����apk�ļ�
     */
    private void downloadApk()
    {
        // �������߳��������
        new downloadApkThread().start();
    }
    
    /**
     * �����ļ��߳�
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
                // �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // ��ô洢����·��
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    save_path = sdpath + "download";
                    URL url = new URL(hashMap.get("url"));
                    // ��������
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // ��ȡ�ļ���С
                    int length = conn.getContentLength();
                    // ����������
                    InputStream is = conn.getInputStream();

                    File file = new File(save_path);
                    // �ж��ļ�Ŀ¼�Ƿ����
                    if (!file.exists())
                    {
                        file.mkdir();
                    }
                    File apkFile = new File(save_path, hashMap.get("name"));
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // ����
                    byte buf[] = new byte[1024];
                    // д�뵽�ļ���
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // ���������λ��
                        progress = (int) (((float) count / length) * 100);
                        // ���½���
                        handler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // �������
                        	handler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // д���ļ�
                        fos.write(buf, 0, numread);
                    } while (!isCancle);// ���ȡ����ֹͣ����.
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
            // ȡ�����ضԻ�����ʾ
            mDownloadDialog.dismiss();
        }
    };
    
    
    /**
     * ��װAPK�ļ�
     */
    private void installApk()
    {
        File apkfile = new File(save_path, hashMap.get("name"));
        if (!apkfile.exists())
        {
            return;
        }
        // ͨ��Intent��װAPK�ļ�
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
    }

}
