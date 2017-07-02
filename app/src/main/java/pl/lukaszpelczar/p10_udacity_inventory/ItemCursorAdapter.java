package pl.lukaszpelczar.p10_udacity_inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pl.lukaszpelczar.p10_udacity_inventory.data.ItemContract.ItemEntry;

import static pl.lukaszpelczar.p10_udacity_inventory.data.ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY;

/**
 * {@link ItemCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of item data as its data source. This adapter knows
 * how to create list items for each row of item data in the {@link Cursor}.
 */
public class ItemCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the item data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current item can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        ImageView saleButton = (ImageView)view.findViewById(R.id.sale_button);

        //Get the position before the button is clicked
        final int position = cursor.getPosition();

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Quantity reduced by one!", Toast.LENGTH_SHORT).show();

                cursor.moveToPosition(position);

                int itemIdColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
                final long itemId = cursor.getLong(itemIdColumnIndex);
                Uri mCurrentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, itemId);

                int quantityColumnIndex = cursor.getColumnIndex(COLUMN_ITEM_QUANTITY);

                String itemQuantity = cursor.getString(quantityColumnIndex);

                int updateQuantity = Integer.parseInt(itemQuantity);

                if (updateQuantity > 0) {

                    //Decrease the quantity by 1
                    updateQuantity--;

                    // Defines an object to contain the updated values
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(ItemEntry.COLUMN_ITEM_QUANTITY, updateQuantity);
                    int rowsUpdate = context.getContentResolver().update(mCurrentItemUri,
                            updateValues, null, null);
                } else {
                    Toast.makeText(context, "Quantity is 0 and can't be reduced.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Find the columns of item attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(COLUMN_ITEM_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);

        // Read the item attributes from the Cursor for the current item
        String itemName = cursor.getString(nameColumnIndex);
        int itemQuantity = cursor.getInt(quantityColumnIndex);
        int itemPrice = cursor.getInt(priceColumnIndex);

        String quantityField = "Quantity: " + Integer.toString(itemQuantity);
        String priceField = "Price: " + Integer.toString(itemPrice);

        // Update the TextViews with the attributes for the current item
        nameTextView.setText(itemName);
        quantityTextView.setText(quantityField);
        priceTextView.setText(priceField);

    }

}
