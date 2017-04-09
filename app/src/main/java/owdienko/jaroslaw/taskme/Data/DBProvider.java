package owdienko.jaroslaw.taskme.Data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jaroslaw Owdienko on 4/4/2017. All rights reserved TaskMe!
 */

public class DBProvider {

    private final DBBackend mDbBackend;
    private final DBNotificationManager mDbNotificationManager;
    private final CustomExecutor mExecutor;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public interface ResultCallback<T> {
        void onFinished(T result);
    }

    DBProvider(Context context) {
        mDbBackend = new DBBackend(context);
        mDbNotificationManager = Container.getNotificationInstance(context);
        mExecutor = new CustomExecutor();
    }

    DBProvider(DBBackend dbBackend,
               DBNotificationManager dbNotificationManager,
               CustomExecutor executor) {
        mDbBackend = dbBackend;
        mDbNotificationManager = dbNotificationManager;
        mExecutor = executor;
    }


    class CustomExecutor extends ThreadPoolExecutor {

        CustomExecutor() {
            super(1, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        }
    }

}
