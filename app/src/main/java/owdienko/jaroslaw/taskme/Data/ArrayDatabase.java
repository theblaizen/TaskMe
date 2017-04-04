package owdienko.jaroslaw.taskme.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaroslaw Owdienko on 2/7/2017. All rights reserved TaskMe!
 */

public class ArrayDatabase {
    private static ArrayDatabase data;
    private static List<TaskCollection> collections = new ArrayList<>();

    @Deprecated
    public static synchronized ArrayDatabase getDataArray() {
        if (data == null)
            data = new ArrayDatabase();
        return data;
    }

    private ArrayDatabase() {

    }

    //Methods are below.

    @Deprecated
    public TaskCollection getItemByPosition(int position) {
        return collections.get(position);
    }

    @Deprecated
    public int getArraySize() {
        return collections.size();
    }

    @Deprecated
    public void addItemToArray(TaskCollection task) {
        collections.add(task);
    }

    @Deprecated
    public void updateItemInArray(int position, TaskCollection newCollection) {
        collections.set(position, newCollection);
    }

    @Deprecated
    public void removeItemFromArray(int position) {
        collections.remove(position);
    }

    @Deprecated
    public void clearAllData() {
        collections.clear();
    }
}
