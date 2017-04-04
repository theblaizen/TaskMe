package owdienko.jaroslaw.taskme.Data;

import android.os.Handler;
import android.os.Looper;

import java.util.HashSet;

/**
 * Created by Jaroslaw Owdienko on 4/4/2017. All rights reserved TaskMe!
 */

public class DBNotificationManager {
    private HashSet<Listener> taskListeners = new HashSet<>();
    private Handler taskHandler = new Handler(Looper.getMainLooper());
    private Runnable taskNotifyRunnable = new Runnable() {
        @Override
        public void run() {
            notifyOnUiThread();
        }
    };

    public interface Listener {
        void onDataUpdated();
    }

    public void addListener(Listener listener) {
        taskListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        taskListeners.remove(listener);
    }

    void notifyListeners() {
        taskHandler.removeCallbacks(taskNotifyRunnable);
        taskHandler.post(taskNotifyRunnable);
    }

    private void notifyOnUiThread() {
        for (Listener l : taskListeners) l.onDataUpdated();
    }
}
