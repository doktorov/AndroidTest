package ru.test.drom.dromtest.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.test.drom.dromtest.app.GithubApi;
import ru.test.drom.dromtest.mvp.GithubService;

@Module(includes = {ApiModule.class})
public class GithubModule {
    @Provides
    @Singleton
    public GithubService provideGithubService(GithubApi authApi) {
        return new GithubService(authApi);
    }
}