package com.stu.fei.mobv;

/**
 * Created by Michal on 04.12.2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
//import com.score.senzors.R;
//import com.score.senzors.pojos.Room;

import java.util.ArrayList;

/**
 * Display friend list
 *
 * @author eranga herath(erangaeb@gmail.com)
 */
public class RoomListAdapter extends BaseAdapter implements Filterable {

    public static class Room {
        public String name;

        Room(String name){
            this.name = name;
        }
    }

    private SearchResultsActivity activity;
    private FriendFilter friendFilter;
    private Typeface typeface;
    private ArrayList<Room> roomList;
    private ArrayList<Room> filteredList;

    /**
     * Initialize context variables
     * @param activity friend list activity
     * @param roomList friend list
     */
    public RoomListAdapter(SearchResultsActivity activity, ArrayList<Room> roomList) {
        this.activity = activity;
        this.roomList = roomList;
        this.filteredList = roomList;
//        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/vegur_2.otf");

        getFilter();
    }

    /**
     * Get size of Room list
     * @return userList size
     */
    @Override
    public int getCount() {
        return filteredList.size();
    }

    /**
     * Get specific item from Room list
     * @param i item index
     * @return list item
     */
    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    /**
     * Get Room list item id
     * @param i item index
     * @return current item id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Create list row view
     * @param position index
     * @param view current list item view
     * @param parent parent
     * @return view
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;
        final Room Room = (Room) getItem(position);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.locationText);

//            holder.iconText = (TextView) view.findViewById(R.id.icon_text);
//            holder.name = (TextView) view.findViewById(R.id.friend_list_row_layout_name);
//            holder.iconText.setTypeface(typeface, Typeface.BOLD);
//            holder.iconText.setTextColor(activity.getResources().getColor(R.color.white));
//            holder.name.setTypeface(typeface, Typeface.NORMAL);

            view.setTag(holder);
        } else {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }

        // bind text with view holder content view for efficient use
//        holder.iconText.setText("#");
        holder.name.setText(Room.name);
//        view.setBackgroundResource(R.drawable.friend_list_selector);

        return view;
    }

    /**
     * Get custom filter
     * @return filter
     */
    @Override
    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new FriendFilter();
        }

        return friendFilter;
    }

    /**
     * Keep reference to children view to avoid unnecessary calls
     */
    static class ViewHolder {
        TextView iconText;
        TextView name;
    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<Room> tempList = new ArrayList<Room>();

                // search content in friend list
                for (Room Room : roomList) {
                    if (Room.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(Room);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = roomList.size();
                filterResults.values = roomList;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Room>) results.values;
            notifyDataSetChanged();
        }
    }

}