package net.publisher.api.method;

import android.content.Context;

import net.publisher.api.Groups;

import java.util.HashMap;
import java.util.Map;

/**
 * Groups request method.
 * Created by Mayboroda on 9/24/15.
 */
public class GroupsRequestMethod extends AbsRequestMethod<Groups>{

    private static final String FILTER_GROUPS           = "admin,editor,moder,groups,publics";
    private static final String GROUPS                  = "groups.get";
    private static final String USER_ID                 = "user_id";
    private static final String OFFSET                  = "offset";
    private static final int EXTENDED                   = 1;
    private static final int GROUPS_COUNT               = 20;

    private static final Map<String, Object> GROUPS_VK_PARAMS = new HashMap<>(5);

    static {
        GROUPS_VK_PARAMS.put("extended", EXTENDED);
        GROUPS_VK_PARAMS.put("count", GROUPS_COUNT);
    }

    private int COUNTER = 0;

    public GroupsRequestMethod(Context context, OnFetchUpdate<Groups> listener, String id)  {
        super(context, listener);
        GROUPS_VK_PARAMS.put("filter",FILTER_GROUPS);
        GROUPS_VK_PARAMS.put(USER_ID, id);
        createRequest(GROUPS, GROUPS_VK_PARAMS, Groups.class);
    }

    @Override
    public void reset() {
        GROUPS_VK_PARAMS.remove(OFFSET);
        recreateRequest(GROUPS_VK_PARAMS);
    }

    @Override
    public void next(Groups model) {
        int count = model.getCount();
        COUNTER += GROUPS_COUNT;
        if (COUNTER < count) {
            GROUPS_VK_PARAMS.put(OFFSET, COUNTER);
            recreateRequest(GROUPS_VK_PARAMS);
        } else {
            stop();
        }
     }
}
