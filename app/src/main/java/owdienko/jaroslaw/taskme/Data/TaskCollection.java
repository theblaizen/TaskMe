package owdienko.jaroslaw.taskme.Data;

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

    @Deprecated
    public int get_id() {
        return _id;
    }

    @Deprecated
    public void set_id(int _id) {
        this._id = _id;
    }

    @Deprecated
    public String get_title() {
        return _title;
    }

    @Deprecated
    public void set_title(String _title) {
        this._title = _title;
    }

    @Deprecated
    public String get_content() {
        return _content;
    }

    @Deprecated
    public void set_content(String _content) {
        this._content = _content;
    }

    @Deprecated
    public int get_image() {
        return _image;
    }

    @Deprecated
    public void set_image(int _image) {
        this._image = _image;
    }

}
