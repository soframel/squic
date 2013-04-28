package org.soframel.android.squic.utils;

import org.soframel.squic.quiz.question.Question;
import org.soframel.squic.utils.SquicLogger;
import org.soframel.squic.utils.URLResourceProvider;
import android.util.Log;
import java.io.*;
import java.util.List;

import android.content.Context;

/**
 * User: sophie.ramel
 * Date: 28/4/13
 * <p/>
 * In case this is a URL dictionary, special initialization includes:
 * - checking if URL exists / is accessible
 * - if so:
 * - download whole file to saved file in fix path
 * - re-read InputStream locally
 * - otherwise: check if a file was saved in fix path, and if so, use it
 */
public class AndroidURLResourceProvider extends URLResourceProvider {

    private static final String TAG = "Squic_AndroidURLResourceProvider";

    private Context context;

    public AndroidURLResourceProvider(Context context) {
        this.context = context;
    }

    @Override
    public InputStream getPropertiesInputStream(String resourceName) {
        InputStream in = super.getPropertiesInputStream(resourceName);

        boolean available = false;
        try {
            if (in!=null && in.available() > 0) {
                available = true;
            }
        } catch (IOException e) {
            available = false;
        }

        String file = this.getBackupFilename(resourceName);

        if (available) {

            try {
                FileOutputStream out = context.openFileOutput(file, Context.MODE_PRIVATE);
                Log.d(TAG, "Copying content of " + resourceName + " to backup file " + file);
                OutputStreamWriter writer = new OutputStreamWriter(out);
                BufferedWriter bwriter = new BufferedWriter(writer);
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader breader = new BufferedReader(reader);
                String line = null;
                try {
                    do {
                        line = breader.readLine();
                        if (line != null)
                            bwriter.write(line+"\n");
                    } while (line != null);
                    breader.close();
                    reader.close();

                    in.close();

                    bwriter.close();
                    writer.close();
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG,"Could not copy the data to backup file: " + e.getMessage(), e);
                }

            } catch (FileNotFoundException e) {
                Log.e(TAG,"Could not create backup file "+file+": " + e.getMessage(), e);
            }
        }
        else
            Log.w(TAG, "URL is not available: "+resourceName);

        try {
            in = context.openFileInput(file);
        } catch (FileNotFoundException e) {
            Log.e(TAG,"URL not available and no backup file - returning null");
            in = null;
        }
        return in;

    }


    private String getBackupFilename(String resourceName) {
        return String.valueOf(resourceName.hashCode());
    }
}
