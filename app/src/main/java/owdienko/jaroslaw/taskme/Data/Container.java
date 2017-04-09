package owdienko.jaroslaw.taskme.Data;

import android.content.Context;

/**
 * Created by Jaroslaw Owdienko on 4/4/2017. All rights reserved TaskMe!
 */

public class Container {

    private static DBProvider sDbProviderInstance;
    public static DBProvider getProviderInstance(Context context) {
        context = context.getApplicationContext();
        if (sDbProviderInstance == null) {
            sDbProviderInstance = new DBProvider(context);
        }
        return sDbProviderInstance;
    }

    private static DBNotificationManager sDbNotificationInstance;

    public static DBNotificationManager getNotificationInstance(Context context) {
        context = context.getApplicationContext();
        if (sDbNotificationInstance == null) {
            sDbNotificationInstance = new DBNotificationManager();
        }
        return sDbNotificationInstance;
    }

}
