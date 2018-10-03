package sedeeqalkhazraji.blogspot.com.anasheedquran;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    public boolean[] Islocal = new boolean[51];
    private final Context context = null;
    // Constructor
	public SongsManager(){

	}
	
	public ArrayList<HashMap<String, String>> getPlayList(String[] myAnasheedNames,String[] mySongPath){

        HashMap<String, String> song = new HashMap<String, String>();
        String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        //String URLPATH  = "https://archive.org/download/QuranAnasheed_201501/";
        //String[] SongPath = getResources().getStringArray(R.array.SongPath);

        for (int i = 1; i < myAnasheedNames.length +1; i++) {
            song    = new HashMap<String, String>();
            song.put("songTitle",myAnasheedNames[i-1]  );
            File file = new File(PATH + i + ".mp3");
            if(file.exists()) {
                song.put("songPath", file.getAbsolutePath());
                Islocal[i]=true;
            }
            else {
                //song.put("songPath", URLPATH + i + ".mp3");
                song.put("songPath",mySongPath[i-1]);
                Islocal[i]=false;
            }
            songsList.add(song);
        }

        // return songs list array
		return songsList;
	}


}
