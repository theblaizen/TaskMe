package owdienko.jaroslaw.taskme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaroslaw Owdienko on 2/7/2017. All rights reserved TaskMe!
 */

public class ArrayDatabase {
    private static ArrayDatabase data;
    private static List<TaskCollection> collections = new ArrayList<>();

    public static synchronized ArrayDatabase getDataArray() {
        if (data == null)
            data = new ArrayDatabase();
        return data;
    }

    private ArrayDatabase() {

    }

    //Methods are below.


    public TaskCollection getItemByPosition(int position) {
        return collections.get(position);
    }

    public int getArraySize() {
        return collections.size();
    }

    public void addItemToArray(TaskCollection task) {
        collections.add(task);
    }

    public void updateItemInArray(int position, TaskCollection newCollection) {
        collections.set(position, newCollection);
    }

    public void removeItemFromArray(int position) {
        collections.remove(position);
    }

    public void clearAllData() {
        collections.clear();
    }
}
