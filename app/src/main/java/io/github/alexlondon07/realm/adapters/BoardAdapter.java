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
import io.github.alexlondon07.realm.models.Board;

/**
 * Created by alexlondon07 on 2/23/18.
 */

public class BoardAdapter extends BaseAdapter {

    private Context context;
    private List<Board> boardList;
    private int layout;

    public BoardAdapter(Context context, List<Board> boardList, int layout) {
        this.context = context;
        this.boardList = boardList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Board getItem(int position) {
        return boardList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.title =  convertView.findViewById(R.id.textViewBoardTitle);
            viewHolder.notes =  convertView.findViewById(R.id.textViewBoardNotes);
            viewHolder.createdAt =  convertView.findViewById(R.id.textViewBoardDate);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Board board = boardList.get(position);
        viewHolder.title.setText(board.getTitle());

        int numberOfNumber = board.getNotes().size();
        String textForNotes = (numberOfNumber==1) ? numberOfNumber + " Note " : numberOfNumber + " Notes";
        viewHolder.notes.setText(textForNotes);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String createdAt = df.format(board.getCreateAt());
        viewHolder.createdAt.setText(createdAt);

        return convertView;
    }

    public class ViewHolder{
        TextView title;
        TextView notes;
        TextView createdAt;
    }
}
