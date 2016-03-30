package net.publisher.tool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKList;

import net.publisher.R;
import net.publisher.Utils;
import net.publisher.api.Groups;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Groups adapter.
 * Created by Mayboroda on 9/25/15.
 */
public class GroupsAdapter extends BaseAdapter<Groups> {

    /** Listener for group touch.*/
    public interface OnGroupClick {
        void onGroupClick(VKApiCommunity community);
    }

    private VKList<VKApiCommunity> items = new VKList<>();
    private boolean clear = false;

    public OnGroupClick onGroupClick;

    public GroupsAdapter(OnGroupClick onGroupClick) {
        this.onGroupClick = onGroupClick;
    }

    @Override
    public void setData(Groups model) {
        if (clear) {
            clear = false;
            items.clear();
        }
        items.addAll(model.getItems());
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        clear = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_card, parent, false);
        return new GroupsHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GroupsHolder binder = (GroupsHolder) holder;
        VKApiCommunity community = items.get(position);
        binder.bind(community, onGroupClick);
    }

    @Override
    public int getItemCount() { return items.size(); }


    /** Group card view holder. */
    public static class GroupsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.group_name)    TextView name;
        @Bind(R.id.group_avatar)  ImageView avatar;
        private Context context;
        private VKApiCommunity community;
        private OnGroupClick onGroupClick;

        public GroupsHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            name.setTypeface(Utils.typeface(context, Utils.ROBOTO_MEDIUM));
        }

        public void bind(VKApiCommunity community, OnGroupClick onGroupClick) {
            this.community = community;
            this.onGroupClick = onGroupClick;
            if (!TextUtils.isEmpty(community.name)) {
                name.setText(community.name);
            }

            String url = !TextUtils.isEmpty(community.photo_200)
                    ? community.photo_200
                    : community.photo_100;

            if (!TextUtils.isEmpty(url)) {
                Picasso.with(context)
                        .load(url)
                        .into(avatar);
            }
        }

        @Override
        public void onClick(View v) {
            if (onGroupClick != null && community != null) {
                onGroupClick.onGroupClick(community);
            }
        }
    }

}
