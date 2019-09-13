package whack.bl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

import whack.activities.MapsActivity;
import whack.activities.R;
import whack.activities.TopTenActivity;

public class PlayerListAdapter extends ArrayAdapter<Player> {
//Written with the help of https://www.youtube.com/watch?v=E6vE8fqQPTE
    private Context context;
    private int resource;

    public PlayerListAdapter(Context context, int resource, List<Player> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        int score = getItem(position).getScore();

        LayoutInflater inflater = LayoutInflater.from(context);

        convertView = inflater.inflate(resource, parent, false); //not best practice

        //get the text view objects from the list_layout- the layout to make the list look better
        TextView nameTextView = convertView.findViewById(R.id.name_text_view);
        TextView scoreTextView = convertView.findViewById(R.id.score_text_view);
        ImageButton img = convertView.findViewById(R.id.location_imageButton);

        nameTextView.setText(name);
        scoreTextView.setText(score + "");
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
