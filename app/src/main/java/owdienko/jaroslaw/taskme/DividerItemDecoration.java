package owdienko.jaroslaw.taskme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Jaroslaw Owdienko on 2/26/2017. All rights reserved TaskMe!
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final String TAG = "DebugIssues";
    private Drawable divider;

    //Custom divider will be used
    public DividerItemDecoration(Context context, int resId) {
        divider = ContextCompat.getDrawable(context, resId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //View dvd = parent.findViewById(R.id.content_wrapper);
        int childCount = parent.getChildCount();
        int left = 0;
        if(childCount != 0 && childCount > 1)
            left = parent.findViewById(R.id.row_task).getWidth() - parent.findViewById(R.id.content_wrapper).getWidth();
        int right = parent.getWidth() - parent.getPaddingRight() ; // parent.getWidth() - parent.getPaddingRight() //parent.findViewById(R.id.content_wrapper).getWidth()
        Log.e(TAG, String.valueOf(right) + " -> width");

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            if (i != childCount - 1) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }
}
