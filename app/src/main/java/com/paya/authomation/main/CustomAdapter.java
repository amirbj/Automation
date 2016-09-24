package com.paya.authomation.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 07/04/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.mViewHolder> implements View.OnClickListener {


    private static List<Letters> itemlist;
    private List<Letters> items;
    private String code;
    private Context context;
    private static ClickListener clickListener;
private static String id;


    public CustomAdapter(List<Letters> itemlist, Context context, String id) {
        this.itemlist = itemlist;
        this.code = code;
        this.context = context;
        this.id =id;


    }


    public static Letters getItem(int position) {
        return itemlist != null ? itemlist.get(position) : null;
    }

    @Override
    public void onClick(View view) {
clickListener.onItemClick(view);
    }


    public static class mViewHolder extends RecyclerView.ViewHolder  {

        public TextView Sender, Subject, RecieveAt;
        LinearLayoutCompat xt;
        ImageView attachment, icon;
       // int position = getAdapterPosition();

        private Letters item;
        Context context;
        public mViewHolder(View itemView,Context context ) {
            super(itemView);




            Sender = (TextView) itemView.findViewById(R.id.tvSender);
            Subject = (TextView) itemView.findViewById(R.id.tvSubject);
            RecieveAt = (TextView) itemView.findViewById(R.id.tvdate);
            attachment = (ImageView) itemView.findViewById(R.id.attachment);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }






    }


    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Itemview = layoutInflater.inflate(R.layout.kartabl, parent, false);
        Itemview.setOnClickListener(this);

        return new mViewHolder(Itemview, context);
    }



    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
       holder.item = getItem(position);
        Letters letters = itemlist.get(position);
      //  for(Letters s:itemlist)
//        Log.e("Custome adapter",s.getSender() );
     holder.itemView.setTag(position);

        holder.Sender.setText(letters.getSender());
        holder.Subject.setText(letters.getSubject());
        Calendar cal = new Calendar();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ");
        try {
            Date daterecieve = format.parse(letters.getRecieveAt());

            Calendar.SolarCalendar sc = cal.new SolarCalendar(daterecieve);

            Date current = new GregorianCalendar().getTime();
            Log.e("current", String.valueOf(current));
            int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
            int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
            int day = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH);

            if(daterecieve.before(current) && (Integer.parseInt(letters.getRecieveAt().toString().substring(2,4))== Integer.parseInt(current.toString().substring(25)))){
                holder.RecieveAt.setText(sc.date+" "+sc.strMonth);
            }
            else if(daterecieve.before(current) && (Integer.parseInt(letters.getRecieveAt().toString().substring(2, 4)) < Integer.parseInt(current.toString().substring(25)))){
                holder.RecieveAt.setText(sc.date+" "+sc.strMonth+" "+sc.year);

            }
            else if(daterecieve.equals(current)){
                holder.RecieveAt.setText(daterecieve.toString().substring(11,16));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }



        if(letters.getUnread()){
            holder.Subject.setTypeface(null, Typeface.BOLD);
            holder.RecieveAt.setTextColor(Color.BLUE);
        }
        if(letters.getHasAttachment()){
            holder.attachment.setImageResource(R.drawable.ic_menu_attachment);
        }
        TextDrawable draw = iconGenaretot(position,letters);
        holder.icon.setImageDrawable(draw);



    }

     public TextDrawable iconGenaretot(int position,Letters letters){
         ColorGenerator generator = ColorGenerator.MATERIAL;
         int color = generator.getRandomColor();
         String Firstletter;
         String alpha="منتالبیسشک";
         if(letters.getSender()!=null) {
             Firstletter = String.valueOf(letters.getSender().charAt(0));
         }
         else {
             Random x= new Random();
             Firstletter = String.valueOf(alpha.charAt(x.nextInt(9)));

         }
         TextDrawable drawable = TextDrawable.builder().buildRound(Firstletter, color);
         return  drawable;

     }

    @Override
    public int getItemCount() {
        return itemlist != null ? itemlist.size() : 0;
    }



    public void setOnItemClickListener(ClickListener clickListener) {
        CustomAdapter.clickListener = clickListener;
    }

    public interface ClickListener{
        void onItemClick(View view);
    }




}
