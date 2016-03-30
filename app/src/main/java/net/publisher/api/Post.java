package net.publisher.api;

import android.os.Parcel;

import com.vk.sdk.api.model.VKApiPost;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Vk api post full.
 * Created by Mayboroda on 10/3/15.
 */
public class Post extends VKApiPost {

    public int sourceId;
    public String textAbout;
    public long dateAbout;

    public Post() {
        /* Empty */
    }

    public static Creator CREATOR = new Creator() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public Post(Parcel parcel) {
        super(parcel);
        sourceId = parcel.readInt();
        dateAbout = parcel.readLong();
        textAbout = parcel.readString();
    }

    @Override
    public Post parse(JSONObject source) throws JSONException {
        super.parse(source);
        sourceId = source.getInt("source_id");
        dateAbout = source.getLong("date");
        textAbout = source.getString("text");
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(sourceId);
        dest.writeLong(dateAbout);
        dest.writeString(textAbout);
    }
}
