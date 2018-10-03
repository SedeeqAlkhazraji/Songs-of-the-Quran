package sedeeqalkhazraji.blogspot.com.anasheedquran;
// Created by Sedeeq Al-khazraji
// https://people.rit.edu/sha6709/index.html
// Sedeeq.alkhazrji@gmail.com

import android.content.Context;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class AnasheedRecyclerViewAdapter extends RecyclerView.Adapter <AnasheedRecyclerViewAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();
    Context context;
    private InterstitialAd interstitial;
    private boolean DoubleAds;
    private boolean[] MyIslocal;


    public AnasheedRecyclerViewAdapter(Context context, List<Information> data,boolean[] Islocal )
    {
        inflater=LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        this.MyIslocal = Islocal;

    }

    public void Play (int position){
        ((MainActivity) context).playSong(position);
        //Toast.makeText(context, position+ "", Toast.LENGTH_SHORT).show();

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.anasheedquran_recycler_view_row, parent, false);
        MyViewHolder holder =  new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Information current = data.get(position);

        if (this.MyIslocal[position])
        {
            holder.txtCount.setBackgroundResource(R.drawable.ic_clear);
        } else {
            holder.txtCount.setBackgroundResource(R.drawable.baseline_save_alt_white_48);

        }


        holder.txtAzkarIntro.setText(current.txtIntroSongName);
        holder.txtAzkarIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Play(position);
            }
        });
        holder.txtSignerName.setText(current.txtSignerName);
        holder.txtSignerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Play(position);

            }
        });
        holder.txtSongPath.setText(current.txtSongPath);
        holder.txtSongPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Play(position);
            }
        });
        holder.txtCount.setPaintFlags(holder.txtCount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //holder.txtCount.setText("" + current.txtCount);
        holder.txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Play(position);
                Toast.makeText(context, holder.txtSongPath.getText().toString()+ "", Toast.LENGTH_SHORT).show();




                String saveToPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + position + ".mp3";
                //String URLPATH  = "https://archive.org/download/QuranAnasheed_201501/";

                File file = new File(saveToPath);
                if(file.exists()) {
                    Toast.makeText(context, "File already exsist. Deleting ..." + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    file.delete();
                    MyIslocal[position] = false;
                    holder.txtCount.setBackgroundResource(R.drawable.baseline_save_alt_white_48);

                }
                else{
                    Toast.makeText(context, "Download start", Toast.LENGTH_SHORT).show();
                    ((MainActivity) context).startDownload(holder.txtSongPath.getText().toString(), saveToPath);
                    holder.txtCount.setBackgroundResource(R.drawable.ic_clear);
                    MyIslocal[position] = true;
                    }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtAzkarIntro;
        TextView txtSignerName;
        TextView txtSongPath;
        TextView txtCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtAzkarIntro= (TextView) itemView.findViewById(R.id.textAzkarIntro);
            txtSignerName = (TextView) itemView.findViewById(R.id.textAzkarText);
            txtSongPath = (TextView) itemView.findViewById(R.id.textAzkarSummery);
            txtCount = (TextView) itemView.findViewById(R.id.textAzkarCount);
        }

    }
    //Keep it for later use
    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
