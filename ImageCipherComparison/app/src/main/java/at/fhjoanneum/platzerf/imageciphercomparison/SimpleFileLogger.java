package at.fhjoanneum.platzerf.imageciphercomparison;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by platzerf on 20.10.2017.
 */

public class SimpleFileLogger implements Writer {
    private Context context;
    private String currentFile = null;
    private ArrayList<String> logCache = new ArrayList<>();

    public SimpleFileLogger(Context context) {
        this.context = context;

        try {
            File[] extFiles = ContextCompat.getExternalFilesDirs(context, null);

            File extDir = null;
            int i = 0;
            while(!(extDir = extFiles[i++]).exists());

            if(!extDir.exists())
                extDir.mkdirs();

            File gpxfile = new File(extDir.getAbsolutePath(), "imagecipher-" + getCurrentTimeStamp() + ".log");
            currentFile = gpxfile.getAbsolutePath();
            FileOutputStream out = new FileOutputStream(currentFile);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write("Init File\n");
            writer.flush();
            writer.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("Ciphers", "logging failed", e);
            Toast.makeText(context, "failed logger: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void Write(String text){
        logCache.add(getCurrentTimeStamp() + " - " + text + "\n");

        if(logCache.size() > 3)
            Flush();
    }

    public void Flush(){
        if(currentFile == null)
            return;

        try {
            FileOutputStream out = new FileOutputStream(currentFile, true);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            for(int i = 0; i < logCache.size(); i++)
                writer.append(logCache.get(i));
            writer.flush();
            writer.close();
            out.flush();
            out.close();
            logCache.clear();
        } catch (Exception e) {
            Log.e("Ciphers", "logging failed", e);
            Toast.makeText(context, "failed logger: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSS");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
