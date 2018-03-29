package io.github.alexlondon07.realm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import io.github.alexlondon07.realm.R;
import io.github.alexlondon07.realm.models.Note;

/**
 * Created by alexlondon07 on 3/29/18.
 */

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private List<Note> noteList;
    private int layout;


    public NoteAdapter(Context context, List<Note> noteList, int layout) {
        this.context = context;
        this.noteList = noteList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Note getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.description = convertView.findViewById(R.id.textViewNoteDescription);
            vh.createdAt = convertView.findViewById(R.id.textViewNoteCreatedAt);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        Note note = noteList.get(position);
        vh.description.setText(note.getDescription());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(note.getCreatedAt());
        vh.createdAt.setText(date);

        return convertView;
    }

    public class ViewHolder{
        TextView description;
        TextView createdAt;
    }
}
