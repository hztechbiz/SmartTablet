package com.smart.tablet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smart.tablet.R;
import com.smart.tablet.helpers.ImageHelper;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.models.LanguageModel;
import com.smart.tablet.models.ServiceModel;

import java.util.List;

public class LanguagesListAdapter extends BaseAdapter {

    private final View.OnClickListener itemClickListener;
    private List<LanguageModel> languages;
    private Context context;
    private LayoutInflater inflater;

    public LanguagesListAdapter(Context context, List<LanguageModel> languages, View.OnClickListener itemClickListener) {
        this.context = context;
        this.languages = languages;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return languages.size();
    }

    @Override
    public LanguageModel getItem(int position) {
        return languages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.language_list_item, null);
        }

        LanguageModel languageModel = getItem(position);

        TextView name = view.findViewById(R.id.txt_name);
        ImageView icon = view.findViewById(R.id.img_icon);

        name.setText(languageModel.getName());
        icon.setImageDrawable(context.getDrawable(context.getResources().getIdentifier(languageModel.getIcon(), "drawable", context.getPackageName())));

        view.setOnClickListener(itemClickListener);
        view.setTag(languageModel.getValue());

        return view;
    }
}
