package owdienko.jaroslaw.taskme;

/**
 * Created by Jaroslaw Owdienko on 2/6/2017. All rights reserved TaskMe!
 */

public class TaskCollection {

    private int _id;
    private String _title;
    private String _content;
    private int _image;

    public TaskCollection(int _id, String _title, String _content, int _image) {
        this._id = _id;
        this._title = _title;
        this._content = _content;
        this._image = _image;
    }

    public TaskCollection(String _title, String _content, int _image) {
        this._title = _title;
        this._content = _content;
        this._image = _image;
    }

    public TaskCollection() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public int get_image() {
        return _image;
    }

    public void set_image(int _image) {
        this._image = _image;
    }

}
