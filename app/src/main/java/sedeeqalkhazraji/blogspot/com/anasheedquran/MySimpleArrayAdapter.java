package sedeeqalkhazraji.blogspot.com.anasheedquran;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<String>  {
    private final Context context;
    private final String[] values;
    private boolean[] Islocal;

    public MySimpleArrayAdapter(Context context,  String[] values, boolean[] islocal1) {
        super(context, R.layout.playlist_item, values);
        this.context = context;
        this.values = values;
        this.Islocal = islocal1;

    }

    //Try
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        MyViewHolder holder=null;

        if (rowView == null)
        {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.playlist_item, parent, false);
            holder=new MyViewHolder(rowView);
            rowView.setTag(holder);


        }
        else{
            holder = (MyViewHolder) rowView.getTag();

        }

        holder.songTitle1.setText(values[position]);

        Log.d("position", String.valueOf(position));
        if (Islocal[position+1])
            holder.btn_buy1.setVisibility(View.INVISIBLE);
        return rowView;
    }

    class MyViewHolder {
        ImageButton btn_buy1 ;
        TextView songTitle1;

        MyViewHolder(View v) {
            btn_buy1 = (ImageButton) v.findViewById(R.id.btn_buy);
            songTitle1 = (TextView) v.findViewById(R.id.songTitleNew);
        }
    }


}