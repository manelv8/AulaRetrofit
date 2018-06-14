package tech.sobre.aularetrofit;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter<Repo> {

    Activity ctx;
    List<Repo> list;
    int resource;
    public MyAdapter(Activity ctx,List<Repo> objects){
        this(ctx,R.layout.simple_list_item,objects);
        this.ctx = ctx;
        this.list = objects;
        this.resource = R.layout.simple_list_item;
    }

    public MyAdapter(@NonNull Context context, int resource, @NonNull List<Repo> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = null;

        row = ctx.getLayoutInflater().inflate(resource,parent,false);

        TextView tvRepoName = row.findViewById(R.id.tvRepoName);

        Repo item = list.get(position);

        tvRepoName.setText(item.getName());


        return  row;
    }
}
