package com.neu.calander.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neu.calander.Adapter.Bean.Post_Data;
import com.neu.calander.R;
import com.neu.calander.Util.ImageTobit;
import com.nex3z.flowlayout.FlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Post_Adapter extends BaseAdapter {
    private int imagewidth;
    private List<Post_Data> lists = new ArrayList();
    private Activity activity;
    public Post_Adapter(List<Post_Data> lists, Activity context,int imagewidth) {
        this.lists = lists;
        this.activity = context;
        this.imagewidth = imagewidth;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return ((Post_Data)lists.get(i)).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       ViewHolder viewHolder;
       if(view==null){
           viewHolder = new ViewHolder();
           view = LayoutInflater.from(activity).inflate(R.layout.post_listview,viewGroup,false);
           viewHolder.user_image = view.findViewById(R.id.my_image_4);
           viewHolder.username = view.findViewById(R.id.textView37);
           viewHolder.time = view.findViewById(R.id.textView40);
           viewHolder.content = view.findViewById(R.id.textView41);
           viewHolder.flowLayout = view.findViewById(R.id.flowlayout2);
           view.setTag(viewHolder);
       }else{
           viewHolder = (ViewHolder) view.getTag();
       }
       Post_Data post = (Post_Data) getItem(i);
        Log.e("TAG", "getView:"+String.valueOf(i));
       if(post.getUser_image()!=null) {
           try {
               Log.e("TAG","has_userIMage" );
               ImageTobit imageTobit3 = new ImageTobit();
               viewHolder.user_image.setImageBitmap(imageTobit3.StringToBitmap(post.getUser_image()));
           } catch (IOException e) {
               e.printStackTrace();
           }
       }else{
           Log.e("TAG","has_userIMage" );
       }
       viewHolder.username.setText(post.getUser_name());
       viewHolder.time.setText(post.getTime());
       if(post.getContent()!=null){
           if(!post.getContent().equals("null"))
           {
               viewHolder.content.setVisibility(View.VISIBLE);
               viewHolder.content.setText(post.getContent());
           }else{
               viewHolder.content.setVisibility(View.GONE);
           }
           }else {
               viewHolder.content.setVisibility(View.GONE);
           }
       if(post.getImage()!=null){
           viewHolder.flowLayout.removeAllViews();
           int h = post.getImage().size();
           for(String s:post.getImage()){
               if(h==0){
                  break;
               }
               ImageView imageView = new ImageView(activity);
               try {
                   ImageTobit imageTobit = new ImageTobit();
                   imageView.setImageBitmap(imageTobit.StringToBitmap(s));
               } catch (IOException e) {
                   e.printStackTrace();
               }
               imageView.setAdjustViewBounds(true);
               imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
               LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(imagewidth,imagewidth);
               viewHolder.flowLayout.addView(imageView,parms);
               h--;
           }
           viewHolder.flowLayout.setVisibility(View.VISIBLE);
       }else{
           viewHolder.flowLayout.setVisibility(View.GONE);
       }
       return view;
    }

    private class ViewHolder {
        ImageView user_image;
        TextView username;
        TextView time;
        TextView content;
        FlowLayout flowLayout;
    }
}
