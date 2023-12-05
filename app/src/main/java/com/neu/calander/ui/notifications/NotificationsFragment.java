package com.neu.calander.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.neu.calander.Util.ImageTobit;
import com.neu.calander.Pojo.User;
import com.neu.calander.R;
import com.neu.calander.Service.SQL_SERVICE;
import com.neu.calander.Util.tool;
import com.neu.calander.ui.User_setting;
import com.neu.calander.ui.Login;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private ImageView imageView;
    private static Uri uri;
    private User user = User.getSingle_instance();
    private  TextView textView;
    private TextView text_id;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout1;
    private ImageView imageView2;
    private TextView like;
    private TextView foucs;
    private TextView fans;
    private TextView sign;
    private TextView textView1;
    private TextView textView2;
    private LinearLayout linearLayout2;
    @SuppressLint("WrongConstant")
    @Override
    public void onStart() {
        super.onStart();
        tool.getSingle_instance().setShow(3);
        user = User.getSingle_instance();
        Log.e(TAG, user.toString() );
        if (User.getSingle_instance().getName() == null) {
            textView.setText("未登入");
        } else {
            textView.setText(User.getSingle_instance().getName());
        }
        if(user.isIs_login() &&user.getId()!=null) {
            if(User.getSingle_instance().getSign()!=null){
                sign.setText(User.getSingle_instance().getSign());
            }
            linearLayout2.setVisibility(View.VISIBLE);
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), User_setting.class);
                    startActivityForResult(intent, 1);
                }
            });

            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQL_SERVICE sql_service = new SQL_SERVICE();
                    sql_service.quit_login(getContext());
                    User.getSingle_instance().quit();
                    getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.nav_host_fragment,new NotificationsFragment()).commit();
                    TextView textViewa = getActivity().findViewById(R.id.user_name);
                    textViewa.setText("未登入");
                    ImageView imageViewa = getActivity().findViewById(R.id.my_image);
                    imageViewa.setImageResource(R.drawable.ic_action_name2);
                }
            });
            text_id.setText(user.getId());
            like.setText(String.valueOf(user.getLike()));
            foucs.setText(String.valueOf(user.getFocus()));
            fans.setText(String.valueOf(user.getFans()));
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.GONE);
            if (user.getIcon() != null) {
                try {
                    imageView.setImageBitmap(ImageTobit.StringToBitmap(User.getSingle_instance().getIcon()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), User_setting.class);
                    startActivity(intent);
                }
            });
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), User_setting.class);
                    startActivity(intent);
                }
            });
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), User_setting.class);
                    startActivity(intent);
                }
            });

        }else{
            linearLayout2.setVisibility(View.GONE);
            linearLayout.setVisibility(8);
            linearLayout1.setVisibility(0);
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivityForResult(intent,1);
                }
            });
        }

    }

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tool.getSingle_instance().setShow(3);
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        TextView toolbar_textview = getActivity().findViewById(R.id.toolbar_title);
        toolbar_textview.setVisibility(View.GONE);
        textView = root.findViewById(R.id.user_text1);
        text_id = root.findViewById(R.id.textView2);
        linearLayout = root.findViewById(R.id.user_has_login);
        linearLayout1 = root.findViewById(R.id.user_has_not_login);
        imageView = root.findViewById(R.id.my_image_1);
        imageView2 = root.findViewById(R.id.my_image_2);
        like = root.findViewById(R.id.textView4);
        sign = root.findViewById(R.id.textView3);
        foucs = root.findViewById(R.id.textView5);
        fans = root.findViewById(R.id.textView6);
         textView1 = root.findViewById(R.id.tt2);
        linearLayout2 = root.findViewById(R.id.linear1);
         textView2 = root.findViewById(R.id.tt3);
        return root;
    }



}