package net.publisher.api;

import android.os.Parcel;

import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * News feed parsing model.
 * Created by Mayboroda on 6/11/15.
 */
public class Feed extends VKApiModel {

    private VKList<Post>           items    = new VKList<>();
    private VKList<VKApiUser>      profiles = new VKList<>();
    private VKList<VKApiCommunity> groups   = new VKList<>();
    private String                 next;
    private int                    count;


    public Feed() {
        /* Default constructor for context visibility, used for parsing */
    }

    public Feed(Parcel parcel) {
        items = parcel.readParcelable(VKList.class.getClassLoader());
        profiles = parcel.readParcelable(VKList.class.getClassLoader());
        groups = parcel.readParcelable(VKList.class.getClassLoader());
        next = parcel.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(items, flags);
        dest.writeParcelable(profiles, flags);
        dest.writeParcelable(groups, flags);
        dest.writeString(next);
        dest.writeInt(count);
    }

    @Override
    public VKApiModel parse(JSONObject response) throws JSONException {
        JSONObject outer = response.getJSONObject("response");
        if (outer.has("items")) {
            JSONArray items = outer.getJSONArray("items");
            this.items = new VKList<Post>(items, Post.class);
        }
        if (outer.has("profiles")) {
            JSONArray profiles = outer.getJSONArray("profiles");
            this.profiles = new VKList<VKApiUser>(profiles, VKApiUser.class);
        }
        if (outer.has("groups")) {
            JSONArray groups = outer.getJSONArray("groups");
            this.groups = new VKList<VKApiCommunity>(groups, VKApiCommunity.class);
        }
        if (outer.has("next_from")) {
            next = outer.optString("next_from");
        }
        if (outer.has("count")) {
            count = outer.getInt("count");
        }
        return this;
    }

    public VKList<Post> getItems() { return items; }

    public VKList<VKApiUser> getProfiles() { return profiles; }

    public VKList<VKApiCommunity> getGroups() { return groups; }

    public String getNext() { return next; }

    public int getCount() { return count; }

    public static Creator<Feed> CREATOR = new Creator<Feed>() {
        public Feed createFromParcel(Parcel source) {
            return new Feed(source);
        }

        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
}
