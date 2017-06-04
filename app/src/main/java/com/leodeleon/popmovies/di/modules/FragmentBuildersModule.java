package com.leodeleon.popmovies.di.modules;

import com.leodeleon.popmovies.feature.view.DetailFragment;
import com.leodeleon.popmovies.feature.view.MoviesFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module public abstract class FragmentBuildersModule {
  @ContributesAndroidInjector
  abstract MoviesFragment moviesFragment();

  @ContributesAndroidInjector
  abstract DetailFragment detailFragment();
}
