package owdienko.jaroslaw.taskme.Data;

/**
 * Created by Jaroslaw Owdienko on 4/3/2017. All rights reserved TaskMe!
 */

public interface DBContract {

    String DB_NAME = "tasks.db";

    String TASKS = "tasks";

    interface Tasks {
        String TASKS_ID = "rowid";
        String TASKS_TITLE = "task_title";
        String TASKS_CONTENT = "task_content";
        String TASKS_DATE = "task_date";
        String TASKS_IMAGE = "task_image";
    }

    String IMAGES = "images";

    interface Images {
        String IMAGES_ID = "images_id";
        String IMAGES_RESOURCE = "images_";
    }

}
