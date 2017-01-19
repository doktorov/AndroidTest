package ru.test.doktorov.androidtest.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.test.doktorov.androidtest.di.modules.ContextModule;
import ru.test.doktorov.androidtest.di.modules.GithubModule;
import ru.test.doktorov.androidtest.mvp.GithubService;
import ru.test.doktorov.androidtest.mvp.presenters.RepositoriesPresenter;

@Singleton
@Component(modules = {ContextModule.class, GithubModule.class})
public interface AppComponent {
    Context getContext();
    GithubService getAuthService();

    void inject(RepositoriesPresenter presenter);
}
