package com.twosquares.e_mandi;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import static com.twosquares.e_mandi.MainActivity.ip;

/**
 * Created by PRASHANT on 15-11-2016.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    Context context;
    List<RowItem> rowItem;
    int lastPosition = -1;

    public CustomAdapter(Context context, List objects) {
        this.context = context;
        rowItem = objects;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitleView, mPriceView, mDescriptionView;
        public ImageView mImageView;
        public RelativeLayout rl;

        public ViewHolder(View v) {
            super(v);
            rl = (RelativeLayout) v.findViewById(R.id.relativeLayout);
            mTitleView = (TextView) v.findViewById(R.id.titleListing);
            mImageView = (ImageView) v.findViewById(R.id.imgThumbnail);
            mDescriptionView = (TextView) v.findViewById(R.id.descriptionListing);
            mPriceView = (TextView) v.findViewById(R.id.priceListing);
        }

        public void clearAnimation() {

        }
    }


    /*@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.list_item,null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgThumbnail);
        TextView tv = (TextView) convertView.findViewById(R.id.txt);
        Picasso.with(context).load("http://i.imgur.com/" + rowItem.get(position) + "s.jpg").into(imageView);
        return convertView;
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.mTitleView.setText(rowItem.get(position).getTitle());
        holder.mPriceView.setText("₹ " + rowItem.get(position).getPrice());
        holder.mDescriptionView.setText(rowItem.get(position).getDescription());
//        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load("http://" + ip + "/images/reduced/" + rowItem.get(position).getImage_id() + ".jpg").into(holder.mImageView);

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return rowItem.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            animation.setDuration(100 * position);
            viewToAnimate.startAnimation(animation);
         /*   ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(100 * position);//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);*/
            lastPosition = position;
        }

    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.clearAnimation();
    }
}
