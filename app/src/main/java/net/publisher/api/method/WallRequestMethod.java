package net.publisher.api.method;

import android.content.Context;

import net.publisher.api.Wall;

import java.util.HashMap;
import java.util.Map;

/**
 * Request method for wall;
 * Created by Mayboroda on 9/22/15.
 */
public class WallRequestMethod extends AbsRequestMethod<Wall> {

    private static final String WALL            = "wall.get";
    private static final String OWNER_ID        = "owner_id";
    private static final String OFFSET          = "offset";
    private static final int WALL_COUNT         = 20;
    private static final int EXTENDED           = 1;

    private static final Map<String, Object> WALL_VK_PARAMS = new HashMap<>(4);

    static {
        WALL_VK_PARAMS.put("extended", EXTENDED);
        WALL_VK_PARAMS.put("count", WALL_COUNT);
    }

    private int COUNTER = 0;

    public WallRequestMethod(Context context, OnFetchUpdate<Wall> listener, int id) {
        super(context, listener);
        WALL_VK_PARAMS.put(OWNER_ID, Integer.toString(id));
        createRequest(WALL, WALL_VK_PARAMS, Wall.class);
    }

    @Override
    public void reset() {
        WALL_VK_PARAMS.remove(OFFSET);
        recreateRequest(WALL_VK_PARAMS);
    }

    @Override
    public void next(Wall model) {
        int count = model.getCount();
        COUNTER += WALL_COUNT;
        if (COUNTER < count) {
            WALL_VK_PARAMS.put(OFFSET, COUNTER);
            recreateRequest(WALL_VK_PARAMS);
        } else {
            stop();
        }
    }
}
