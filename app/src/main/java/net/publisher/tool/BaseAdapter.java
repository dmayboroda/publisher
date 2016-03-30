package net.publisher.tool;

import android.support.v7.widget.RecyclerView;

import com.vk.sdk.api.model.VKApiModel;

/**
 * Basic adapter.
 * M - model type.
 * Created by Mayboroda on 9/25/15.
 */
public abstract class BaseAdapter<M extends VKApiModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public abstract void setData(M model);

    public abstract void clear();
}
