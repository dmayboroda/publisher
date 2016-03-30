package net.publisher.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKList;

import net.publisher.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Photo attachment view.
 * Created by Mayboroda on 9/28/15.
 */
public class PhotoAttach extends RelativeLayout {

    @Bind(R.id.photo_prev_land)   ImageView land;
    @Bind(R.id.photo_prev_port)   ImageView port;
    @Bind(R.id.photo_count)       TextView count;

    public PhotoAttach(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bind(VKList<VKApiPhoto> photoList) {
        if (photoList.size() > 1) {
            count.setVisibility(VISIBLE);
            count.setText((photoList.size() - 1) + "+");
        } else {
            count.setVisibility(GONE);
        }

        VKApiPhoto first  = photoList.get(0);
        String url = !TextUtils.isEmpty(first.photo_604) ?
                first.photo_604 : first.photo_807;

        if (TextUtils.isEmpty(url)) { return; }

        if (first.height > first.width) {
            land.setVisibility(GONE);
            port.setVisibility(VISIBLE);
            Picasso.with(getContext())
                    .load(url)
                    .into(port);
        } else {
            port.setVisibility(GONE);
            land.setVisibility(VISIBLE);
            Picasso.with(getContext())
                    .load(url)
                    .into(land);
        }
    }
}
