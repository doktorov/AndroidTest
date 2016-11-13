package ru.test.drom.dromtest.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import ru.test.drom.dromtest.R;
import ru.test.drom.dromtest.mvp.common.MvpAppCompatActivity;
import ru.test.drom.dromtest.mvp.models.Repositories;
import ru.test.drom.dromtest.mvp.presenters.RepositoriesPresenter;
import ru.test.drom.dromtest.mvp.views.RepositoriesView;
import ru.test.drom.dromtest.ui.adapters.RepositoryAdapter;
import ru.test.drom.dromtest.ui.listeners.OnScrollToBottomListener;

/*
Тестовое задание: «Приложение состоит из одной активности. В тулбаре название приложения и иконка поиска.
При нажатии на поиск открывается SearchView. При вводе символов, используя GitHub Search API (https://developer.github.com/v3/search/),
подгружается результат поиска и отображается список. В каждом элементе списка должны присутствовать аватара владельца репозитория,
полное название репозитория и его описание. Остальные поля можно добавить по желанию исполнителя.
При скроллинге данные должны догружаться (ленивая подгрузка). Поворот экрана должен быть включен и корректно обработан. Исходный код должен быть на Java.»
 */
public class MainActivity extends MvpAppCompatActivity implements RepositoriesView,
        OnScrollToBottomListener {
    @InjectPresenter
    RepositoriesPresenter mRepositoriesPresenter;

    private Toolbar toolbar;

    private RepositoryAdapter mRepositoryAdapter;

    private RecyclerView mRepositoriesListView;
    private ProgressBar mRepositoriesProgressBar;
    private View mRepositoryFormView;
    private View mProgressView;

    private CharSequence mQuery;

    private static final String QUERY_VALUE = "queryValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.activity_home_toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            mQuery = savedInstanceState.getCharSequence(QUERY_VALUE);
        }

        mRepositoriesListView = (RecyclerView) findViewById(R.id.activity_home_list_view_repositories);
        mRepositoriesProgressBar = (ProgressBar) findViewById(R.id.activity_home_progress_bar_repositories);
        mRepositoryFormView = findViewById(R.id.repository_form);
        mProgressView = findViewById(R.id.repository_progress);

        mRepositoriesListView.setLayoutManager(new LinearLayoutManager(this));
        mRepositoryAdapter = new RepositoryAdapter(mRepositoriesListView, this);
        mRepositoriesListView.setAdapter(mRepositoryAdapter);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!TextUtils.isEmpty(mQuery)) {
            outState.putCharSequence(QUERY_VALUE, mQuery.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_user_search, menu);
        final MenuItem searchActionMenuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) searchActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;

                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }

                toolbar.setTitle(mQuery.toString());

                searchActionMenuItem.collapseActionView();

                mRepositoriesPresenter.loadRepositories(mQuery.toString());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchActionMenuItem.expandActionView();

        if (!TextUtils.isEmpty(mQuery)) {
            searchActionMenuItem.expandActionView();
            searchView.setQuery(mQuery.toString(), false);
            searchView.clearFocus();
        }

        return true;
    }

    @Override
    public void showProgress() {
        toggleProgressVisibility(true);
    }

    @Override
    public void hideProgress() {
        toggleProgressVisibility(false);
    }

    private void toggleProgressVisibility(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mRepositoryFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        final Snackbar snackbar = Snackbar
                .make(mRepositoriesListView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }

    @Override
    public void showListProgress() {
        mRepositoriesListView.setVisibility(View.GONE);
        mRepositoriesProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListProgress() {
        mRepositoriesListView.setVisibility(View.VISIBLE);
        mRepositoriesProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setRepositories(Repositories repositories, boolean maybeMore) {
        mRepositoryAdapter.setRepositories(repositories.items);
    }

    @Override
    public void addRepositories(Repositories repositories, boolean maybeMore) {
        mRepositoryAdapter.addRepositories(repositories.items);
    }

    @Override
    public void onScrollToBottom() {
        mRepositoriesPresenter.loadNextRepositories(mQuery.toString(), mRepositoryAdapter.getItemCount());
    }
}
