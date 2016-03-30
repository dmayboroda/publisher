package net.publisher.api.method;

import com.vk.sdk.api.model.VKApiModel;

import java.util.Map;

/**
 * Request strategy for different types of requests.
 * Created by Mayboroda on 9/22/15.
 */
public interface RequestMethod<M extends VKApiModel> extends Next<M> {

    void createRequest(String where,
                            Map<String, Object> params,
                            Class<M> type);

    void fetch();

    void cancel();

    void reset();

    void stop();

}
