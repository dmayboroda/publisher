package net.publisher.api.method;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiModel;

import net.publisher.Utils;

import java.util.Map;

/**
 * Abstract request method.
 * Created by Mayboroda on 9/22/15.
 */
public abstract class AbsRequestMethod<M extends VKApiModel> implements RequestMethod<M> {

    public interface OnFetchUpdate<M> {
        void onError(VKError error);
        void onSuccess(M model);
        void onFetch();
    }

    private Context context;
    private VKRequest request;
    private OnFetchUpdate<M> listener;

    private String where;
    private Class<M> type;

    public AbsRequestMethod(Context context, OnFetchUpdate<M> listener) {
        this.context = context;
        this.listener = listener;
    }

    public void createRequest(String where,
                                      Map<String, Object> params,
                                      Class<M> type) {
        this.type = type;
        this.where = where;
        VKParameters parameters = new VKParameters(params);
        request = new VKRequest(where, parameters);
        request.setModelClass(type);
    }

    protected void recreateRequest(Map<String, Object> param) {
        if (!TextUtils.isEmpty(where) && type != null) {
            VKParameters parameters = new VKParameters(param);
            request = new VKRequest(where, parameters);
            request.setModelClass(type);
        }
    }

    public void fetch() {
        if (request != null && Utils.checkConnection(context)) {
            listener.onFetch();
            request.executeWithListener(new OnRequest<M>(listener, this));
        }
    }

    public void cancel() {
        if (request != null) {
            request.cancel();
        }
    }

    @Override
    public void stop() { request = null; }

    private static class OnRequest<M extends VKApiModel> extends VKRequest.VKRequestListener {

        private OnFetchUpdate<M> onFetchUpdate;
        private Next<M> next;

        public OnRequest(OnFetchUpdate<M> onFetchUpdate, Next<M> next) {
            this.onFetchUpdate = onFetchUpdate;
            this.next = next;
        }

        @Override
        public void onError(VKError error) {
            onFetchUpdate.onError(error);
        }

        @Override
        public void onComplete(VKResponse response) {
            if (response == null) { return; }
            Object model = response.parsedModel;
            if (model != null) {
                M data = (M) model;
                next.next(data);
                onFetchUpdate.onSuccess(data);
            }
        }
    }

}
