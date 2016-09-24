package com.paya.authomation.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 08/03/2016.
 */
public class ErjaAdapter extends ArrayAdapter<User> {

private ArrayList<User> listuser;
    private ArrayList<User> listuserall;
    private Context context;
    ImageView imguser;
    TextView username;
    CheckBox checkBox;
    private List<User> items;

    private List<User> suggestions;

    public ErjaAdapter(Context context, int resource, ArrayList<User> listuser) {
        super(context, resource, listuser);
        this.context =context;
        this.listuser =listuser;
        this.listuserall = (ArrayList<User>) listuser.clone();
        this.suggestions = new ArrayList<User>();
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }
Filter nameFilter = new Filter() {
    @Override
    public CharSequence convertResultToString(Object resultValue) {
        String str = ((User)(resultValue)).getUserTitle();
        return str;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        if(constraint != null) {
            suggestions.clear();
            for (User user : listuserall) {
                if(user.getUserTitle().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                    suggestions.add(user);
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            return filterResults;
        } else {
            return new FilterResults();
        }
    }


    @Override
    protected void publishResults(CharSequence charSequence, FilterResults results) {
        ArrayList<User> filteredList = (ArrayList<User>) results.values;
        if(results != null && results.count > 0) {
            clear();
            for (User c : filteredList) {
                add(c);
            }
            notifyDataSetChanged();
        }
    }

};
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

      //  ActionMode action = null;
      // action.setTag(position);
        holder = new ViewHolder();
        View RootView = LayoutInflater.from(context).inflate(R.layout.erjauser, parent, false);

        holder.username = (TextView) RootView.findViewById(R.id.username);
        holder.imguser = (ImageView) RootView.findViewById(R.id.imguser);
       // holder.checkBox = (CheckBox) RootView.findViewById(R.id.check);

        holder.user = listuser.get(position);
        holder.username.setText(holder.user.getUserTitle());
        if (holder.user.getStat() == 1) {
            holder.username.setBackgroundResource(R.drawable.btn_check_buttonless_on);
        }
        else{
            holder.username.setBackgroundResource(0);
            holder.user.setStat(0);
        }
        TextDrawable draw = iconGenaretot(position,holder.user);
        holder.imguser.setImageDrawable(draw);

        return RootView;
    }



    class ViewHolder {
        User user;
       TextView username;
        ImageView imguser;
        CheckBox checkBox;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class CheckableLayout extends FrameLayout implements Checkable {
        private boolean mChecked;

        public CheckableLayout(Context context) {
            super(context);
        }

        public void setChecked(boolean checked) {
            mChecked = checked;
            setBackground(
                    getResources().getDrawable(R.drawable.btn_check_buttonless_on, null)
                    );
        }

        public boolean isChecked() {
            return mChecked;
        }

        public void toggle() {
            setChecked(!mChecked);
        }

    }

    public TextDrawable iconGenaretot(int position, User user){
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        String Firstletter;
        String alpha="منتالبیسشک";
        if(user.getUserTitle()!=null) {
            Firstletter = String.valueOf(user.getUserTitle().charAt(0));
        }
        else {
            Random x= new Random();
            Firstletter = String.valueOf(alpha.charAt(x.nextInt(9)));

        }
        TextDrawable drawable = TextDrawable.builder().buildRound(Firstletter, color);
        
        return  drawable;

    }


}
