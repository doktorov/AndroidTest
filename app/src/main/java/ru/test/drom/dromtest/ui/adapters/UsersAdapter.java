package ru.test.drom.dromtest.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.test.drom.dromtest.R;
import ru.test.drom.dromtest.mvp.models.Repositories;
import ru.test.drom.dromtest.mvp.presenters.UserPresenter;
import ru.test.drom.dromtest.mvp.views.RepositoryView;
import ru.test.drom.dromtest.ui.listeners.OnScrollToBottomListener;

public class UsersAdapter extends MvpBaseAdapter {
    public static final int REPOSITORY_VIEW_TYPE = 0;
    public static final int PROGRESS_VIEW_TYPE = 1;

    private List<Repositories.Item> mRepositories;
    private boolean mMaybeMore;
    private OnScrollToBottomListener mScrollToBottomListener;

    public UsersAdapter(MvpDelegate<?> parentDelegate, OnScrollToBottomListener scrollToBottomListener) {
        super(parentDelegate, String.valueOf(0));

        mScrollToBottomListener = scrollToBottomListener;
        mRepositories = new ArrayList<>();
    }

    public void setRepositories(List<Repositories.Item> repositories, boolean maybeMore) {
        mRepositories = new ArrayList<>(repositories);
        dataSetChanged(maybeMore);
    }

    public void addRepositories(List<Repositories.Item> repositories, boolean maybeMore) {
        mRepositories.addAll(repositories);
        dataSetChanged(maybeMore);
    }

    private void dataSetChanged(boolean maybeMore) {
        mMaybeMore = maybeMore;

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == mRepositories.size() ? PROGRESS_VIEW_TYPE : REPOSITORY_VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public int getRepositoriesCount() {
        return mRepositories.size();
    }

    @Override
    public int getCount() {
        return mRepositories.size() + (mMaybeMore ? 1 : 0);
    }

    @Override
    public Repositories.Item getItem(int position) {
        return mRepositories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == PROGRESS_VIEW_TYPE) {
            if (mScrollToBottomListener != null) {
                mScrollToBottomListener.onScrollToBottom();
            }

            return new ProgressBar(parent.getContext());
        }

        RepositoryHolder holder;
        if (convertView != null) {
            holder = (RepositoryHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository, parent, false);
            holder = new RepositoryHolder(convertView);
            convertView.setTag(holder);
        }

        final Repositories.Item item = getItem(position);

        holder.bind(item);

        return convertView;
    }

    class RepositoryHolder implements RepositoryView {
        @InjectPresenter(type = PresenterType.GLOBAL)
        UserPresenter mRepositoryPresenter;

        private Repositories.Item mRepository;

        @ProvidePresenterTag(presenterClass = UserPresenter.class, type = PresenterType.GLOBAL)
        String provideRepositoryPresenterTag() {
            return String.valueOf(mRepository.getId());
        }

        @ProvidePresenter(type = PresenterType.GLOBAL)
        UserPresenter provideRepositoryPresenter() {
            UserPresenter repositoryPresenter = new UserPresenter();
            repositoryPresenter.setRepository(mRepository);
            return repositoryPresenter;
        }

        private View mView;
        private TextView mNameTextView;
        private TextView mDescriptionTextView;
        private ImageButton mRepositoryImageButton;
        private MvpDelegate mMvpDelegate;

        RepositoryHolder(View view) {
            this.mView = view;

            mNameTextView = (TextView) view.findViewById(R.id.item_repository_text_view_name);
            mDescriptionTextView = (TextView) view.findViewById(R.id.item_repository_text_view_description);
            mRepositoryImageButton = (ImageButton) view.findViewById(R.id.item_repository_image);
        }

        void bind(final Repositories.Item repository) {
            if (getMvpDelegate() != null) {
                getMvpDelegate().onSaveInstanceState(getMvpDelegate().getChildrenSaveState());
                getMvpDelegate().onDetach();
                mMvpDelegate = null;
            }

            mRepository = repository;

            getMvpDelegate().onCreate(getMvpDelegate().getChildrenSaveState());
            getMvpDelegate().onAttach();
        }

        @Override
        public void showRepository(Repositories.Item repository) {
            mNameTextView.setText(repository.getName());
            mDescriptionTextView.setText(repository.getDescription());
            Picasso.with(mView.getContext()).load(repository.owner.getAvatarUrl()).into(mRepositoryImageButton);
        }

        MvpDelegate getMvpDelegate() {
            if (mRepository == null) {
                return null;
            }

            if (mMvpDelegate == null) {
                mMvpDelegate = new MvpDelegate<>(this);
                mMvpDelegate.setParentDelegate(UsersAdapter.this.getMvpDelegate(), String.valueOf(mRepository.getId()));

            }
            return mMvpDelegate;
        }
    }
}
