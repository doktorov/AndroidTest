package ru.test.drom.dromtest.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.test.drom.dromtest.mvp.models.Repositories;
import ru.test.drom.dromtest.mvp.views.RepositoryView;

@InjectViewState
public class UserPresenter extends MvpPresenter<RepositoryView> {
    private boolean mIsInitialized = false;

    public UserPresenter() {
        super();
    }

    public void setRepository(Repositories.Item repository) {
        if (mIsInitialized) {
            return;
        }
        mIsInitialized = true;

        getViewState().showRepository(repository);
    }
}
