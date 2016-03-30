package net.publisher.api;

import android.os.Parcel;

import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Wall entity
 * Created by Mayboroda on 10/15/15.
 */
public class Wall extends VKApiModel {

    private VKList<VKApiPost>      items    = new VKList<>();
    private VKList<VKApiUser>      profiles = new VKList<>();
    private VKList<VKApiCommunity> groups   = new VKList<>();
    private int                    count;

    public Wall(){
        /* for parsing only */
    }

    public Wall(Parcel parcel) {
        items = parcel.readParcelable(VKList.class.getClassLoader());
        profiles = parcel.readParcelable(VKList.class.getClassLoader());
        groups = parcel.readParcelable(VKList.class.getClassLoader());
        count = parcel.readInt();
    }

    @Override
    public VKApiModel parse(JSONObject response) throws JSONException {
        JSONObject outer = response.getJSONObject("response");
        if (outer.has("items")) {
            JSONArray items = outer.getJSONArray("items");
            this.items = new VKList<VKApiPost>(items, VKApiPost.class);
        }
        if (outer.has("profiles")) {
            JSONArray profiles = outer.getJSONArray("profiles");
            this.profiles = new VKList<VKApiUser>(profiles, VKApiUser.class);
        }
        if (outer.has("groups")) {
            JSONArray groups = outer.getJSONArray("groups");
            this.groups = new VKList<VKApiCommunity>(groups, VKApiCommunity.class);
        }
        if (outer.has("count")) {
            count = outer.getInt("count");
        }
        return this;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(items, flags);
        dest.writeParcelable(profiles, flags);
        dest.writeParcelable(groups, flags);
        dest.writeInt(count);
    }

    public VKList<VKApiPost> getItems() { return items; }

    public VKList<VKApiUser> getProfiles() { return profiles; }

    public VKList<VKApiCommunity> getGroups() { return groups; }

    public int getCount() { return count; }

    public static Creator<Wall> CREATOR = new Creator<Wall>() {
        public Wall createFromParcel(Parcel source) {
            return new Wall(source);
        }

        public Wall[] newArray(int size) { return new Wall[size]; }
    };
}
