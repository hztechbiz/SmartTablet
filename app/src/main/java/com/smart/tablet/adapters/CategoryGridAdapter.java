package com.smart.tablet.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.tablet.R;
import com.smart.tablet.models.CategoryModel;

import java.util.List;

public class CategoryGridAdapter extends BaseAdapter {

    private View.OnClickListener itemClickListener;
    private List<CategoryModel> categories;
    private Context context;
    private LayoutInflater inflater;

    public CategoryGridAdapter(Context context, List<CategoryModel> categories, View.OnClickListener itemClickListener) {
        this.context = context;
        this.categories = categories;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public CategoryModel getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.category_item, null);
        }

        CategoryModel category = getItem(position);

        LinearLayout box_categories = gridView.findViewById(R.id.bx_category);
        TextView txt_title = gridView.findViewById(R.id.txt_title);
        TextView txt_description = gridView.findViewById(R.id.txt_description);
        ImageView image = gridView.findViewById(R.id.image);

        txt_title.setText(category.getName().toUpperCase());
        txt_description.setText(category.getDescription());

        txt_title.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(context));
        txt_description.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(context));

        box_categories.setTag(R.string.tag_value, category.getId());
        box_categories.setTag(R.string.tag_has_children, (category.getChildren_count() > 0));
        box_categories.setTag(R.string.tag_is_mp, category.isIs_marketing_partner());

        if (category.getEmbed_url() != null && !category.getEmbed_url().isEmpty())
            box_categories.setTag(R.string.tag_embed_url, category.getEmbed_url());

        String image_name = "useful";

        if (category.getImage() != null && !category.getImage().isEmpty()) {
            image_name = category.getImage().replace(".png", "");
        }

        try {
            Resources resources = context.getResources();
            final int resourceId = resources.getIdentifier(image_name, "drawable",
                    context.getPackageName());
            image.setImageDrawable(resources.getDrawable(resourceId));
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
        }

        box_categories.setOnClickListener(itemClickListener);

        return gridView;
    }
}
