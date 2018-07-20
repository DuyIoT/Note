package assignment.rekkeitrainning.com.note.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoang on 7/17/2018.
 */

public class Note implements Parcelable {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String title;
    String content;
    String date;
    String image;
    String time;
    String alaramDate;
    String alaramTime;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlaramDate() {
        return alaramDate;
    }

    public void setAlaramDate(String alaramDate) {
        this.alaramDate = alaramDate;
    }

    public String getAlaramTime() {
        return alaramTime;
    }

    public void setAlaramTime(String alaramTime) {
        this.alaramTime = alaramTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.date);
        dest.writeString(this.image);
        dest.writeString(this.time);
        dest.writeString(this.alaramDate);
        dest.writeString(this.alaramTime);
    }

    public Note() {
    }

    protected Note(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.date = in.readString();
        this.image = in.readString();
        this.time = in.readString();
        this.alaramDate = in.readString();
        this.alaramTime = in.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
