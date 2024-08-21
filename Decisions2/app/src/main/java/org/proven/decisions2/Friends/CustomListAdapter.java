package org.proven.decisions2.Friends;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import org.proven.decisions2.R;

import java.util.ArrayList;
import java.util.List;


public class CustomListAdapter extends ArrayAdapter<String> implements Filterable {

    private final Activity context;
    private final List<String> values;

    private List<String> filteredValues;

    private CustomFilter filter;
    private final int layoutResourceId;


    /**
     * The provided code shows the constructor of a class called CustomListAdapter. It is used to initialize the adapter with the necessary data and
     * resources for displaying a custom list in an Android ListView or similar view.
     *
     * Here's what each parameter and initialization does in the constructor:
     *
     * Activity context: It represents the activity context in which the adapter is being used. It is typically passed as this from the calling activity.
     * List<String> values: It is the list of values or data that will be displayed in the list.
     * int layoutResourceId: It is the resource ID of the layout file that defines the appearance of each item in the list.
     * Initialization:
     * super(context, layoutResourceId, values): This calls the parent class constructor of ArrayAdapter and passes the context, layout resource ID,
     * and values to initialize the adapter.
     *
     * this.context = context: Assigns the provided activity context to the context variable of the adapter.
     * this.values = values: Assigns the provided list of values to the values variable of the adapter.
     * this.layoutResourceId = layoutResourceId: Assigns the provided layout resource ID to the layoutResourceId variable of the adapter.
     * this.filteredValues = values: Initializes the filteredValues variable with the same list of values. This will be used to store the filtered results when filtering is applied.
     * this.filter = new CustomFilter(): Initializes the filter variable with a new instance of a custom filter class (CustomFilter).
     * The filter is used to perform filtering operations on the data in the list.
     * @param context
     * @param values
     * @param layoutResourceId
     */
    public CustomListAdapter(Activity context, List<String> values, int layoutResourceId) {
        super(context, layoutResourceId, values);
        this.context = context;
        this.values = values;
        this.layoutResourceId = layoutResourceId;
        this.filteredValues = values;
        this.filter = new CustomFilter();
    }

    /**
     * The provided code shows the getView method override, which is a crucial part of the CustomListAdapter class.
     * This method is responsible for creating and populating the individual views for each item in the list.
     *
     * Here's what the code does:
     *
     * It retrieves the LayoutInflater from the context of the adapter.
     * It initializes a rowView variable to hold the view for the current item in the list.
     * If convertView is null, indicating that there is no recycled view available, it inflates the appropriate layout based on the value of layoutResourceId.
     * If layoutResourceId matches R.layout.list_item_add, it inflates the list_item_add layout.
     * If layoutResourceId matches R.layout.list_item_remove, it inflates the list_item_remove layout.
     * If layoutResourceId matches R.layout.list_item_request, it inflates the list_item_request layout.
     * If layoutResourceId matches R.layout.list_item_send, it inflates the list_item_send layout.
     * If none of the above matches, it inflates the list_item_play layout.
     * If convertView is not null, it reuses the existing view by assigning convertView to rowView.
     * It finds the TextView with the ID tUsername in the rowView.
     * It sets the text of the tUsername TextView to the corresponding value from the filteredValues list at the current position.
     * It returns the populated rowView as the view for the current item.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView;
        if (convertView == null) {
            // Inflate the corresponding layout according to the value of layoutResourceId
            if (layoutResourceId == R.layout.list_item_add) {
                rowView = inflater.inflate(R.layout.list_item_add, null, true);
            } else if (layoutResourceId == R.layout.list_item_remove) {
                rowView = inflater.inflate(R.layout.list_item_remove, null, true);
            } else if (layoutResourceId == R.layout.list_item_request){
                rowView = inflater.inflate(R.layout.list_item_request, null, true);
            }else if (layoutResourceId == R.layout.list_item_send){
                rowView = inflater.inflate(R.layout.list_item_send, null, true);
            }else{
                rowView = inflater.inflate(R.layout.list_item_play, null, true);
            }
        } else {
            rowView = convertView;
        }

        // Assign the corresponding values to the views in the layout
        TextView tUsername = (TextView) rowView.findViewById(R.id.tUsername);
        tUsername.setText(filteredValues.get(position));

        return rowView;
    }

    /**
     * The code provided includes three additional overridden methods in the CustomListAdapter class:
     *
     * 1 --> getCount(): This method returns the total number of items in the filtered list (filteredValues).
     * It is used to determine the size of the list and the number of views to be displayed.
     *
     * The implementation simply returns the size of the filteredValues list using the size() method.
     *
     * 2 --> getItem(int position): This method returns the data item at the specified position in the filtered list (filteredValues).
     * It is used to retrieve a specific item from the list when needed.
     *
     * The implementation retrieves the item from the filteredValues list at the given position using the get(position) method and returns it as a String.
     *
     * 3 --> getFilter(): This method returns the Filter object associated with the adapter.
     * The filter is responsible for performing filtering operations on the data in the list based on user input.
     *
     * The implementation simply returns the filter instance that was initialized in the constructor.
     * @return
     */

    @Override
    public int getCount() {
        return filteredValues.size();
    }

    @Override
    public String getItem(int position) {
        return filteredValues.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    /**
     * The code provided includes a nested class called CustomFilter, which extends the Filter class.
     * This custom filter is responsible for performing filtering operations on the data in the CustomListAdapter based on user input.
     *
     * Here's what each method in CustomFilter does:
     *
     * 1 ----> performFiltering(CharSequence constraint): This method is executed in a background thread and performs the actual filtering of the data based on the provided constraint.
     *
     * In this method:
     *
     * A new FilterResults object is created to hold the filtering results.
     * It checks if the constraint parameter is not null and has a length greater than 0.
     * If the constraint is valid, filtering is performed.
     * The constraint is converted to uppercase using toString().toUpperCase().
     * A new filteredList is created to store the filtered results.
     * The values list (the original unfiltered list) is iterated, and each item is compared with the filter string.
     * If an item starts with the filter string (case-insensitive match), it is added to the filteredList.
     * If the constraint is null or empty, no filtering is performed, and the values list remains unchanged.
     * The final filtered results are assigned to results.values, and the count of filtered items is assigned to results.count.
     * The results object is returned.
     *
     * 2 ----> publishResults(CharSequence constraint, FilterResults results): This method is executed on the UI thread after performFiltering completes.
     * It updates the adapter with the filtered results.
     *
     * In this method:
     *
     * The values in the adapter are updated with the filtered values from results.values.
     * The adapter is notified of the data set change using notifyDataSetChanged(). This triggers the getView method to be called again to reflect the updated filtered data.
     */
    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                String filterString = constraint.toString().toUpperCase();
                List<String> filteredList = new ArrayList<>();
                for (String value : values) {
                    if (value.toUpperCase().startsWith(filterString)) {
                        filteredList.add(value);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            } else {
                results.count = values.size();
                results.values = values;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredValues = (List<String>) results.values;
            notifyDataSetChanged();
        }
    }
}