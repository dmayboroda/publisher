package net.publisher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.vk.sdk.api.model.VKApiPoll;
import com.vk.sdk.api.model.VKList;

import net.publisher.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Poll dialog view.
 * Created by Mayboroda on 10/21/15.
 */
public class PollView extends ScrollView{

    @Bind(R.id.poll_answers) ViewGroup answersContainer;

    public PollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bind(VKList<VKApiPoll.Answer> answers) {
        for (VKApiPoll.Answer answer : answers) {
            AnswerView answerView = (AnswerView)LayoutInflater.from(getContext())
                    .inflate(R.layout.poll_answer, this, false);
            answerView.bind(answer);
            answersContainer.addView(answerView);
        }
    }
}
