package com.paya.authomation.main;

import android.content.Context;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomDrawerAdapter extends RecyclerView.Adapter<CustomDrawerAdapter.ViewHolder>  implements View.OnClickListener {
    private Context context;
   ArrayList<String> titles;
    TypedArray icons;
    private static ClickListener clickListener;
   int position;
    private String username;






    public CustomDrawerAdapter(ArrayList<String> titles, TypedArray icons, String username, Context context) {

        this.titles = titles;
        this.icons = icons;
        this.context = context;
        this.username = username;

    }

    @Override
    public void onClick( View view) {
        clickListener.onItemClick(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView navTitle, navSep, navheader;
        ImageView navIcon, navIsep;
        Context context;

        public ViewHolder(View drawerItem, int itemType, Context context) {

            super(drawerItem);
            this.context = context;


            if (itemType == 2) {
                navTitle = (TextView) itemView.findViewById(R.id.tv_NavTitle);
                navIcon = (ImageView) itemView.findViewById(R.id.iv_NavIcon);


            }
            if(itemType ==1){
                navSep = (TextView) itemView.findViewById(R.id.tv_NavSep);
                navIsep = (ImageView) itemView.findViewById(R.id.iv_NavIsep);
            }
           if(itemType==0){
               navheader = (TextView) itemView.findViewById(R.id.header);
           }
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {

            LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(viewType ==2){
                View itemLayout =   layoutInflater.inflate(R.layout.draweritem,null);
                itemLayout.setOnClickListener(this);

                return new ViewHolder(itemLayout,viewType,context);
            }
            else if(viewType ==1){
                View itemsSeperator = layoutInflater.inflate(R.layout.defaultitem, null);
                itemsSeperator.setOnClickListener(this);

                return  new ViewHolder(itemsSeperator, viewType,context);
            }
            else if (viewType==0) {
                View itemHeader = layoutInflater.inflate(R.layout.header,null);
                return new ViewHolder(itemHeader,viewType,context);
            }



            return null;
        }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position!=0){
            if(position>4) {

                holder.navTitle.setText(titles.get(position - 1));
                holder.navIcon.setImageResource(R.drawable.ic_dialog_email);
            }
            if((position>0 && position<4)){
                holder.navSep.setText(titles.get(position-1));
                holder.navIsep.setImageResource(icons.getResourceId(position-1,-1));

            }
        }
        else{
            holder.navheader.setText(username);
        }
    }



    @Override
    public int getItemViewType(int position) {
      if (position==0)return 0;
      if(position>0 && position<4){
          return 1;
      }
        else return 2;
}

    @Override
    public int getItemCount() {
        return titles.size()+1;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
       CustomDrawerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick( View view);
    }


}