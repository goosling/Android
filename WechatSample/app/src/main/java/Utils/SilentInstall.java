package Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by JOE on 2016/3/25.
 * 手机静默安装 具体实现类
 * 需要手机root
 */
public class SilentInstall {
    /**
     * 执行具体的静默安装逻辑，需要手机ROOT。
     * @param apkPath
     *          要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public boolean install(String apkPath) {
        boolean result = false;
        DataOutputStream dos = null;
        BufferedReader errorStream = null;
        try{
            //申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            //执行pm install命令
            String command = "pm install -r" + apkPath+ "\n";
            dos.write(command.getBytes(Charset.forName("utf-8")));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            //读取命令执行结果
            while((line = errorStream.readLine()) != null) {
                msg  += line;
            }
            Log.d("TAG", "install msg is" + msg);
            if(!msg.contains("Failure")) {
                result = true;
            }
        }catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        }finally {
            try{
                if(dos != null) {
                    dos.close();
                }
                if(errorStream != null) {
                    errorStream.close();
                }
            }catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }
}
