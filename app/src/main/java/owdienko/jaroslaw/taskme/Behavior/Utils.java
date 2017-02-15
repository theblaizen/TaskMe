package owdienko.jaroslaw.taskme.Behavior;

/**
 * Created by Jaroslaw Owdienko on 2/15/2017. All rights reserved TaskMe!
 */

import android.content.Context;
import android.content.res.TypedArray;

import owdienko.jaroslaw.taskme.R;

public class Utils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }
}
