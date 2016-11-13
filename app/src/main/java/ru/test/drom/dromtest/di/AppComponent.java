package ru.test.drom.dromtest.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.test.drom.dromtest.di.modules.ContextModule;
import ru.test.drom.dromtest.di.modules.GithubModule;
import ru.test.drom.dromtest.mvp.GithubService;
import ru.test.drom.dromtest.mvp.presenters.RepositoriesPresenter;

@Singleton
@Component(modules = {ContextModule.class, GithubModule.class})
public interface AppComponent {
    Context getContext();
    GithubService getAuthService();

    void inject(RepositoriesPresenter presenter);
}
