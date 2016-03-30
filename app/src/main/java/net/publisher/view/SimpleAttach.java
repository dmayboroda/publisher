package net.publisher.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKApiLink;
import com.vk.sdk.api.model.VKApiPoll;
import com.vk.sdk.api.model.VKApiWikiPage;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKList;

import net.publisher.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Simple attachment view.
 * Created by Mayboroda on 9/28/15.
 */
public class SimpleAttach extends RelativeLayout {

    @Bind(R.id.attach_icon)   ImageView icon;
    @Bind(R.id.attach_name)   TextView name;
    @Bind(R.id.attach_desc)   TextView desc;

    public SimpleAttach(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bind(VKAttachments.VKApiAttachment attachment) {
        if (attachment == null) { return; }
        if (attachment instanceof VKApiWikiPage) {
            final VKApiWikiPage wiki = (VKApiWikiPage)attachment;
            String details = !TextUtils.isEmpty(wiki.parent)
                    ? wiki.parent
                    : getContext().getString(R.string.wiki);
            setAttachInfo(wiki.title,
                    details,
                    R.drawable.ic_reply_white_24dp);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(wiki.source)) {
                        browse(wiki.source);
                    }
                }
            });
        }

        if (attachment instanceof VKApiAudio) {
            final VKApiAudio audio = (VKApiAudio)attachment;
            setAttachInfo(audio.artist,
                    audio.title,
                    R.drawable.ic_play_arrow_white_24dp);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(audio.url)){
                        browse(audio.url);
                    }
                }
            });
        }

        if (attachment instanceof VKApiLink) {
            final VKApiLink link = (VKApiLink) attachment;
            String title = TextUtils.isEmpty(link.title)
                    ? getContext().getString(R.string.link_title)
                    : link.title;
            setAttachInfo(title,
                    link.description,
                    R.drawable.ic_link_white_24dp);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(link.url)) {
                        browse(link.url);
                    }
                }
            });
        }

        if (attachment instanceof VKApiPoll) {
            final VKApiPoll poll = (VKApiPoll)attachment;
            final PollView pollView = (PollView)LayoutInflater.from(getContext())
                                .inflate(R.layout.poll_layout, this, false);
            String describe = String.format(
                    getContext().getString(R.string.poll_votes),
                    poll.votes);
            setAttachInfo(poll.question,
                    describe,
                    R.drawable.ic_insert_chart_white_24dp);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    VKList<VKApiPoll.Answer> answers = poll.answers;
                    if (answers != null && !answers.isEmpty()) {
                        pollView.bind(answers);
                        AppCompatDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle(poll.question)
                                .setView(pollView)
                                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                }
            });
        }

        if (attachment instanceof VKApiDocument) {
            final VKApiDocument doc = (VKApiDocument)attachment;
            setAttachInfo(doc.title,
                    doc.ext,
                    R.drawable.ic_attach_file_white_24dp);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(doc.url)) {
                        browse(doc.url);
                    }
                }
            });
        }
    }

    private void setAttachInfo(String title, String description, int iconId) {
        Picasso.with(getContext())
                .load(iconId)
                .into(icon);

        if (!TextUtils.isEmpty(title)) {
            name.setText(title.trim());
        }

        if (!TextUtils.isEmpty(description)) {
            desc.setText(description.trim());
        }
    }

    private void browse(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getContext().startActivity(intent);
    }

}
