package ru.test.drom.dromtest.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.test.drom.dromtest.mvp.models.Repositories;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface UsersView extends MvpView {
    void showError(String message);

    void showListProgress();

    void hideListProgress();

    void showProgress();

    void hideProgress();

    void hideError();

    void setRepositories(Repositories repositories, boolean maybeMore);

    @StateStrategyType(AddToEndStrategy.class)
    void addRepositories(Repositories repositories, boolean maybeMore);
}

