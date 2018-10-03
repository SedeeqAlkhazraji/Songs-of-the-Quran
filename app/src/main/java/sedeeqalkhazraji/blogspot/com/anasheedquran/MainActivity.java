package sedeeqalkhazraji.blogspot.com.anasheedquran;
// Developed by Sedeeq Al-khazraji
// https://people.rit.edu/sha6709/index.html
// Sedeeq.alkhazrji@gmail.com

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import co.mobiwise.library.InteractivePlayerView;
import co.mobiwise.library.OnActionClickedListener;
import android.media.MediaPlayer.OnCompletionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import sedeeqalkhazraji.blogspot.com.anasheedquran.Information;
public class MainActivity extends Activity implements OnCompletionListener, OnActionClickedListener {

    // Define varibales
    private RecyclerView recyclerView;
    private AnasheedRecyclerViewAdapter adapter;

    public boolean[] Islocal = new boolean[51];
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private ImageButton btnRate;

    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private  MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();;
    private SongsManager songManager;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private int rated = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private InteractivePlayerView mInteractivePlayerView;
    private ImageView PlayimageView;
    private SharedPreferences sharedPreferences;



    public String fileName1;
    private static LayoutInflater inflater = null;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TestDangurosPermision();

        mInteractivePlayerView = (InteractivePlayerView) findViewById(R.id.interactivePlayerView);
        mInteractivePlayerView.setOnActionClickedListener(this);


        //sharedPreferences = getSharedPreferences("anasheedquran", Context.MODE_PRIVATE);
        //currentSongIndex = sharedPreferences.getInt("currentSongIndex", 0);
        Log.d("currentSongIndex", String.valueOf(currentSongIndex));
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        sharedPreferences = getSharedPreferences("anasheedqurannew", Context.MODE_PRIVATE);
        rated = sharedPreferences.getInt("rated", 0);

        final Button btnRatMeFont = (Button) findViewById(R.id.btnRatMeFont);
        btnRatMeFont.setTypeface(font);

        if (rated == 1) {
            btnRatMeFont.setTextColor(Color.RED);
        }
        btnRatMeFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRatMeFont.setTextColor(Color.RED);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=sedeeqalkhazraji.blogspot.com.anasheedquran1"));
                SharedPreferences.Editor editor =  sharedPreferences.edit();
                editor.putInt("rated", 1);
                editor.commit();
                startActivity(intent);
            }
        });

        final Button btnShareME = (Button) findViewById(R.id.btnShareME);
        btnShareME.setTypeface(font);
        btnShareME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnShareME.setTextColor(Color.RED);
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String sAux = getResources().getString(R.string.shareRecommand);
                    sAux = sAux + "https://play.google.com/store/apps/details?id=sedeeqalkhazraji.blogspot.com.anasheedquran1 \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, getResources().getString(R.string.shareMessage)));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });



        // All player buttons
        songTitleLabel = (TextView) findViewById(R.id.songTitleNew);
        btnNext = (ImageButton) findViewById(R.id.btnNextNew);
        btnPrevious = (ImageButton) findViewById(R.id.btnPreviousNew);
        btnForward = (ImageButton) findViewById(R.id.btnForwardNew);
        btnBackward = (ImageButton) findViewById(R.id.btnBackwardNew);
        PlayimageView = (ImageView) findViewById(R.id.btnPlayNew);

        // Mediaplayer
        mp = new MediaPlayer();
        songManager = new SongsManager();


        // Listeners
        mp.setOnCompletionListener(this); // Important

        // Getting all songs list
        String[] AnasheedNames = getResources().getStringArray(R.array.AnasheedNames);
        String[] SongPath = getResources().getStringArray(R.array.SongPath);
        songsList = songManager.getPlayList(AnasheedNames,SongPath);

        // By default play first song
        playSong(currentSongIndex);

        Islocal = songManager.Islocal;

        recyclerView=(RecyclerView) findViewById(R.id.recycler_viewID);

        adapter = new AnasheedRecyclerViewAdapter(MainActivity.this, getSabahData(),Islocal);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        PlayimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mp.isPlaying()) {
                    mInteractivePlayerView.start();
                    mp.start();
                    PlayimageView.setBackgroundResource(R.drawable.ic_action_pause);
                } else {
                    mInteractivePlayerView.stop();
                    mp.pause();
                    PlayimageView.setBackgroundResource(R.drawable.ic_action_play);
                }
            }
        });




        /**
         * Forward button click event
         * Forwards song specified seconds
         * */
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mp.getDuration()){
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
                mInteractivePlayerView.setProgress((int) (mp.getCurrentPosition() * 0.001)); //Milliseconds to Seconds
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mp.seekTo(0);
                }
                mInteractivePlayerView.setProgress((int) (mp.getCurrentPosition() * 0.001)); //Milliseconds to Seconds

            }
        });



        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if(currentSongIndex < (songsList.size() - 1)){
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                }else{
                    // play first song
                    playSong(0);
                    currentSongIndex = 0;
                }

            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(currentSongIndex > 0){
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                }else{
                    // play last song
                    playSong(songsList.size() - 1);
                    currentSongIndex = songsList.size() - 1;
                }

            }
        });



    }

    @Override
    public void onActionClicked(int id) {
        switch (id) {
            case 1://Shaffle Icon
                Log.d("Btn", String.valueOf(id));
                if(isShuffle){
                    isShuffle = false;
                    //Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                }else{
                    // make repeat to true
                    isShuffle= true;
                    //Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                }

                break;
            case 2:
                Log.d("Btn", String.valueOf(id));

                break;
            case 3: //Repete Icon
                Log.d("Btn", String.valueOf(id));
                if(isRepeat){
                    isRepeat = false;
                    //Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                }else{
                    // make repeat to true
                    isRepeat = true;
                    //Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                Log.d("Btn", String.valueOf(id));

                break;
        }
    }





    /**
     * Function to play a song
     * @param songIndex - index of song
     * */
    public void  playSong(int songIndex){
        // Play song
        if (URLUtil.isValidUrl(songsList.get(songIndex).get("songPath"))){
            if (!isAvailable()){
                showAlertDialog(this, getResources().getString(R.string.InternetTitle),getResources().getString(R.string.InternetMessage), false);
                return;
            }
        }

        try {
            mp.reset();
            mInteractivePlayerView.stop();
            //Uri myUri = Uri.parse(songsList.get(songIndex).get("songPath"));
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(songsList.get(songIndex).get("songPath"));

            mp.prepare();
            mp.start();

            // Displaying Song title
            String songTitle = songsList.get(songIndex).get("songTitle");
            songTitleLabel.setText(songTitle);

            // Changing Button Image to pause image
            // DOIT
            //btnPlay.setImageResource(R.drawable.btn_pause);
            PlayimageView.setBackgroundResource(R.drawable.ic_action_pause);

            // set Progress bar values
            Log.d("Length", String.valueOf(mp.getDuration()));
            mInteractivePlayerView.setProgress(0);
            mInteractivePlayerView.setMax((int) (mp.getDuration()* 0.001));//Milliseconds to Seconds
            mInteractivePlayerView.start();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        if(isRepeat){
            // repeat is on play same song again
            playSong(currentSongIndex);
        } else if(isShuffle){
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
        } else{
            // no repeat or shuffle ON - play next song
            if(currentSongIndex < (songsList.size() - 1)){
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }else{
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mp.release();
        //Log.d("currentSongIndex", String.valueOf(currentSongIndex));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }


    public void startDownload(String fileURL, String saveToPath) {
        Toast.makeText(this, fileURL, Toast.LENGTH_SHORT).show();

        String url = fileURL;
        fileName1 = saveToPath;
        new MainActivity.DownloadFileAsync().execute(url);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(fileName1);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                total = 0;
                publishProgress(""+(int)((total*100)/lenghtOfFile));
                //output.write(data, 0, count);
            } catch (Exception e) {}

            return null;

        }
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }

    public List<Information> getSabahData(){
        List<Information> data =    new ArrayList<>();
        Resources res = getResources();
        String[] IntroSongName = res.getStringArray(R.array.IntroSongName);
        String[] SignerName = res.getStringArray(R.array.SignerName);
        String[] SongPath = res.getStringArray(R.array.SongPath);
        int[] SongCount=  res.getIntArray(R.array.SongID);
        for (int    i=0; i< IntroSongName.length; i++)
        {
            Information current = new Information();
            current.txtIntroSongName = IntroSongName[i];
            current.txtSignerName = SignerName[i];
            current.txtSongPath = SongPath[i];
            current.txtCount = SongCount[i];
            data.add(current);
        }
        return data;
    }

    //get access
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @TargetApi(Build.VERSION_CODES.M)
    void TestDangurosPermision ()
    {
        //check if the API>=23 to display runtime request permison
        if ((int) Build.VERSION.SDK_INT >= 23)
        {


            // check if this permission is not grated yet
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) !=
                    PackageManager.PERMISSION_GRANTED)||
                    (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) !=
                            PackageManager.PERMISSION_GRANTED))
            {
                //shouldShowRequestPermissionRationale(). This method returns true
                // if the app has requested this permission previously and the user denied the request.
                //if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // display request permission
                requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW,
                                Manifest.permission.ACCESS_NETWORK_STATE},
                        REQUEST_CODE_ASK_PERMISSIONS);

                return   ;

            }

            return  ;
        }
    }

    //Check internet connection
    public Boolean isAvailable() {

        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1    www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            if(reachable){
                System.out.println("Internet access");
                return true;
            }
            else{
                System.out.println("No Internet access");
                return false;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }


    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
