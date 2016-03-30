package net.publisher.api;

import android.os.Parcel;

import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Groups container model.
 * Created by Mayboroda on 6/12/15.
 */
public class Groups extends VKApiModel {

    private int count;
    private VKList<VKApiCommunity> items = new VKList<>();

    public Groups() {
        /* for parsing */
    }

    @Override
    public VKApiModel parse(JSONObject response) throws JSONException {
        JSONObject outer = response.getJSONObject("response");
        if (outer.has("count")) {
            count = outer.getInt("count");
        }
        if (outer.has("items")) {
            JSONArray items = outer.getJSONArray("items");
            this.items = new VKList<VKApiCommunity>(items, VKApiCommunity.class);
        }
        return this;
    }

    public int getCount() { return count; }

    public VKList<VKApiCommunity> getItems() { return items; }

    public Groups(Parcel parcel) {
        count = parcel.readInt();
        items = parcel.readParcelable(VKList.class.getClassLoader());
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(items, flags);
        dest.writeInt(count);
    }

    public static Creator<Groups> CREATOR = new Creator<Groups>() {
        public Groups createFromParcel(Parcel source) {
            return new Groups(source);
        }

        public Groups[] newArray(int size) {
            return new Groups[size];
        }
    };
}
