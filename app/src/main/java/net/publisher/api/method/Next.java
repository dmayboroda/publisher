package net.publisher.api.method;

import com.vk.sdk.api.model.VKApiModel;

/**
 * Next request creator;
 * Created by Mayboroda on 9/22/15.
 */
public interface Next<M extends VKApiModel> {

    void next(M model);
}
