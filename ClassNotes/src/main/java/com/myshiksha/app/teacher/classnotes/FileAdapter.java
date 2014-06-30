package com.myshiksha.app.teacher.classnotes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myshiksha.app.teacher.classnotes.utils.CONSTANTS;
import com.myshiksha.app.teacher.classnotes.utils.FileExtensionType;

import java.io.File;
import java.util.List;

/*
   @author seinjuti.chatterjee
 */
public class FileAdapter extends ArrayAdapter<File> {
    private final Context context;
    private List<File> files;

    public FileAdapter(Context context, int resource, List<File> objects) {
        super(context, resource, objects);
        this.context = context;
        this.files = objects;
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    public int getCount() {
        return this.files == null ? 0 : this.files.size();
    }

    public File getItem(int index) {
        return this.files.get(index);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row_file, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.list_row_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_row_image);
        TextView sizeView = (TextView) rowView.findViewById(R.id.list_row_size);

        File currentFile = getItem(position);
        Log.i(CONSTANTS.FILE_ADAPTER_TAG, "For position=" + position + " found file=" + currentFile);

        if (currentFile != null) {
            FileDomainModel model = new FileDomainModel(currentFile);
            nameView.setText(model.getName());
            sizeView.setText(model.getSize());
            FileExtensionType type = model.getType();
            Log.i(CONSTANTS.FILE_ADAPTER_TAG, "File Details=" + model.toString());
            switch (type) {
                case JPEG:
                    imageView.setImageResource(R.drawable.jpg_image);
                    break;
                case XLS:
                    imageView.setImageResource(R.drawable.jpg_image);
                    break;
                case PDF:
                    imageView.setImageResource(R.drawable.jpg_image);
                    break;
                case TXT:
                    imageView.setImageResource(R.drawable.jpg_image);
                    break;
                case DOC:
                    imageView.setImageResource(R.drawable.jpg_image);
                    break;
                case PNG:
                    imageView.setImageResource(R.drawable.jpg_image);
                    break;
                default:
                    break;
            }
        }

        return rowView;
    }

    public boolean hasStableIds() {
        return false;
    }


    public boolean isEmpty() {
        return this.getCount() < 1;
    }


    @Override
    public void notifyDataSetChanged() {
        Log.i(CONSTANTS.FILE_ADAPTER_TAG, "Got files = " + this.files);
    }
}
