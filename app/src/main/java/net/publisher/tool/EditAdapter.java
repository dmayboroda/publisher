package net.publisher.tool;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.vk.sdk.api.model.VKAttachments;

import net.publisher.api.Post;

/**
 * Edit post adapter.
 * Created by Mayboroda on 10/15/15.
 */
public class EditAdapter extends BaseAdapter<VKAttachments>{

    /** Attachment list. */
    private VKAttachments attachments = new VKAttachments();
    /** Post for editing.*/
    private Post post;

    public EditAdapter() {
        /* nothing */
    }

    @Override
    public void setData(VKAttachments model) {
        clear();
        attachments.addAll(model);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        attachments.clear();
    }

    public void setupPost(Post post) {
        this.post = post;
    }

    public void setup(){
        if (post != null) {

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() { return attachments.size(); }
}
