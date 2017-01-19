package ru.test.doktorov.androidtest.mvp;

import retrofit2.Call;
import ru.test.doktorov.androidtest.app.GithubApi;
import ru.test.doktorov.androidtest.mvp.models.Repositories;

public class GithubService {
    private GithubApi mGithubApi;

    public GithubService(GithubApi githubApi) {
        mGithubApi = githubApi;
    }

    public Call<Repositories> searchUsers(String query, int page, int perPage) {
        return mGithubApi.searchUsers(query, page, perPage);
    }
}
