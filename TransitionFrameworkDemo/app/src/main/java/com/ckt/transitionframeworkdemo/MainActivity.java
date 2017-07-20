package com.ckt.transitionframeworkdemo;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Scene mScene1;
    private Scene mScene2;
    private Scene mCurrentScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        mScene1 = new Scene(sceneRoot, findViewById(R.id.scene1));
        mCurrentScene = mScene1;
        mScene1.setEnterAction(new Runnable() {
            @Override
            public void run() {
                startFabAnimation();
            }
        });

        mScene2 = Scene.getSceneForLayout(sceneRoot, R.layout.scene2, this);
        mScene2.setEnterAction(new Runnable() {
            @Override
            public void run() {
                RecyclerView tracks = (RecyclerView) findViewById(R.id.tracks);
                tracks.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
                tracks.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                tracks.setAdapter(new TracksAdapter());
                startFabAnimation();
            }
        });
    }

    private void startFabAnimation() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) fab.getDrawable();
        drawable.start();
    }

    public void onFabClick(View view) {
        if (mCurrentScene == mScene1) {
            changeScene(mScene2);
        } else if (mCurrentScene == mScene2) {
            changeScene(mScene1);
        }
    }

    private class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.VH> {
        private String[] songs =
                new String[]{"Lose Yourself - Eminem", "When I'm Gone - Eminem",
                        "Sing For The Moment - Eminem", "The Way I Am - Eminem",
                        "Superman - Eminem", "So Bad - Eminem",
                        "Love The Way You Lie - Eminem", "Beautiful - Eminem",
                        "Not Afraid - Eminem", "You Don't Know -Eminem"};


        @Override
        public TracksAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            return new VH(rootView);
        }

        @Override
        public void onBindViewHolder(final TracksAdapter.VH holder, int position) {
            holder.mTitleView.setText(songs[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeScene(mScene1);
                }
            });
        }

        @Override
        public int getItemCount() {
            return songs.length;
        }

        class VH extends RecyclerView.ViewHolder {
            TextView mTitleView;

            VH(View itemView) {
                super(itemView);
                mTitleView = itemView.findViewById(android.R.id.text1);
            }
        }
    }


    private void changeScene(Scene scene) {
        Transition transition = new ChangeBounds();
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(R.id.tracks);
        set.addTransition(slide);
        set.addTransition(transition);
        mCurrentScene = scene;
        TransitionManager.go(scene, set);
    }

}
