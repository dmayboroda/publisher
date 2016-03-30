package net.publisher.api;

import android.os.Parcel;

import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * User parsing model.
 * Created by Mayboroda on 6/17/15.
 */
public class User extends VKApiModel{

    private VKList<VKApiUser> users = new VKList<>();

    public User() {
        /* parsing only. */
    }

    public User(Parcel parcel) {
        users = parcel.readParcelable(VKList.class.getClassLoader());
    }

    @Override
    public VKApiModel parse(JSONObject response) throws JSONException {
        JSONArray array = response.getJSONArray("response");
        users = new VKList<VKApiUser>(array, VKApiUser.class);
        return this;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(users, flags);
    }

    public VKList<VKApiUser> getUsers() { return users; }

    public static Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
