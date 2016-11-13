package ru.test.drom.dromtest.mvp.presenters;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import ru.test.drom.dromtest.App;
import ru.test.drom.dromtest.app.GithubApi;
import ru.test.drom.dromtest.mvp.GithubService;
import ru.test.drom.dromtest.mvp.common.RxUtils;
import ru.test.drom.dromtest.mvp.models.Repositories;
import ru.test.drom.dromtest.mvp.views.UsersView;

@InjectViewState
public class RepositoriesPresenter extends MvpPresenter<UsersView> {
    @Inject
    Context mContext;
    @Inject
    GithubService mGithubService;

    public RepositoriesPresenter() {
        App.getAppComponent().inject(this);
    }

    public void loadRepositories(String query) {
        getViewState().showProgress();

        searchUsers(query, 1, false);
    }

    public void loadNextRepositories(String query, int currentCount) {
        int page = currentCount / GithubApi.PAGE_SIZE + 1;

        searchUsers(query, page, true);
    }

    public void searchUsers(String query, int page, final boolean isPageLoading) {
        final Observable<Repositories> userObservable = RxUtils.wrapRetrofitCall(mGithubService.searchUsers(query, page, GithubApi.PAGE_SIZE));
        RxUtils.wrapAsync(userObservable)
                .subscribe(new Consumer<Repositories>() {
                    @Override
                    public void accept(Repositories repositories) throws Exception {
                        getViewState().hideProgress();
                        onLoadingSuccess(isPageLoading, repositories);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onLoadingFailed(throwable.getMessage());
                    }
                });
    }

    private void onLoadingSuccess(boolean isPageLoading, Repositories repositories) {
        boolean maybeMore = repositories.items.size() >= GithubApi.PAGE_SIZE;
        if (isPageLoading) {
            getViewState().addRepositories(repositories, maybeMore);
        } else {
            getViewState().setRepositories(repositories, maybeMore);
        }
    }

    private void onLoadingFailed(String error) {
        getViewState().showError(error);
    }

    public void onErrorCancel() {
        getViewState().hideError();
    }
}
