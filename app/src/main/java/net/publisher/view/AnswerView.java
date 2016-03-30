package net.publisher.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiPoll;

import net.publisher.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Poll answer view
 * Created by Mayboroda on 10/20/15.
 */
public class AnswerView extends RelativeLayout {

    @Bind(R.id.answer_name) TextView answerName;
    @Bind(R.id.answer_desc) TextView answerDesc;

    public AnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bind(VKApiPoll.Answer answer) {
        if (!TextUtils.isEmpty(answer.text)) {
            answerName.setVisibility(VISIBLE);
            answerName.setText(answer.text.trim());
        } else {
            answerName.setVisibility(GONE);
        }
        String votes = String.format(getContext()
                        .getString(R.string.poll_votes),
                        answer.votes);
        answerDesc.setText(votes);
    }
}
