package ru.test.doktorov.androidtest.ui.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.test.doktorov.androidtest.R;
import ru.test.doktorov.androidtest.mvp.models.Repositories;
import ru.test.doktorov.androidtest.mvp.views.RepositoryView;
import ru.test.doktorov.androidtest.ui.listeners.OnScrollToBottomListener;

public class RepositoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int REPOSITORY_VIEW_TYPE = 0;
    private final int PROGRESS_VIEW_TYPE = 1;

    private OnScrollToBottomListener mOnLoadMoreListener;

    private boolean mIsLoading;
    private int mVisibleThreshold = 5;
    private int mLastVisibleItem;
    private int mTotalItemCount;

    private List<Repositories.Item> mRepositories;

    private Context mContext;
    private LinearLayoutManager linearLayoutManager;

    public RepositoryAdapter(RecyclerView recyclerView, OnScrollToBottomListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
        mRepositories = new ArrayList<>();

        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!mIsLoading && mTotalItemCount <= (mLastVisibleItem + mVisibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onScrollToBottom();
                    }
                    mIsLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mRepositories.size() - 1) ? PROGRESS_VIEW_TYPE : REPOSITORY_VIEW_TYPE;
    }

    public void setRepositories(List<Repositories.Item> data) {
        mRepositories = data;

        notifyDataSetChanged();
    }

    public void addRepositories(List<Repositories.Item> data) {
        mRepositories.addAll(data);

        setLoaded();

        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        switch (viewType) {
            case REPOSITORY_VIEW_TYPE:
                View viewRepository = LayoutInflater.from(mContext).inflate(R.layout.layout_user_item, parent, false);
                return new UserViewHolder(viewRepository);
            case PROGRESS_VIEW_TYPE:
                View viewProgress = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(viewProgress);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            Repositories.Item repository = mRepositories.get(position);
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.showRepository(repository);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.showRepository(null);
        }
    }

    @Override
    public int getItemCount() {
        return mRepositories == null ? 0 : mRepositories.size();
    }

    public void setLoaded() {
        mIsLoading = false;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder implements RepositoryView {
        private View mView;

        private TextView mNameTextView;
        private TextView mDescriptionTextView;
        private ImageButton mRepositoryImageButton;

        public UserViewHolder(View view) {
            super(view);

            this.mView = view;

            mNameTextView = (TextView) view.findViewById(R.id.item_repository_text_view_name);
            mDescriptionTextView = (TextView) view.findViewById(R.id.item_repository_text_view_description);
            mRepositoryImageButton = (ImageButton) view.findViewById(R.id.item_repository_image);
        }

        @Override
        public void showRepository(Repositories.Item repository) {
            mNameTextView.setText(repository.getName());
            mDescriptionTextView.setText(repository.getDescription());
            Picasso.with(mView.getContext()).load(repository.owner.getAvatarUrl()).into(mRepositoryImageButton);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder implements RepositoryView {
        public ProgressBar mProgressBar;

        public LoadingViewHolder(View view) {
            super(view);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }

        @Override
        public void showRepository(Repositories.Item user) {
            mProgressBar.setIndeterminate(true);
        }
    }
}
