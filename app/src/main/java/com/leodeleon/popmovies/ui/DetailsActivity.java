package com.leodeleon.popmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.gson.Gson;
import com.leodeleon.popmovies.BuildConfig;
import com.leodeleon.popmovies.R;
import com.leodeleon.popmovies.adapters.TrailerAdapter;
import com.leodeleon.popmovies.api.MovieCalls;
import com.leodeleon.popmovies.interfaces.MovieDetailCallback;
import com.leodeleon.popmovies.interfaces.StringsCallback;
import com.leodeleon.popmovies.interfaces.VideosCallback;
import com.leodeleon.popmovies.model.Movie;
import com.leodeleon.popmovies.model.MovieDetail;
import com.leodeleon.popmovies.model.Video;
import com.leodeleon.popmovies.util.GlideHelper;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetailsActivity extends AppCompatActivity {

  public final static String MOVIE = "movie";

  @BindView(R.id.backdrop)
  ImageView mBackdropView;
  @BindView(R.id.collapsing_toolbar)
  CollapsingToolbarLayout mCollapsingToolbar;
  @BindView(R.id.fab)
  FloatingActionButton mFloatingButton;
  @BindView(R.id.poster)
  ImageView mPosterView;
  @BindView(R.id.vote_average)
  TextView mVoteAvgText;
  @BindView(R.id.release_date)
  TextView mYearText;
  @BindView(R.id.videos)
  TextView mVideosText;
  @BindView(R.id.runtime)
  TextView mRuntimeText;
  @BindView(R.id.overview)
  TextView mOverviewText;
  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;

  @BindString(R.string.lorem_ipsum)
  String title;
  @BindString(R.string.runtime_label)
  String runtime;
  @BindString(R.string.voting_label)
  String voting;

  MovieDetail movieDetail;
  Movie movie;
  ArrayList<String> videoIds;

  Unbinder unbinder;
  TrailerAdapter trailerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    unbinder = ButterKnife.bind(this);
    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    String movieJson = getIntent().getStringExtra(MOVIE);
    movie = new Gson().fromJson(movieJson, Movie.class);
    bindViews();
    getDetails();
    getVideos();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        supportFinishAfterTransition();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setRecyclerView() {
    if (videoIds.size() > 0) {
      trailerAdapter = new TrailerAdapter(this, videoIds);
      mRecyclerView.setAdapter(trailerAdapter);
    } else {
      mRecyclerView.setVisibility(View.GONE);
      mVideosText.setVisibility(View.GONE);
    }

  }

  private void getDetails() {
    MovieCalls.getInstance().getMovieDetail(movie.getId(), new MovieDetailCallback() {
      @Override
      public void callback(MovieDetail movie) {
        movieDetail = movie;
        mRuntimeText.setText(String.format(runtime, movie.getRuntime()));
      }
    });
  }

  private void getVideos() {
    MovieCalls.getInstance().getVideos(movie.getId(), new StringsCallback() {
      @Override
      public void callback(ArrayList<String> videos) {
        videoIds = videos;
        setRecyclerView();
      }
    });
  }

  public void startVideo(String videoId) {
    Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, BuildConfig.GOOGLE_APIS_KEY, videoId, 0, false, true);
    startActivity(intent);
  }

  private void bindViews() {
    GlideHelper.loadBackdrop(this, movie.getBackdropPath(), mBackdropView);
    GlideHelper.loadPoster(this, movie.getPosterPath(), mPosterView);
    mCollapsingToolbar.setTitle(movie.getTitle());
    mVoteAvgText.setText(String.format(voting, movie.getVoteAverage()));
    mYearText.setText(movie.getReleaseDate());
    mOverviewText.setText(movie.getOverview());
  }

  @OnClick({R.id.fab})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.fab:
        mFloatingButton.setSelected(!mFloatingButton.isSelected());
        break;
    }
  }
}
